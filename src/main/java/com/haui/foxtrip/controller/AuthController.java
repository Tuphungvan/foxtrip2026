package com.haui.foxtrip.controller;

import com.haui.foxtrip.dto.LoginRequest;
import com.haui.foxtrip.dto.LoginResponse;
import com.haui.foxtrip.dto.RegisterRequest;
import com.haui.foxtrip.dto.common.ApiResponse;
import com.haui.foxtrip.entity.UserProfile;
import com.haui.foxtrip.service.KeycloakAuthService;
import com.haui.foxtrip.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserSyncService userSyncService;
    private final KeycloakAuthService keycloakAuthService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = keycloakAuthService.login(request);
        return ApiResponse.success(response, "Login successful");
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody RegisterRequest request) {
        keycloakAuthService.register(request);
        return ApiResponse.success(null, "Registration successful. Please login.");
    }
    
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody String refreshToken) {
        LoginResponse response = keycloakAuthService.refreshToken(refreshToken);
        return ApiResponse.success(response, "Token refreshed");
    }
    
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody String refreshToken) {
        keycloakAuthService.logout(refreshToken);
        return ApiResponse.success(null, "Logout successful");
    }
    
    @GetMapping("/me")
    public ApiResponse<UserProfile> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UserProfile userProfile = userSyncService.syncUserFromJwt(jwt);
        return ApiResponse.success(userProfile);
    }
    
    @GetMapping("/verify")
    public ApiResponse<Boolean> verifyToken() {
        return ApiResponse.success(true, "Token is valid");
    }
}
