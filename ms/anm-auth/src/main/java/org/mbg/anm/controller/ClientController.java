package org.mbg.anm.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.Client;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.model.dto.request.ClientReq;
import org.mbg.anm.model.dto.request.ClientTokenReq;
import org.mbg.anm.model.dto.request.RoleReq;
import org.mbg.anm.model.dto.request.UserReq;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.service.ClientService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.api.util.HeaderUtil;
import org.mbg.common.label.LabelKey;
import org.mbg.common.util.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("search")
    @PreAuthorize("hasPrivilege('CLIENT_READ')")
    public ResponseEntity<?> search(ClientReq search) {
        return ResponseEntity.ok(this.clientService.search(search));
    }

    @GetMapping("users")
    @PreAuthorize("hasPrivilege('CLIENT_CREATE')")
    public ResponseEntity<?> searchUser(UserSearch search) {
        return ResponseEntity.ok(this.clientService.searchUser(search));
    }

    @PostMapping("create")
    @PreAuthorize("hasPrivilege('CLIENT_CREATE')")
    public ResponseEntity<?> create(@RequestBody ClientDTO req) {
        return ResponseEntity.ok(this.clientService.createClient(req));
    }

    @PostMapping("enable")
    @PreAuthorize("hasPrivilege('CLIENT_UPDATE')")
    public ResponseEntity<Void> enable(@RequestBody ClientReq req) {
        this.clientService.enableClient(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("delete")
    @PreAuthorize("hasPrivilege('CLIENT_DELETE')")
    public ResponseEntity<Void> delete(@RequestBody ClientReq req) {
        this.clientService.deleteClient(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("disable")
    @PreAuthorize("hasPrivilege('CLIENT_UPDATE')")
    public ResponseEntity<Void> disable(@RequestBody ClientReq req) {
        this.clientService.disableClient(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset")
    @PreAuthorize("hasPrivilege('CLIENT_UPDATE')")
    public ResponseEntity<?> reset(@RequestBody ClientReq req) {
        return ResponseEntity.ok(this.clientService.resetSecret(req));
    }

}
