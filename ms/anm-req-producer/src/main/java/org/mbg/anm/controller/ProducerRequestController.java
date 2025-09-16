package org.mbg.anm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerRequestController {
    @GetMapping("ping")
    @PreAuthorize("hasPrivilege('TEST')")
    ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
