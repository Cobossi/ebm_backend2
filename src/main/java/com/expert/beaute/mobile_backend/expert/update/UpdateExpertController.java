package com.expert.beaute.mobile_backend.expert.update;


import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("expert")
public class UpdateExpertController {
    private final UpdateExpertService expertService;

    @Autowired
    public UpdateExpertController(UpdateExpertService expertService) {
        this.expertService = expertService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpertResponse> getExpert(@PathVariable String id) {
        ExpertResponse expert = expertService.getExpertById(id);
        return ResponseEntity.ok(expert);
    }
    @GetMapping("/all/{expertId}")
    public ResponseEntity<List<ExpertResponse>> getAllExperts(@PathVariable String expertId) {
       List <ExpertResponse> expert = expertService.getAllExpertsExceptConnected(expertId);
        return ResponseEntity.ok(expert);
    }


    @PatchMapping("/{id}/nom")
    public ResponseEntity<?> updateNom(@PathVariable String id, @RequestParam String nom) {
          expertService.updateNom(id, nom);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id,
                                            @RequestParam String oldpassword,
                                            @RequestParam String newpassword) {
        expertService.updatePassword(
                id,
                oldpassword,
                newpassword
        );
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}/expertname")
    public ResponseEntity<ExpertResponse> updateExpertName(@PathVariable String id, @RequestParam String expertname) {
        ExpertResponse updatedExpert = expertService.updateExpertname(id, expertname);
        return ResponseEntity.ok(updatedExpert);
    }



    @PatchMapping("/{id}/prenom")
    public ResponseEntity<?> updatePrenom(@PathVariable String id, @RequestParam String prenom) {
        expertService.updatePrenom(id, prenom);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Expert> updateEmail(@PathVariable String id, @RequestParam String email) {
        Expert updatedExpert = expertService.updateEmail(id, email);
        return ResponseEntity.ok(updatedExpert);
    }

    @PatchMapping("/{id}/phone-number")
    public ResponseEntity<?> updatePhoneNumber(@PathVariable String id, @RequestParam String phoneNumber) {
        expertService.updatePhoneNumber(id, phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/actus")
    public ResponseEntity<?> updateActus(@PathVariable String id, @RequestParam String actus) {
       expertService.updateActus(id, actus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/profile-photo")
    public ResponseEntity<?> updateProfilePhoto(@PathVariable String id, @RequestParam("photo") MultipartFile photo) throws IOException {
      expertService.updateProfilePhoto(id, photo);
        return ResponseEntity.ok().build();
    }

}
