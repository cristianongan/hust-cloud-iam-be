package com.hust.iam.controller;

import lombok.RequiredArgsConstructor;
import com.hust.iam.model.dto.request.CommonDataReq;
import com.hust.iam.service.CommonDataService;
import com.hust.iam.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
