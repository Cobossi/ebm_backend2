package com.expert.beaute.mobile_backend.expert.login;


import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("expert")
@RequiredArgsConstructor
@Tag(name = "login")
public class LoginEController {
    private final LoginEService service;

    @PostMapping("/login")
    public ResponseEntity<LoginEResponse> login(@RequestBody LoginERequest request) {
        LoginEResponse response = service.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }}