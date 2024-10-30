package com.expert.beaute.mobile_backend.prestation;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PrestationService {

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private PrestationRepository serviceRepository;



    @Transactional
    public void addServiceToExpert(String expertId, Long serviceId, BigDecimal price) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        Prestation service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Si le prix fourni est null, utilisez le prix de base du service
        BigDecimal finalPrice = (price != null) ? price : service.getPrixBase();

        expert.addService(service, finalPrice);
        expertRepository.save(expert);
    }
    @Transactional
    public void updatePrice(String expertId, Long serviceId, BigDecimal newPrice) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        Prestation service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Mettre à jour le prix du service pour cet expert
        expert.getServicePrices().put(service, newPrice);

        expertRepository.save(expert);
    }
    @Transactional
    public void removeServiceFromExpert(String expertId, Long serviceId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        Prestation services = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        expert.removeService(services);
        expertRepository.save(expert);
    }

    public List<Prestation> getExpertServices(String expertId) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        return new ArrayList<>(expert.getPrestation());
    }

    public List<PrestationRequest> getAllServices() {
        List<Prestation> services = serviceRepository.findAll();
        return services.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Prestation getPrestationById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation non trouvée avec l'ID : " + id));
    }

    public List<Prestation> getPrestationsById(List<Long> ids) {
        List<Prestation> prestations = serviceRepository.findAllById(ids);
        if (prestations.size() != ids.size()) {
            throw new RuntimeException("Une ou plusieurs prestations n'ont pas été trouvées");
        }
        return prestations;
    }

    private PrestationRequest convertToDTO(Prestation service) {
        return new PrestationRequest(
                service.getId(),
                service.getNom(),
                service.getDescription(),
                service.getPrixBase()
        );
    }
    public Map<Prestation, BigDecimal> getExpertServicePrices(String expertId) {
        // Récupérer les prix associés à chaque service pour un expert donné
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new NoSuchElementException("Expert not found"));
        return expert.getServicePrices(); // Suppose que servicePrices est une propriété de l'entité Expert
    }
}
