package com.expert.beaute.mobile_backend.client.update;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientResponse;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("client")
public class UpdateClientController {

    @Autowired
    private  UpdateClientService updateClientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getExpert(@PathVariable String id) {
        ClientResponse client = updateClientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PatchMapping("/{clientId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id,
                                            @RequestParam String oldpassword,
                                            @RequestParam String newpassword) {
        updateClientService.updatePassword(
                id,
                oldpassword,
                newpassword
        );
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{clientId}/nom")
    public ResponseEntity<Client> updateFirstName(@PathVariable String clientId, @RequestParam String nom) {
        Client updatedClient = updateClientService.updateFirstName(clientId, nom);
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{clientId}/prenom")
    public ResponseEntity<Client> updateLastName(@PathVariable String clientId, @RequestParam String prenom) {
        Client updatedClient = updateClientService.updateLastName(clientId, prenom);
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{clientId}/phone-number")
    public ResponseEntity<Client> updatePhoneNumber(@PathVariable String clientId, @RequestBody String newPhoneNumber) {
        Client updatedClient = updateClientService.updatePhoneNumber(clientId, newPhoneNumber);
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{clientId}/email")
    public ResponseEntity<Client> updateEmail(@PathVariable String clientId, @RequestBody String newEmail) {
        Client updatedClient = updateClientService.updateEmail(clientId, newEmail);
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{id}/profile-photo")
    public ResponseEntity<?> updateProfilePhoto(@PathVariable String id, @RequestParam("photo") MultipartFile photo) throws IOException {
        updateClientService.updateProfilePhoto(id, photo);
        return ResponseEntity.ok().build();
    }


}
