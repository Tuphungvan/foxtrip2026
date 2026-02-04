package com.haui.foxtrip.controller;

import com.haui.foxtrip.dto.UpdateProfileRequest;
import com.haui.foxtrip.dto.common.ApiResponse;
import com.haui.foxtrip.entity.UserProfile;
import com.haui.foxtrip.service.UserProfileService;
import com.haui.foxtrip.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    
    private final UserProfileService userProfileService;
    
    /**
     * Lấy profile của user hiện tại kèm roles
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> getMyProfile() {
        Map<String, Object> profileWithRoles = userProfileService.getCurrentUserProfileWithRoles();
        return ApiResponse.success(profileWithRoles);
    }
    
    /**
     * Cập nhật profile của user hiện tại
     */
    @PutMapping
    public ApiResponse<UserProfile> updateMyProfile(@RequestBody UpdateProfileRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        UserProfile updated = userProfileService.updateProfile(userId, request);
        return ApiResponse.success(updated, "Profile updated successfully");
    }
}
