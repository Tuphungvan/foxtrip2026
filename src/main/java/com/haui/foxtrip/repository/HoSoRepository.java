package com.haui.foxtrip.repository;

import com.haui.foxtrip.entity.HoSo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HoSoRepository extends JpaRepository<HoSo, UUID> {
    
    Optional<HoSo> findByKeycloakUserId(UUID keycloakUserId);
    
    Optional<HoSo> findByEmail(String email);
    
    Optional<HoSo> findByUsername(String username);
    
    boolean existsByKeycloakUserId(UUID keycloakUserId);
    
    @Query("SELECT h FROM HoSo h WHERE h.deletedAt IS NULL " +
           "AND (LOWER(h.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(h.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(h.hoTen) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<HoSo> searchActive(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT h FROM HoSo h WHERE h.deletedAt IS NULL")
    Page<HoSo> findAllActive(Pageable pageable);
}
