package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.dto.request.RoleReq;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("search")
    @PreAuthorize("hasPrivilege('ROLE_READ')")
    public ResponseEntity<?> search(RoleReq search) {
        return ResponseEntity.ok(this.roleService.search(search));
    }

    @PostMapping("create")
    @PreAuthorize("hasPrivilege('ROLE_CREATE')")
    public ResponseEntity<?> create(RoleReq req) {
        return ResponseEntity.ok(this.roleService.createRole(req));
    }

    @PostMapping("update")
    @PreAuthorize("hasPrivilege('ROLE_UPDATE')")
    public ResponseEntity<?> update(RoleReq req) {
        return ResponseEntity.ok(this.roleService.updateRole(req));
    }

    @PostMapping("delete")
    @PreAuthorize("hasPrivilege('ROLE_DELETE')")
    public ResponseEntity<?> delete(RoleReq req) {
        this.roleService.deleteRole(req);
        return ResponseEntity.ok().build();
    }
}
