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
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> lookup(LookupReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.lookup(req)));
    }

    @GetMapping("{clientId}/lookup")
    ResponseEntity<?> lookupWithClientId(LookupReq req, @PathVariable String clientId) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.lookup(req, clientId)));
    }

    @GetMapping("/info")
    ResponseEntity<?> info() {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.info()));
    }

    @PostMapping("{clientId}/verify/send-otp")
    ResponseEntity<?> sendOtp(@RequestBody CustomerDataReq req, @PathVariable String clientId) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.sendOtpToVerify(req, clientId)));
    }

    @PostMapping("{clientId}/verify")
    ResponseEntity<?> verify(@RequestBody CustomerDataReq req, @PathVariable String clientId) {
        this.customerService.verify(req, clientId);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }

    @PostMapping("{clientId}/lookup/add")
    ResponseEntity<?> addDataLookup(@RequestBody SubscribeReq req, @PathVariable String clientId) {
        this.customerService.addDataLookup(req, clientId);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }

    @PostMapping("{clientId}/lookup/remove")
    ResponseEntity<?> removeDataLookup(@RequestBody SubscribeReq req, @PathVariable String clientId) {
        this.customerService.removeDataLookup(req, clientId);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }
}
