package com.haui.foxtrip.repository;

import com.haui.foxtrip.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    
    Optional<UserProfile> findByKeycloakUserId(UUID keycloakUserId);
    
    Optional<UserProfile> findByEmail(String email);
    
    Optional<UserProfile> findByUsername(String username);
    
    boolean existsByKeycloakUserId(UUID keycloakUserId);
    
    @Query("SELECT u FROM UserProfile u WHERE u.deletedAt IS NULL " +
           "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<UserProfile> searchActive(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT u FROM UserProfile u WHERE u.deletedAt IS NULL")
    Page<UserProfile> findAllActive(Pageable pageable);
}
