package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.dto.request.CustomerDataReq;
import org.mbg.anm.model.dto.request.SubscribeBatchReq;
import org.mbg.anm.service.AuthService;
import org.mbg.common.base.annotation.ReqEncryptAes256;
import org.mbg.common.base.annotation.ResEncryptAes256;
import org.mbg.common.base.annotation.Signature;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.service.CustomerService;
import org.mbg.common.api.response.ClientResponse;
import org.mbg.common.security.util.SecurityConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final AuthService authService;

    @ReqEncryptAes256
    @PostMapping("/{org}/token")
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> subscribeBatch(@RequestBody SubscribeReq req, @PathVariable String org) {
        return ResponseEntity.ok(ClientResponse.ok(this.authService.verifyCustomer(req, org)));
    }

    @ReqEncryptAes256
    @PostMapping("subscribe-batch")
    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
    ResponseEntity<?> subscribeBatch(@RequestBody SubscribeBatchReq req,
                                     @RequestHeader(value = SecurityConstants.Header.ORG, required = false) String org) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.subscribeBatch(req, org)));
    }

//    @PostMapping("unsubscribe")
//    @PreAuthorize("hasPrivilege('CLIENT_DEFAULT')")
//    ResponseEntity<?> unsubscribe(@RequestBody SubscribeReq req) {
//        return ResponseEntity.ok(ClientResponse.ok(this.customerService.unSubscribe(req)));
//    }

    @ReqEncryptAes256
    @ResEncryptAes256
    @PostMapping("/lookup")
    ResponseEntity<?> lookup(LookupReq req) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.lookup(req)));
    }

//    @GetMapping("{clientId}/lookup")
//    ResponseEntity<?> lookupWithClientId(LookupReq req, @PathVariable String clientId) {
//        return ResponseEntity.ok(ClientResponse.ok(this.customerService.lookup(req, clientId)));
//    }

    @ResEncryptAes256
    @PostMapping("/{org}/info")
    ResponseEntity<?> info() {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.info()));
    }

    @PostMapping("{org}/verify/send-otp")
    ResponseEntity<?> sendOtp(@RequestBody CustomerDataReq req, @PathVariable String org) {
        return ResponseEntity.ok(ClientResponse.ok(this.customerService.sendOtpToVerify(req, org)));
    }

    @PostMapping("{org}/verify")
    ResponseEntity<?> verify(@RequestBody CustomerDataReq req, @PathVariable String org) {
        this.customerService.verify(req, org);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }

    @PostMapping("{org}/lookup/add")
    ResponseEntity<?> addDataLookup(@RequestBody SubscribeReq req, @PathVariable String org) {
        this.customerService.addDataLookup(req, org);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }

    @PostMapping("{org}/lookup/remove")
    ResponseEntity<?> removeDataLookup(@RequestBody SubscribeReq req, @PathVariable String org) {
        this.customerService.removeDataLookup(req, org);
        return ResponseEntity.ok(ClientResponse.ok(null));
    }
}
