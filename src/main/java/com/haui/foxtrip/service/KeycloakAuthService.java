package com.haui.foxtrip.service;

import com.haui.foxtrip.dto.LoginRequest;
import com.haui.foxtrip.dto.LoginResponse;
import com.haui.foxtrip.dto.RegisterRequest;
import com.haui.foxtrip.dto.keycloak.KeycloakTokenResponse;
import com.haui.foxtrip.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAuthService {
    
    private final RestTemplate restTemplate;
    
    @Value("${spring.security.oauth2.resourceserver.app.keycloak.server-url}")
    private String keycloakUrl;
    
    @Value("${spring.security.oauth2.resourceserver.app.keycloak.realm}")
    private String realm;
    
    @Value("${spring.security.oauth2.resourceserver.app.keycloak.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.resourceserver.app.keycloak.client-secret:}")
    private String clientSecret;
    
    /**
     * Đăng nhập qua Keycloak
     */
    public LoginResponse login(LoginRequest request) {
        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            body.add("client_secret", clientSecret);
        }
        body.add("username", request.getUsername());
        body.add("password", request.getPassword());
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                KeycloakTokenResponse.class
            );
            
            log.info("User logged in successfully: username={}", request.getUsername());
            
            // Convert Keycloak response to frontend-friendly response
            return convertToLoginResponse(response.getBody());
            
        } catch (HttpClientErrorException e) {
            log.error("Login failed: username={}, error={}", request.getUsername(), e.getMessage());
            throw new BadRequestException("Invalid username or password");
        }
    }
    
    /**
     * Đăng ký user mới qua Keycloak Admin API
     * Sử dụng service account của client để tạo user
     */
    public void register(RegisterRequest request) {
        String serviceAccountToken = getServiceAccountToken();
        String usersUrl = keycloakUrl + "/admin/realms/" + realm + "/users";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(serviceAccountToken);
        
        Map<String, Object> userRepresentation = new HashMap<>();
        userRepresentation.put("username", request.getUsername());
        userRepresentation.put("email", request.getEmail());
        userRepresentation.put("firstName", request.getFirstName());
        userRepresentation.put("lastName", request.getLastName());
        userRepresentation.put("enabled", true);
        userRepresentation.put("emailVerified", false);
        
        // Set password
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", request.getPassword());
        credential.put("temporary", false);
        userRepresentation.put("credentials", List.of(credential));
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userRepresentation, headers);
        
        try {
            restTemplate.exchange(
                usersUrl,
                HttpMethod.POST,
                entity,
                Void.class
            );
            
            log.info("User registered successfully: username={}, email={}", 
                request.getUsername(), request.getEmail());
            
        } catch (HttpClientErrorException e) {
            log.error("Registration failed: username={}, error={}", 
                request.getUsername(), e.getMessage());
            
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new BadRequestException("Username or email already exists");
            }
            throw new BadRequestException("Registration failed: " + e.getMessage());
        }
    }
    
    /**
     * Refresh access token
     */
    public LoginResponse refreshToken(String refreshToken) {
        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            body.add("client_secret", clientSecret);
        }
        body.add("refresh_token", refreshToken);
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                KeycloakTokenResponse.class
            );
            
            // Convert Keycloak response to frontend-friendly response
            return convertToLoginResponse(response.getBody());
            
        } catch (HttpClientErrorException e) {
            log.error("Token refresh failed: error={}", e.getMessage());
            throw new BadRequestException("Invalid refresh token");
        }
    }
    
    /**
     * Đăng xuất (revoke refresh token)
     */
    public void logout(String refreshToken) {
        String logoutUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            body.add("client_secret", clientSecret);
        }
        body.add("refresh_token", refreshToken);
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        
        try {
            restTemplate.exchange(
                logoutUrl,
                HttpMethod.POST,
                entity,
                Void.class
            );
            
            log.info("User logged out successfully");
            
        } catch (HttpClientErrorException e) {
            log.error("Logout failed: error={}", e.getMessage());
        }
    }
    
    /**
     * Lấy service account token để gọi Keycloak Admin API
     * Sử dụng client credentials của chính client này
     */
    private String getServiceAccountToken() {
        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                KeycloakTokenResponse.class
            );
            
            return response.getBody().getAccessToken();
            
        } catch (HttpClientErrorException e) {
            log.error("Failed to get service account token: error={}", e.getMessage());
            throw new BadRequestException("Failed to authenticate with Keycloak");
        }
    }
    
    /**
     * Convert Keycloak token response to frontend-friendly LoginResponse
     */
    private LoginResponse convertToLoginResponse(KeycloakTokenResponse keycloakResponse) {
        return LoginResponse.builder()
                .accessToken(keycloakResponse.getAccessToken())
                .refreshToken(keycloakResponse.getRefreshToken())
                .tokenType(keycloakResponse.getTokenType())
                .expiresIn(keycloakResponse.getExpiresIn())
                .refreshExpiresIn(keycloakResponse.getRefreshExpiresIn())
                .build();
    }
}
