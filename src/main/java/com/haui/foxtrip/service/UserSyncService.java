package com.haui.foxtrip.service;

import com.haui.foxtrip.entity.HoSo;
import com.haui.foxtrip.repository.HoSoRepository;
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
    
    private final HoSoRepository hoSoRepository;

    @Transactional
    public HoSo syncUserFromJwt(Jwt jwt) {
        UUID keycloakUserId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaim("email");
        String username = jwt.getClaim("preferred_username");
        Boolean emailVerified = jwt.getClaim("email_verified");
        
        log.info("Syncing user from Keycloak: userId={}, email={}", keycloakUserId, email);
        
        // Tìm hoặc tạo mới
        HoSo hoSo = hoSoRepository.findByKeycloakUserId(keycloakUserId)
            .orElseGet(() -> {
                log.info("Creating new user profile for userId={}", keycloakUserId);
                return HoSo.builder()
                    .keycloakUserId(keycloakUserId)
                    .hoatDong(true)
                    .emailXacThuc(false)
                    .sdtXacThuc(false)
                    .build();
            });
        
        // Cập nhật thông tin từ Keycloak
        hoSo.setEmail(email);
        hoSo.setUsername(username);
        hoSo.setEmailXacThuc(emailVerified != null && emailVerified);
        
        // Nếu chưa có họ tên, dùng username
        if (hoSo.getHoTen() == null || hoSo.getHoTen().isEmpty()) {
            hoSo.setHoTen(username);
        }
        
        return hoSoRepository.save(hoSo);
    }
}
