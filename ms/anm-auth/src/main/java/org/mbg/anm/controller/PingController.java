package org.mbg.anm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("public/ping")
    public String ping() {
        return "pong";
    }
}
