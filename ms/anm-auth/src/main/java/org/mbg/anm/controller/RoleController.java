package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.dto.request.RoleReq;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> create(@RequestBody RoleReq req) {
        return ResponseEntity.ok(this.roleService.createRole(req));
    }

    @PostMapping("update")
    @PreAuthorize("hasPrivilege('ROLE_UPDATE')")
    public ResponseEntity<?> update(@RequestBody RoleReq req) {
        return ResponseEntity.ok(this.roleService.updateRole(req));
    }

    @PostMapping("delete")
    @PreAuthorize("hasPrivilege('ROLE_DELETE')")
    public ResponseEntity<?> delete(@RequestBody RoleReq req) {
        this.roleService.deleteRole(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("permissions")
    @PreAuthorize("hasPrivilege('ROLE_READ')")
    public ResponseEntity<?> getPermissions() {
        return ResponseEntity.ok(this.roleService.getAllPermission());
    }

}
