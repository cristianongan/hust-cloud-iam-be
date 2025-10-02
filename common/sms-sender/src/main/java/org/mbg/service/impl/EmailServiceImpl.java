package org.mbg.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.api.response.SmsResponse;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.model.ContentTemplate;
import org.mbg.common.base.repository.ContentTemplateRepository;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.util.StringUtil;
import org.mbg.common.util.Validator;
import org.mbg.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final ContentTemplateRepository contentTemplateRepository;

    @Override
    public void send(String email, String code, Map<String, String> params) {
        ContentTemplate template =
                this.contentTemplateRepository.findByTemplateCodeAndStatus(code,
                        EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(template)) {
            _log.error("Cannot find content template with code {}", code);

            throw new BadRequestException(ErrorCode.MSG1036);
        }

        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
            helper.setTo(email);
            helper.setSubject(template.getTitle());
            helper.setText(StringUtil.replaceMapValue(template.getContent(), params), true);                 // HTML
            javaMailSender.send(msg);
        } catch (Exception e) {
            _log.error("sendOtpViaEmail failed: {}", e.getMessage());
            throw new BadRequestException(ErrorCode.MSG1020);
        }
    }
}
