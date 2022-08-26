package com.mycompany.jwtdemonew.model;

public class JwtResponse {

    private String token;

    public JwtResponse() {
    }

    public JwtResponse(String jwt) {
        this.token = jwt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
