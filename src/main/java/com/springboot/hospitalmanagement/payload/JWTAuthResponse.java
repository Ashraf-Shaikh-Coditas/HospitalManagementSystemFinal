package com.springboot.hospitalmanagement.payload;

public class JWTAuthResponse {
    private String accessToken;
    private String role;

//    private String tokenName = "Bearer";

    private Long id;

    public JWTAuthResponse(String accessToken, String role, Long id) {
        this.accessToken = accessToken;
        this.role = role;
        this.id = id;
    }

    public JWTAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

//    public String getTokenType() {
//        return tokenType;
//    }
//
//    public void setTokenType(String tokenType) {
//        this.tokenType = tokenType;
//    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
