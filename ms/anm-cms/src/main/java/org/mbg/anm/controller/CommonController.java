package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.dto.request.ChangeTotalQuotaReq;
import org.mbg.anm.model.dto.request.CommonDataReq;
import org.mbg.anm.service.CommonDataService;
import org.mbg.anm.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonDataService commonDataService;

    private final DashboardService dashboardService;

    @GetMapping("/common-data")
    public ResponseEntity<?> getAll( CommonDataReq req) {
        return ResponseEntity.ok(this.commonDataService.getAll(req));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(this.dashboardService.get());
    }
}
