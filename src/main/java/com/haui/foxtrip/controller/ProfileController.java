package com.haui.foxtrip.controller;

import com.haui.foxtrip.dto.UpdateProfileRequest;
import com.haui.foxtrip.dto.common.ApiResponse;
import com.haui.foxtrip.entity.HoSo;
import com.haui.foxtrip.service.HoSoService;
import com.haui.foxtrip.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    
    private final HoSoService hoSoService;
    @GetMapping
    public ApiResponse<HoSo> getMyProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        HoSo profile = hoSoService.findByKeycloakUserId(userId);
        return ApiResponse.success(profile);
    }
    @PutMapping
    public ApiResponse<HoSo> updateMyProfile(@RequestBody UpdateProfileRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        HoSo updated = hoSoService.updateProfile(userId, request);
        return ApiResponse.success(updated, "Profile updated successfully");
    }
}
