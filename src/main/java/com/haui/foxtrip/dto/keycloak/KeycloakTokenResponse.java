package com.haui.foxtrip.dto.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for deserializing token response from Keycloak
 * Internal use only - not exposed to frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakTokenResponse {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("token_type")
    private String tokenType;
    
    @JsonProperty("expires_in")
    private Integer expiresIn;
    
    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;
    
    @JsonProperty("scope")
    private String scope;
}
