package org.mbg.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.configuration.ValidationProperties;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.enums.TemplateCode;
import org.mbg.common.base.enums.TemplateField;
import org.mbg.common.base.model.TransactionLock;
import org.mbg.common.base.repository.TransactionLockRepository;
import org.mbg.common.label.Labels;
import org.mbg.common.util.RandomGenerator;
import org.mbg.common.util.StringPool;
import org.mbg.common.util.Validator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.mbg.api.response.SmsResponse;
import org.mbg.configuration.OtpProperties;
import org.mbg.enums.OtpType;
import org.mbg.model.OtpValue;
import org.mbg.repository.OtpRepository;
import org.mbg.service.OtpService;
import org.mbg.service.SmsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
	
    private final ValidationProperties validation;
    
    private final OtpProperties otpProperties;

	private final OtpRepository otpRepository;

	private final SmsService smsService;
	
	private final TransactionLockRepository transactionLockRepository;

	private final JavaMailSender javaMailSender;

	@Override
	public OtpValue findByKey(String phoneNumber, OtpType type) {
		return this.otpRepository.getIfPresent(this.createKey(phoneNumber, type));
	}

	@Override
	public void invalidateOtp(String phoneNumber, OtpType type) {
		this.otpRepository.invalidate(this.createKey(phoneNumber, type));
	}

	@Override
	public String sendOtpViaSms(String phoneNumber, OtpType type, boolean validate) {
		return this.sendOtpViaSms(phoneNumber, type, validate, false);
	}
	
	@Override
	public String sendOtpViaSms(String phoneNumber, OtpType type, boolean validate, boolean otpDefault) {
		if (validate) {
			if (Validator.isNull(phoneNumber)) {
				throw new BadRequestException(ErrorCode.MSG1010);
			}
		}

		// Kiểm tra số lần gửi otp
		OtpValue otpValue = this.findByKey(phoneNumber, type);

		if (Validator.isNotNull(otpValue) && this.otpProperties.getOtpAttempt() > 0
				&& otpValue.getCount() >= this.otpProperties.getOtpAttempt()) {
			throw new BadRequestException(ErrorCode.MSG1021);
		}

		// generate transactionId
		String transactionId = UUID.randomUUID().toString();

		if (this.otpProperties.isEnable() && !otpDefault) {
			// Tao tin nhan de gui
			String otp = this.generateOtp(phoneNumber, transactionId, type);

			Map<String, String> valuesMap = new HashMap<>();

			valuesMap.put(TemplateField.OTP_CODE.name(), otp);
			valuesMap.put(TemplateField.TIMESTAMP.name(), String.valueOf(System.currentTimeMillis()));

			SmsResponse smsResponse = this.smsService.send(phoneNumber, TemplateCode.SMS_OTP.name(), valuesMap);

			if (!Validator.equals(smsResponse.getResult(), SmsResponse.Result.SUCCESS.getStatus())) {
				this.invalidateOtp(phoneNumber, type);

				_log.error("Gui tin nhan that bai: {}", smsResponse.getMessage());

				throw new BadRequestException(ErrorCode.MSG1020);
			}
		} else {
			// Setup de test
			this.setOtp(phoneNumber, transactionId, type, this.otpProperties.getDefaultOtp(),
					Validator.isNotNull(otpValue) ? otpValue.getCount() : 0,
					Validator.isNotNull(otpValue) ? otpValue.getOtpConfirmCount() : 0);
		}

		return transactionId;
	}

	@Override
	public void validateOtp(String phoneNumber, String transactionId, OtpType type, String otp) {
		if (Validator.isNull(phoneNumber)) {
			throw new BadRequestException(ErrorCode.MSG1008);
		}

		if (Validator.isNotNull(otp) && Validator.isNull(transactionId)) {
            throw new BadRequestException(ErrorCode.MSG1022);
        }
		
		if (Validator.isNotNull(transactionId)) {
			TransactionLock transactionLock = new TransactionLock(transactionId);
			
			try {
				_log.info("Save transaction {} into transaction lock", transactionId );
				// insert to transaction lock
				this.transactionLockRepository.saveAndFlush(transactionLock);
			} catch (Exception ex) {
				_log.error("validateOtp has error : {}", ex.getMessage());

				if (ex.getMessage().contains(ConstraintViolationException.class.getName())) {
					_log.error("Transaction with id {} and otp {} have been processed", transactionId, otp);

					throw new BadRequestException(ErrorCode.MSG1023);
				}

				throw new BadRequestException(ErrorCode.MSG1010);
			}
		}
		
		OtpValue value = this.findByKey(phoneNumber, type);

        if (value == null || !Validator.equals(value.getOtp(), otp)
                        || !Validator.equals(value.getTransactionId(), transactionId)) {
            _log.error("OTP {} to phone number {} is incorrect or expired", otp, phoneNumber);

            if (Validator.isNotNull(value)) {
                if (this.otpProperties.getOtpConfirmAttemp() > 0
                                && value.getOtpConfirmCount() >= this.otpProperties.getOtpConfirmAttemp()) {
                	_log.error("phoneNumber {} has reached the limit of OTP validation attempts", phoneNumber);
                	
                    throw new BadRequestException(ErrorCode.MSG1024);
                } else {
                    this.setOtpConfirm(phoneNumber, transactionId, type, value.getOtp(), value.getCount(),
                                    value.getOtpConfirmCount());
                    
                    int otpConfirmCount = value.getOtpConfirmCount() + 1;
                    
					if (this.otpProperties.getOtpConfirmAttemp() > 0
							&& otpConfirmCount >= this.otpProperties.getOtpConfirmAttemp()) {
						throw new BadRequestException(ErrorCode.MSG1024);
					}
                    
                    if (Validator.isNotNull(transactionId)) {
						this.transactionLockRepository
								.deleteInBatch(Collections.singleton(new TransactionLock(transactionId)));
                    }
                    
                    throw new BadRequestException(
                            Labels.getLabels(ErrorCode.MSG1025.getKey(),
                                    new Object[] {this.otpProperties.getOtpConfirmAttemp()
                                            - otpConfirmCount}),
                            ErrorCode.MSG1025.name(), ErrorCode.MSG1025.getKey());
                }
            }
            
            throw new BadRequestException(ErrorCode.MSG1026);
        }
        
        // invalidate otp
        this.otpRepository.invalidate(this.createKey(phoneNumber, type));
	}

	private String createKey(String phoneNumber, OtpType type) {
		StringBuilder sb = new StringBuilder(3);

		sb.append(phoneNumber);
		sb.append(StringPool.DASH);
		sb.append(type.name());

		return sb.toString();
	}

    private String generateOtp(String phoneNumber, String transactionId, OtpType type) {
        String key = this.createKey(phoneNumber, type);

        OtpValue value = this.otpRepository.getIfPresent(key);

        String otp = RandomGenerator.randomWithNDigits(this.otpProperties.getNumberOfDigits());

        if (value != null) {
            while (value.getOtp().equals(otp)) {
                otp = RandomGenerator.randomWithNDigits(this.otpProperties.getNumberOfDigits());
            }
        }

        OtpValue newOtp = Validator.isNotNull(value) ? new OtpValue(otp, transactionId, value.getCount())
                        : new OtpValue(otp, transactionId);

		if (_log.isDebugEnabled()) {
			_log.debug("Otp Value: {}", otp);
		}      

        this.otpRepository.put(key, newOtp);

        return otp;
    }

	private void setOtp(String phoneNumber, String transactionId, OtpType type, String otp, int count,
			int confirmCount) {
		OtpValue value = new OtpValue(otp, transactionId, count);

		value.setOtpConfirmCount(confirmCount);
		
		this.otpRepository.put(this.createKey(phoneNumber, type), value);
	}
    
    private void setOtpConfirm(String phoneNumber, String transactionId, OtpType type, String otp, int count,
                    int confirmCount) {
        OtpValue value = new OtpValue(otp, transactionId, count, confirmCount);

        this.otpRepository.put(this.createKey(phoneNumber, type), value);
    }
}
