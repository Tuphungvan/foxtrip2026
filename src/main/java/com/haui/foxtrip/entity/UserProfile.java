package com.haui.foxtrip.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

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
    
    @Column(nullable = false)
    String email;
    
    @Column(nullable = false)
    String username;
    
    @Column(name = "full_name")
    String fullName;
    
    String avatar;
    
    @Column(name = "phone_number")
    String phoneNumber;
    
    @Column(columnDefinition = "TEXT")
    String address;
    
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    
    String gender;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> preferences;
    
    @Type(JsonBinaryType.class)
    @Column(name = "additional_data", columnDefinition = "jsonb")
    Map<String, Object> additionalData;
    
    @Builder.Default
    @Column(name = "is_active")
    Boolean isActive = true;
    
    @Builder.Default
    @Column(name = "email_verified")
    Boolean emailVerified = false;
    
    @Builder.Default
    @Column(name = "phone_verified")
    Boolean phoneVerified = false;
}
