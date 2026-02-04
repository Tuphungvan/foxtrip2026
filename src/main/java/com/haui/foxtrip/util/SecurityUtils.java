package com.haui.foxtrip.util;

import com.haui.foxtrip.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class SecurityUtils {
    
    /**
     * Lấy userId từ JWT token
     */
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String subject = jwt.getSubject();
            return UUID.fromString(subject);
        }
        
        throw new UnauthorizedException("User not authenticated");
    }
    
    /**
     * Lấy email từ JWT token
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("email");
        }
        
        throw new UnauthorizedException("User not authenticated");
    }
    
    /**
     * Lấy username từ JWT token
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("preferred_username");
        }
        
        throw new UnauthorizedException("User not authenticated");
    }
    
    /**
     * Lấy danh sách roles từ JWT token
     * Roles có thể nằm ở realm_access hoặc resource_access
     */
    @SuppressWarnings("unchecked")
    public static List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            // Try resource_access first (client-specific roles)
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                // Try foxtrip-backend client
                Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("foxtrip-backend");
                if (clientAccess != null && clientAccess.containsKey("roles")) {
                    return (List<String>) clientAccess.get("roles");
                }
            }
            
            // Fallback to realm_access
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                return (List<String>) realmAccess.get("roles");
            }
        }
        
        return List.of();
    }
    
    /**
     * Kiểm tra user có role cụ thể không
     */
    public static boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role);
    }
    
    /**
     * Kiểm tra user có phải admin không
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN") || hasRole("SUPER_ADMIN");
    }
    
    /**
     * Kiểm tra user có phải Super Admin không
     */
    public static boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }
    
    /**
     * Kiểm tra user có role hoặc cao hơn không
     * Ví dụ: hasRoleOrHigher("ADMIN") → true nếu có ADMIN hoặc SUPER_ADMIN
     */
    public static boolean hasRoleOrHigher(String role) {
        if ("SUPER_ADMIN".equals(role)) {
            return hasRole("SUPER_ADMIN");
        }
        if ("ADMIN".equals(role)) {
            return hasRole("ADMIN") || hasRole("SUPER_ADMIN");
        }
        if ("USER".equals(role)) {
            return hasRole("USER") || hasRole("ADMIN") || hasRole("SUPER_ADMIN");
        }
        return false;
    }
    
    /**
     * Lấy JWT token hiện tại
     */
    public static Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        
        throw new UnauthorizedException("User not authenticated");
    }
}
