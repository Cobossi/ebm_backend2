package com.expert.beaute.mobile_backend.client.login;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String Id_client;
    private String message;
    private boolean success;

}
