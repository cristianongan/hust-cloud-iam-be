package org.mbg.anm.feign;

import org.mbg.common.base.configuration.FeignConfiguration;
import org.mbg.common.base.model.dto.response.IncreaseInUseRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cms-service", configuration = FeignConfiguration.class)
public interface CmsClient {
    @GetMapping("quota/increase/{userId}")
    IncreaseInUseRes increaseQuota(@PathVariable("userId") Long userId);
}
