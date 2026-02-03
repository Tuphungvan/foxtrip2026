package com.haui.foxtrip.service;

import com.haui.foxtrip.entity.UserProfile;
import com.haui.foxtrip.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSyncService {
    
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public UserProfile syncUserFromJwt(Jwt jwt) {
        UUID keycloakUserId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaim("email");
        String username = jwt.getClaim("preferred_username");
        Boolean emailVerified = jwt.getClaim("email_verified");
        
        log.info("Syncing user from Keycloak: userId={}, email={}", keycloakUserId, email);
        
        // Find or create new
        UserProfile userProfile = userProfileRepository.findByKeycloakUserId(keycloakUserId)
            .orElseGet(() -> {
                log.info("Creating new user profile for userId={}", keycloakUserId);
                return UserProfile.builder()
                    .keycloakUserId(keycloakUserId)
                    .isActive(true)
                    .emailVerified(false)
                    .phoneVerified(false)
                    .build();
            });
        
        // Update info from Keycloak
        userProfile.setEmail(email);
        userProfile.setUsername(username);
        userProfile.setEmailVerified(emailVerified != null && emailVerified);
        
        // If no full name, use username
        if (userProfile.getFullName() == null || userProfile.getFullName().isEmpty()) {
            userProfile.setFullName(username);
        }
        
        return userProfileRepository.save(userProfile);
    }
}
