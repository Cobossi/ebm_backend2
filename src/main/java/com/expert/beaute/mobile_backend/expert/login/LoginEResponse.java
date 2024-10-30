package com.expert.beaute.mobile_backend.expert.login;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginEResponse {
    private String token;
    private String Id_client;
    private String message;
    private boolean success;

}
