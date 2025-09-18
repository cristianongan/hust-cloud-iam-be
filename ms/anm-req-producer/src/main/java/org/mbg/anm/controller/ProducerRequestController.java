package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.service.RequestService;
import org.mbg.common.base.model.dto.ProducerRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("request")
@RequiredArgsConstructor
public class ProducerRequestController {

    private final RequestService requestService;

    @PostMapping("")
    @PreAuthorize("hasPrivilege('PRODUCER_REQUEST_CREATE')")
    ResponseEntity<?> create(@RequestBody ProducerRequestDTO producerRequestDTO) {
        return ResponseEntity.ok(this.requestService.createRequest(producerRequestDTO));
    }
}
