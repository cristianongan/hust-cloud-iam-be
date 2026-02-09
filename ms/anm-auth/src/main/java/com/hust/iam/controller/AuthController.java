package com.hust.iam.controller;

import com.hust.common.base.model.dto.request.UserReq;
import lombok.RequiredArgsConstructor;
import com.hust.iam.model.dto.request.LoginReq;
import com.hust.iam.service.AuthService;
import com.hust.iam.service.UserService;
import com.hust.common.base.model.dto.request.OtpReq;
import com.hust.common.base.model.dto.request.ResetPasswordReq;
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

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserReq req) {
        return ResponseEntity.ok(this.userService.register(req));
    }

    @GetMapping("customer/token/{userId}")
    public ResponseEntity<?> customerToken(@PathVariable Long userId) {
        return ResponseEntity.ok(this.authService.customerToken(userId));
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok(this.authService.refreshToken());
    }

    @GetMapping("verify")
    public ResponseEntity<?> verify() {
        return ResponseEntity.ok(this.authService.verify());
    }

    @PostMapping("reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordReq req) {
        return ResponseEntity.ok(this.authService.resetPassword(req));
    }

    @PostMapping("reset/verify")
    public ResponseEntity<?> verifyResetPassword(@RequestBody OtpReq req) {
        this.authService.verify(req);
        return ResponseEntity.ok().build();
    }

}
