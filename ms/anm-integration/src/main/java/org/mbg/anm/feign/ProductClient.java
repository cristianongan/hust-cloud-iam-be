package org.mbg.anm.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/product/all")
    List<String> getAllProduct();
}
