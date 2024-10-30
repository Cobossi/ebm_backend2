package com.expert.beaute.mobile_backend.abonnement;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("abonnements")
public class AbonnementController {

    @Autowired
    private AbonnementService abonnementService;

    @PostMapping("/abonner")
    public ResponseEntity<?> abonner(@RequestParam String clientId, @RequestParam String expertId) {
        abonnementService.abonner(clientId, expertId);
        return ResponseEntity.ok("Abonnement réussi");
    }

    @PostMapping("/desabonner")
    public ResponseEntity<?> desabonner(@RequestParam String clientId, @RequestParam String expertId) {
        abonnementService.desabonner(clientId, expertId);
        return ResponseEntity.ok("Désabonnement réussi");
    }

    @GetMapping("/expert/{expertId}/nombre-abonnes")
    public ResponseEntity<Long> getNombreAbonnes(@PathVariable String expertId) {
        long nombreAbonnes = abonnementService.getNombreAbonnes(expertId);
        return ResponseEntity.ok(nombreAbonnes);
    }

    @GetMapping("/expert/{expertId}/abonnes")
    public ResponseEntity<List<AbonnementResponse>> getListeAbonnes(@PathVariable String expertId) {
        List<AbonnementResponse> abonnes = abonnementService.getListeAbonnes(expertId);
        return ResponseEntity.ok(abonnes);
    }

    @GetMapping("/client/{clientId}/expert-suivis")
    public ResponseEntity<List<Expert>> getListeExpertSuivis(@PathVariable String clientId) {
        List<Expert> expertsSuivis = abonnementService.getListeExpertSuivis(clientId);
        return ResponseEntity.ok(expertsSuivis);
    }

}
