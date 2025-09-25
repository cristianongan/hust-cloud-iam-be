package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.service.CustomerService;
import org.mbg.common.base.model.dto.ProducerRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("subscribe")
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> subscribe(@RequestBody SubscribeReq req) {
        return ResponseEntity.ok(this.customerService.subscribe(req));
    }

    @PostMapping("unsubscribe")
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> unsubscribe(@RequestBody SubscribeReq req) {
        return ResponseEntity.ok(this.customerService.unSubscribe(req));
    }
}
