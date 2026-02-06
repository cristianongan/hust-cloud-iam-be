package com.hust.iam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.model.dto.request.ChangeTotalQuotaReq;
import com.hust.iam.service.QuotaService;
import com.hust.iam.service.mapper.QuotaMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.model.Quota;
import org.mbg.common.base.model.dto.QuotaDTO;
import org.mbg.common.base.model.dto.request.QuotaBatchReq;
import org.mbg.common.base.model.dto.response.IncreaseInUseRes;
import org.mbg.common.base.repository.QuotaRepository;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.util.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuotaServiceImpl implements QuotaService {
    private final QuotaRepository quotaRepository;

    private final QuotaMapper quotaMapper;

    @Override
    @Transactional
    public void changeTotalQuota(ChangeTotalQuotaReq req) {
        if (Validator.isNull(req.getId()) || Validator.isNull(req.getQuota())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_QUOTA)})
                    , Quota.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        if (Validator.isNull(req.getUserId())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND,
                    new String[]{Labels.getLabels(LabelKey.LABEL_USER)})
                    , Quota.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
        }

        Quota quota = this.quotaRepository.findByUserId(req.getUserId());

        if (Validator.isNull(quota)) {
            quota = new Quota();
        }

        if (Validator.isNotNull(quota.getInUse())) {
            if (quota.getInUse() < 0) {
                throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_MUST_BE_NON_NEGATIVE,
                        new String[]{Labels.getLabels(LabelKey.LABEL_QUOTA)})
                        , Quota.class.getName(), LabelKey.ERROR_INPUT_MUST_BE_NON_NEGATIVE);
            }

            if (quota.getInUse() >= req.getQuota()) {
                throw new BadRequestException(LabelKey.ERROR_QUOTA_IS_SMALLER_THEN_IN_USE
                        , Quota.class.getName(), LabelKey.ERROR_DATA_COULD_NOT_BE_FOUND);
            }
        }

        quota.setInUse(req.getQuota());

        this.quotaRepository.save(quota);
    }

    @Override
    public IncreaseInUseRes increase(Long userId) {
        Long value = this.quotaRepository.increaseByUserId(userId);

        return IncreaseInUseRes.builder()
                .inUse(Validator.isNotNull(value) ? value : -1L)
                .build();
    }

    @Override
    public List<QuotaDTO> getBatch(QuotaBatchReq req) {
        if (Validator.isNull(req.getUserIds())) {
            return List.of();
        }

        List<Quota> quotas = this.quotaRepository.findByUserIdIn(req.getUserIds());

        return quotaMapper.toDto(quotas);
    }


}
