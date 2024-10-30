package com.expert.beaute.mobile_backend.expert.register;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterEResponse {

    private String token;
    private String id;
}
