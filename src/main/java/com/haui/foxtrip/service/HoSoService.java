package com.haui.foxtrip.service;

import com.haui.foxtrip.dto.UpdateProfileRequest;
import com.haui.foxtrip.entity.HoSo;
import com.haui.foxtrip.exception.ResourceNotFoundException;
import com.haui.foxtrip.repository.HoSoRepository;
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
public class HoSoService {
    
    private final HoSoRepository hoSoRepository;
    
    public HoSo findByKeycloakUserId(UUID userId) {
        return hoSoRepository.findByKeycloakUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User profile not found"));
    }
    
    @Transactional
    public HoSo updateProfile(UUID userId, UpdateProfileRequest request) {
        HoSo hoSo = findByKeycloakUserId(userId);
        
        if (request.getHoTen() != null) {
            hoSo.setHoTen(request.getHoTen());
        }
        if (request.getSoDienThoai() != null) {
            hoSo.setSoDienThoai(request.getSoDienThoai());
        }
        if (request.getDiaChi() != null) {
            hoSo.setDiaChi(request.getDiaChi());
        }
        if (request.getNgaySinh() != null) {
            hoSo.setNgaySinh(request.getNgaySinh());
        }
        if (request.getGioiTinh() != null) {
            hoSo.setGioiTinh(request.getGioiTinh());
        }
        
        log.info("Updated profile for userId={}", userId);
        return hoSoRepository.save(hoSo);
    }
    
    @Transactional
    public void softDelete(UUID userId) {
        HoSo hoSo = findByKeycloakUserId(userId);
        hoSo.setDeletedAt(LocalDateTime.now());
        hoSo.setHoatDong(false);
        hoSoRepository.save(hoSo);
        log.info("Soft deleted user profile: userId={}", userId);
    }
    
    @Transactional
    public void restore(UUID userId) {
        HoSo hoSo = findByKeycloakUserId(userId);
        hoSo.setDeletedAt(null);
        hoSo.setHoatDong(true);
        hoSoRepository.save(hoSo);
        log.info("Restored user profile: userId={}", userId);
    }
    
    @Transactional
    public void toggleStatus(UUID userId) {
        HoSo hoSo = findByKeycloakUserId(userId);
        hoSo.setHoatDong(!hoSo.getHoatDong());
        hoSoRepository.save(hoSo);
        log.info("Toggled user status: userId={}, active={}", userId, hoSo.getHoatDong());
    }
    
    public Page<HoSo> findAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        if (search != null && !search.trim().isEmpty()) {
            return hoSoRepository.searchActive(search.trim(), pageable);
        }
        
        return hoSoRepository.findAllActive(pageable);
    }
}
