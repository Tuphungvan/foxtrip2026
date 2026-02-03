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
     */
    @SuppressWarnings("unchecked")
    public static List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
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
