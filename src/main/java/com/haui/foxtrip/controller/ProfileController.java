package com.haui.foxtrip.controller;

import com.haui.foxtrip.dto.UpdateProfileRequest;
import com.haui.foxtrip.dto.common.ApiResponse;
import com.haui.foxtrip.entity.UserProfile;
import com.haui.foxtrip.service.UserProfileService;
import com.haui.foxtrip.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    
    private final UserProfileService userProfileService;
    @GetMapping
    public ApiResponse<UserProfile> getMyProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        UserProfile profile = userProfileService.findByKeycloakUserId(userId);
        return ApiResponse.success(profile);
    }
    @PutMapping
    public ApiResponse<UserProfile> updateMyProfile(@RequestBody UpdateProfileRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        UserProfile updated = userProfileService.updateProfile(userId, request);
        return ApiResponse.success(updated, "Profile updated successfully");
    }
}
