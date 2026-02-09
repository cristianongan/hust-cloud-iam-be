package com.hust.iam.fiegn;

import com.hust.common.base.configuration.FeignConfiguration;
import com.hust.common.base.model.dto.QuotaDTO;
import com.hust.common.base.model.dto.request.QuotaBatchReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cms-service", configuration = FeignConfiguration.class)
public interface CmsClient {
    @GetMapping("quota/batch")
    List<QuotaDTO> getBatch(QuotaBatchReq req);
}
