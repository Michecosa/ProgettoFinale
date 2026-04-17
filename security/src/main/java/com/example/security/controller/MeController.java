package com.example.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/me")
public class MeController {

    @GetMapping("/whoami")
    public Map<String, Object> whoami(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            response.put("authenticated", false);
            return response;
        }

        response.put("authenticated", true);
        response.put("username", auth.getName());
        response.put("roles", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        return response;
    }
}
