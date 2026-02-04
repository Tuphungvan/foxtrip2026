package com.haui.foxtrip.util;

import java.util.Arrays;
import java.util.List;

/**
 * Định nghĩa phân cấp roles trong hệ thống
 * SUPER_ADMIN > ADMIN > USER
 */
public class RoleHierarchy {
    
    // Role constants
    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    
    // Role levels (số càng cao càng có quyền cao)
    private static final int SUPER_ADMIN_LEVEL = 100;
    private static final int ADMIN_LEVEL = 50;
    private static final int USER_LEVEL = 10;
    
    /**
     * Kiểm tra user có role cụ thể hoặc cao hơn không
     * Ví dụ: hasRoleOrHigher("ADMIN") → true nếu user có ADMIN hoặc SUPER_ADMIN
     */
    public static boolean hasRoleOrHigher(String requiredRole) {
        List<String> userRoles = SecurityUtils.getCurrentUserRoles();
        int requiredLevel = getRoleLevel(requiredRole);
        
        return userRoles.stream()
            .anyMatch(role -> getRoleLevel(role) >= requiredLevel);
    }
    
    /**
     * Kiểm tra user có đúng role này không (không tính role cao hơn)
     */
    public static boolean hasExactRole(String role) {
        return SecurityUtils.hasRole(role);
    }
    
    /**
     * Kiểm tra user có phải Super Admin không
     */
    public static boolean isSuperAdmin() {
        return SecurityUtils.hasRole(SUPER_ADMIN);
    }
    
    /**
     * Kiểm tra user có phải Admin (không phải Super Admin) không
     */
    public static boolean isAdminOnly() {
        List<String> roles = SecurityUtils.getCurrentUserRoles();
        return roles.contains(ADMIN) && !roles.contains(SUPER_ADMIN);
    }
    
    /**
     * Kiểm tra user có phải User thường (không phải Admin) không
     */
    public static boolean isUserOnly() {
        List<String> roles = SecurityUtils.getCurrentUserRoles();
        return roles.contains(USER) && !roles.contains(ADMIN) && !roles.contains(SUPER_ADMIN);
    }
    
    /**
     * Lấy role cao nhất của user
     */
    public static String getHighestRole() {
        List<String> roles = SecurityUtils.getCurrentUserRoles();
        
        if (roles.contains(SUPER_ADMIN)) {
            return SUPER_ADMIN;
        }
        if (roles.contains(ADMIN)) {
            return ADMIN;
        }
        if (roles.contains(USER)) {
            return USER;
        }
        
        return null;
    }
    
    /**
     * Lấy level của role
     */
    private static int getRoleLevel(String role) {
        return switch (role) {
            case SUPER_ADMIN -> SUPER_ADMIN_LEVEL;
            case ADMIN -> ADMIN_LEVEL;
            case USER -> USER_LEVEL;
            default -> 0;
        };
    }
    
    /**
     * Lấy tất cả roles mà user có quyền truy cập
     * Ví dụ: SUPER_ADMIN có quyền của ADMIN và USER
     */
    public static List<String> getEffectiveRoles() {
        List<String> userRoles = SecurityUtils.getCurrentUserRoles();
        
        if (userRoles.contains(SUPER_ADMIN)) {
            return Arrays.asList(SUPER_ADMIN, ADMIN, USER);
        }
        if (userRoles.contains(ADMIN)) {
            return Arrays.asList(ADMIN, USER);
        }
        if (userRoles.contains(USER)) {
            return List.of(USER);
        }
        
        return List.of();
    }
    
    /**
     * Kiểm tra user có quyền cao hơn target user không
     * Dùng để kiểm tra xem có thể quản lý user khác không
     */
    public static boolean hasHigherRoleThan(List<String> targetRoles) {
        String currentHighestRole = getHighestRole();
        if (currentHighestRole == null) {
            return false;
        }
        
        int currentLevel = getRoleLevel(currentHighestRole);
        int targetLevel = targetRoles.stream()
            .mapToInt(RoleHierarchy::getRoleLevel)
            .max()
            .orElse(0);
        
        return currentLevel > targetLevel;
    }
}
