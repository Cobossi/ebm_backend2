package com.expert.beaute.mobile_backend.client.register;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterResponse {

    private String token;
    private String id;
}
