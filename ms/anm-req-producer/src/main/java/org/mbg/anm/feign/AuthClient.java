package org.mbg.anm.feign;

import org.mbg.common.base.configuration.FeignConfiguration;
import org.mbg.common.base.model.dto.UserDTO;
import org.mbg.common.base.model.dto.request.UserReq;
import org.mbg.common.base.model.dto.response.IncreaseInUseRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service", configuration = FeignConfiguration.class)
public interface AuthClient {
    @PostMapping("user/customer/create")
    UserDTO createCustomerUser(@RequestBody UserReq req);

    @GetMapping("user/detail")
    UserDTO detail();
}
