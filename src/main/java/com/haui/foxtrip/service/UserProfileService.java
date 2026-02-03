package com.haui.foxtrip.service;

import com.haui.foxtrip.dto.UpdateProfileRequest;
import com.haui.foxtrip.entity.UserProfile;
import com.haui.foxtrip.exception.ResourceNotFoundException;
import com.haui.foxtrip.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
}
