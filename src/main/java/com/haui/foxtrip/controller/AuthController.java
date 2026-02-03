package com.haui.foxtrip.controller;

import com.haui.foxtrip.dto.LoginRequest;
import com.haui.foxtrip.dto.LoginResponse;
import com.haui.foxtrip.dto.RegisterRequest;
import com.haui.foxtrip.dto.common.ApiResponse;
import com.haui.foxtrip.entity.HoSo;
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
    
    /**
     * Đăng nhập
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = keycloakAuthService.login(request);
        return ApiResponse.success(response, "Login successful");
    }
    
    /**
     * Đăng ký
     */
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody RegisterRequest request) {
        keycloakAuthService.register(request);
        return ApiResponse.success(null, "Registration successful. Please login.");
    }
    
    /**
     * Refresh token
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestBody String refreshToken) {
        LoginResponse response = keycloakAuthService.refreshToken(refreshToken);
        return ApiResponse.success(response, "Token refreshed");
    }
    
    /**
     * Đăng xuất
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody String refreshToken) {
        keycloakAuthService.logout(refreshToken);
        return ApiResponse.success(null, "Logout successful");
    }
    
    /**
     * Lấy thông tin user hiện tại
     */
    @GetMapping("/me")
    public ApiResponse<HoSo> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        HoSo hoSo = userSyncService.syncUserFromJwt(jwt);
        return ApiResponse.success(hoSo);
    }
    
    /**
     * Kiểm tra token có hợp lệ không
     */
    @GetMapping("/verify")
    public ApiResponse<Boolean> verifyToken() {
        return ApiResponse.success(true, "Token is valid");
    }
}
