package com.haui.foxtrip.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * User Profile - Business-specific data
 * 
 * STRATEGY: Keycloak + PostgreSQL (SIMPLE)
 * - Keycloak: Authentication, core identity (email, username, password, roles)
 * - PostgreSQL: Business data (avatar, phone, address, preferences)
 * 
 * WHY PostgreSQL?
 * - Need to join with orders, bookings (SQL queries)
 * - Need audit trail (createdAt, updatedAt)
 * - Avoid calling Keycloak Admin API frequently (slow)
 * 
 * FLOW:
 * - First login: Auto-create from Keycloak JWT
 * - Update: Save to PostgreSQL only
 * - Get: Query PostgreSQL (no cache needed for đồ án)
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profiles", indexes = {
    @Index(name = "idx_user_profile_keycloak", columnList = "keycloak_user_id"),
    @Index(name = "idx_user_profile_phone", columnList = "phone_number"),
    @Index(name = "idx_user_profile_email", columnList = "email")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile extends BaseEntity {
    
    @Column(name = "keycloak_user_id", unique = true, nullable = false)
    UUID keycloakUserId;
    
    // Snapshot from Keycloak (for fast access, avoid Keycloak API calls)
    @Column(nullable = false)
    String email;
    
    @Column(nullable = false)
    String username;
    
    @Column(name = "full_name")
    String fullName;
    
    // Business-specific fields (NOT in Keycloak)
    String avatar; // Cloudinary URL
    
    @Column(name = "phone_number")
    String phoneNumber;
    
    @Column(columnDefinition = "TEXT")
    String address;
    
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    
    String gender; // MALE, FEMALE, OTHER
    
    // Preferences
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> preferences; // {language: "vi", currency: "VND", notifications: {...}}
    
    // Metadata (loyalty points, badges, etc.)
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> metadata; // {loyaltyPoints: 1000, badges: ["verified", "vip"]}
    
    // Status
    @Column(name = "is_active")
    Boolean isActive = true;
    
    @Column(name = "email_verified")
    Boolean emailVerified = false;
    
    @Column(name = "phone_verified")
    Boolean phoneVerified = false;
}
