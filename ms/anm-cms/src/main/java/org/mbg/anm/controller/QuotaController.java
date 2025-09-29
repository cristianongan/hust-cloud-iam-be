package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.dto.request.ChangeTotalQuotaReq;
import org.mbg.anm.service.QuotaService;
import org.mbg.common.base.model.dto.request.QuotaBatchReq;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("quota")
@RequiredArgsConstructor
public class QuotaController {

    private final QuotaService quotaService;

    @PostMapping("change")
    @PreAuthorize("hasPrivilege('QUOTA_CHANGE')")
    public ResponseEntity<?> create(@RequestBody ChangeTotalQuotaReq req) {
        this.quotaService.changeTotalQuota(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("increase/{userId}")
    public ResponseEntity<?> increase(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(this.quotaService.increase(userId));
    }

    @PostMapping("batch")
    public ResponseEntity<?> batch(@RequestBody QuotaBatchReq req) {
        return ResponseEntity.ok(this.quotaService.getBatch(req));
    }
}
