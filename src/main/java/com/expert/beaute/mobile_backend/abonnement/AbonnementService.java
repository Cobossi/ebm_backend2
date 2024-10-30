package com.expert.beaute.mobile_backend.abonnement;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbonnementService {

    @Autowired
    private AbonnementRepository abonnementRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ExpertRepository expertRepository;

    @Transactional
    public void abonner(String clientId, String expertId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert non trouvé"));

        if (abonnementRepository.findByClientIdAndExpertId(clientId, expertId).isPresent()) {
            throw new RuntimeException("Abonnement déjà existant");
        }

        Abonnement abonnement = new Abonnement();
        abonnement.setClient(client);
        abonnement.setExpert(expert);
        abonnement.setDateAbonnement(LocalDateTime.now());

        abonnementRepository.save(abonnement);
    }

    @Transactional
    public void desabonner(String clientId, String expertId) {
        Abonnement abonnement = abonnementRepository.findByClientIdAndExpertId(clientId, expertId)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));

        abonnementRepository.delete(abonnement);
    }

    public long getNombreAbonnes(String expertId) {
        // Vérifier si le vendeur existe
        if (!expertRepository.existsById(expertId)) {
            throw new RuntimeException("Vendeur non trouvé");
        }
        return abonnementRepository.countAbonnesByExpertId(expertId);
    }

    public List<AbonnementResponse> getListeAbonnes(String expertId) {
        if (!expertRepository.existsById(expertId)) {
            throw new RuntimeException("Expert non trouvé");
        }
        List<Client> abonnes = abonnementRepository.findAbonnesByExpertId(expertId);
        return abonnes.stream()
                .map(client -> AbonnementResponse.builder()
                        .Id_client(client.getId_client())
                        .Id_expert(expertId)
                        .build())
                .collect(Collectors.toList());
    }

    public List<Expert> getListeExpertSuivis(String clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client non trouvé");
        }
        return abonnementRepository.findExpertsSuivisByClientId(clientId);
    }
}
