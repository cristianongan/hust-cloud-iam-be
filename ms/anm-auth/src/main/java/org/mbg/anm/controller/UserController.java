package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.dto.request.UserReq;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("search")
    @PreAuthorize("hasPrivilege('USER_READ')")
    public ResponseEntity<?> search(UserSearch search) {
        return ResponseEntity.ok(this.userService.searchUsers(search));
    }

    @PostMapping("create")
    @PreAuthorize("hasPrivilege('USER_CREATE')")
    public ResponseEntity<?> create(@RequestBody UserReq req) {
        return ResponseEntity.ok(this.userService.create(req));
    }

    @PostMapping("update")
    @PreAuthorize("hasPrivilege('USER_UPDATE')")
    public ResponseEntity<?> update(@RequestBody UserReq req) {
        return ResponseEntity.ok(this.userService.update(req));
    }

    @PostMapping("status")
    @PreAuthorize("hasPrivilege('USER_UPDATE')")
    public ResponseEntity<Void> updateStatus(@RequestBody UserReq req) {
        this.userService.updateStatus(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("delete")
    @PreAuthorize("hasPrivilege('USER_DELETE')")
    public ResponseEntity<Void> delete(@RequestBody UserReq req) {
        this.userService.delete(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("detail")
    public ResponseEntity<?> detail() {
        return ResponseEntity.ok(this.userService.detail());
    }

    @PostMapping("assign-role")
    @PreAuthorize("hasPrivilege('USER_UPDATE')")
    public ResponseEntity<?> assignRole(@RequestBody UserReq req) {
        this.userService.assignRole(req);
        return ResponseEntity.ok().build();
    }
}
