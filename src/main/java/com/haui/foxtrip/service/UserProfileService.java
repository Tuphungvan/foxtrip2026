package com.haui.foxtrip.service;

import com.haui.foxtrip.dto.UpdateProfileRequest;
import com.haui.foxtrip.entity.UserProfile;
import com.haui.foxtrip.exception.ResourceNotFoundException;
import com.haui.foxtrip.repository.UserProfileRepository;
import com.haui.foxtrip.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;
    
    public UserProfile findByKeycloakUserId(UUID userId) {
        return userProfileRepository.findByKeycloakUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }
    
    /**
     * Lấy profile của user hiện tại kèm theo roles từ JWT
     * Tự động tạo profile nếu chưa tồn tại (cho user tạo thủ công trong Keycloak)
     */
    public Map<String, Object> getCurrentUserProfileWithRoles() {
        UUID userId = SecurityUtils.getCurrentUserId();
        
        // Tìm hoặc tạo mới profile nếu chưa tồn tại
        UserProfile profile = userProfileRepository.findByKeycloakUserId(userId)
            .orElseGet(() -> {
                log.info("User not found in database, creating new profile: userId={}", userId);
                
                // Lấy thông tin từ JWT để tạo profile mới
                String email = SecurityUtils.getCurrentUserEmail();
                String username = SecurityUtils.getCurrentUsername();
                
                UserProfile newProfile = UserProfile.builder()
                    .keycloakUserId(userId)
                    .email(email)
                    .username(username)
                    .fullName(username) // Mặc định dùng username làm fullName
                    .isActive(true)
                    .emailVerified(false)
                    .phoneVerified(false)
                    .build();
                
                return userProfileRepository.save(newProfile);
            });
        
        // Lấy roles từ JWT token
        List<String> roles = SecurityUtils.getCurrentUserRoles();
        
        // Tạo response map với profile và roles
        Map<String, Object> response = new HashMap<>();
        response.put("id", profile.getId());
        response.put("keycloakUserId", profile.getKeycloakUserId());
        response.put("email", profile.getEmail());
        response.put("username", profile.getUsername());
        response.put("fullName", profile.getFullName());
        response.put("avatar", profile.getAvatar());
        response.put("phoneNumber", profile.getPhoneNumber());
        response.put("address", profile.getAddress());
        response.put("dateOfBirth", profile.getDateOfBirth());
        response.put("gender", profile.getGender());
        response.put("preferences", profile.getPreferences());
        response.put("additionalData", profile.getAdditionalData());
        response.put("isActive", profile.getIsActive());
        response.put("emailVerified", profile.getEmailVerified());
        response.put("phoneVerified", profile.getPhoneVerified());
        response.put("roles", roles);
        response.put("isAdmin", SecurityUtils.isAdmin());
        response.put("isSuperAdmin", SecurityUtils.isSuperAdmin());
        response.put("highestRole", getHighestRole(roles));
        
        return response;
    }
    
    @Transactional
    public UserProfile updateProfile(UUID userId, UpdateProfileRequest request) {
        UserProfile userProfile = findByKeycloakUserId(userId);
        
        if (request.getFullName() != null) {
            userProfile.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            userProfile.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            userProfile.setAddress(request.getAddress());
        }
        if (request.getDateOfBirth() != null) {
            userProfile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            userProfile.setGender(request.getGender());
        }
        
        log.info("Updated profile for userId={}", userId);
        return userProfileRepository.save(userProfile);
    }
    
    @Transactional
    public void softDelete(UUID userId) {
        UserProfile userProfile = findByKeycloakUserId(userId);
        userProfile.setDeletedAt(LocalDateTime.now());
        userProfile.setIsActive(false);
        userProfileRepository.save(userProfile);
        log.info("Soft deleted user profile: userId={}", userId);
    }
    
    @Transactional
    public void restore(UUID userId) {
        UserProfile userProfile = findByKeycloakUserId(userId);
        userProfile.setDeletedAt(null);
        userProfile.setIsActive(true);
        userProfileRepository.save(userProfile);
        log.info("Restored user profile: userId={}", userId);
    }
    
    @Transactional
    public void toggleStatus(UUID userId) {
        UserProfile userProfile = findByKeycloakUserId(userId);
        userProfile.setIsActive(!userProfile.getIsActive());
        userProfileRepository.save(userProfile);
        log.info("Toggled user status: userId={}, active={}", userId, userProfile.getIsActive());
    }
    
    public Page<UserProfile> findAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        if (search != null && !search.trim().isEmpty()) {
            return userProfileRepository.searchActive(search.trim(), pageable);
        }
        
        return userProfileRepository.findAllActive(pageable);
    }
    
    /**
     * Lấy role cao nhất từ danh sách roles
     */
    private String getHighestRole(List<String> roles) {
        if (roles.contains("SUPER_ADMIN")) {
            return "SUPER_ADMIN";
        }
        if (roles.contains("ADMIN")) {
            return "ADMIN";
        }
        if (roles.contains("USER")) {
            return "USER";
        }
        return null;
    }
}
