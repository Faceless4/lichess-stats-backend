package com.autotests.lichessbackend.controller;

import com.autotests.lichessbackend.service.SecurityDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityDemoController {
    private final SecurityDemoService securityDemoService;

    public SecurityDemoController(SecurityDemoService securityDemoService) {
        this.securityDemoService = securityDemoService;
    }

    @GetMapping("/api/security/user")
    public String userData() {
        return securityDemoService.getUserData();
    }

    @GetMapping("/api/security/admin")
    public String adminSecret() {
        return securityDemoService.getAdminSecret();
    }
}