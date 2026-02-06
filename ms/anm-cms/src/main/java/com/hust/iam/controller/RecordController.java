package com.hust.iam.controller;

import lombok.RequiredArgsConstructor;
import com.hust.iam.service.RecordService;
import org.mbg.common.base.model.dto.request.RecordReq;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("search")
    @PreAuthorize("hasPrivilege('RECORD_READ')")
    public ResponseEntity<?> create(RecordReq req) {
        return ResponseEntity.ok(this.recordService.search(req));
    }
}
