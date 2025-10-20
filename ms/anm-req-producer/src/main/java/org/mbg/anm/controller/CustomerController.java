package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.dto.request.CustomerDataReq;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.service.CustomerService;
import org.mbg.common.api.response.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("subscribe")
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> subscribe(@RequestBody SubscribeReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.subscribe(req)));
    }

    @PostMapping("unsubscribe")
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> unsubscribe(@RequestBody SubscribeReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.unSubscribe(req)));
    }

    @GetMapping("/lookup")
    ResponseEntity<?> lookup(LookupReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.lookup(req)));
    }

    @GetMapping("/info")
    ResponseEntity<?> info(SubscribeReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.info(req)));
    }

    @PostMapping("/verify/send-otp")
    ResponseEntity<?> sendOtp(@RequestBody CustomerDataReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.sendOtpToVerify(req)));
    }

    @PostMapping("/verify")
    ResponseEntity<?> verify(@RequestBody CustomerDataReq req) {
        this.customerService.verify(req);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }

    @PostMapping("/lookup/add")
    ResponseEntity<?> addDataLookup(@RequestBody SubscribeReq req) {
        this.customerService.addDataLookup(req);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }

    @PostMapping("/lookup/remove")
    ResponseEntity<?> removeDataLookup(@RequestBody SubscribeReq req) {
        this.customerService.removeDataLookup(req);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }
}
