package org.mbg.anm.controller;

import lombok.RequiredArgsConstructor;
import org.mbg.anm.model.dto.request.LoginReq;
import org.mbg.anm.service.AuthService;
import org.mbg.anm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginReq userDTO) {
        return ResponseEntity.ok(this.authService.login(userDTO));
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok(this.authService.refreshToken());
    }

    @GetMapping("verify")
    public ResponseEntity<?> verify() {
        return ResponseEntity.ok(this.authService.verify());
    }




}
