package com.autotests.lichessbackend.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecurityDemoService {
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminSecret() {
        return "Only ADMIN can see this protected service data";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String getUserData() {
        return "USER or ADMIN can see this service data";
    }
}