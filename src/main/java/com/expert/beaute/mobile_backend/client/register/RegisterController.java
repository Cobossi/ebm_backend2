package com.expert.beaute.mobile_backend.client.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@CrossOrigin
@RestController
@RequestMapping("client")
@RequiredArgsConstructor
@Tag(name = "register")
public class RegisterController {

    private final RegisterService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            service.register(request);
            return ResponseEntity.accepted()
                    .body(new MessageResponse("Un code d'activation vous a été envoyé"));
        } catch (EmailAlreadyExistsException | PhoneNumberAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse(e.getMessage()));
        } catch (RuntimeException  e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Erreur lors de l'envoi du code d'activation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Erreur lors de l'inscription"));
        }
    }

    @Data
    @AllArgsConstructor
    static class MessageResponse {
        private String message;
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handlePhoneNumberAlreadyExists(PhoneNumberAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

}
