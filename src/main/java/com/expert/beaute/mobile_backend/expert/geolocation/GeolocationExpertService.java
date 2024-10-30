package com.expert.beaute.mobile_backend.expert.geolocation;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.heremaps.ExpertDistance;
import com.expert.beaute.mobile_backend.heremaps.HereMapsAddress;
import com.expert.beaute.mobile_backend.heremaps.HereMapsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeolocationExpertService {

    @Autowired
    private GeolocationExpertRepository geolocationExpertRepository;

    @Autowired
    private  ExpertRepository expertRepository;


   // @Value("${here.maps.api.key}")
    private String hereMapsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // Récupérer les coordonnées d'un expert
    public GeolocationExpert getExpertLocation(String expertId) {
        return geolocationExpertRepository.findByExpertId(expertId)
                .orElseThrow(() -> new RuntimeException("Localisation non trouvée pour l'expert: " + expertId));
    }

    // Trouver les experts les plus proches
    public List<ExpertDistance> findNearbyExperts(String expertId, double maxDistance) {
        GeolocationExpert sourceExpert = getExpertLocation(expertId);
        List<GeolocationExpert> allExperts = geolocationExpertRepository.findAll();

        List<ExpertDistance> nearbyExperts = allExperts.stream()
                .filter(exp -> !exp.getExpertId().equals(expertId))
                .map(exp -> {
                    double distance = calculateDistance(
                            sourceExpert.getLatitude(), sourceExpert.getLongitude(),
                            exp.getLatitude(), exp.getLongitude()
                    );
                    return new ExpertDistance(exp, distance);
                })
                .filter(ed -> ed.getDistance() <= maxDistance)
                .sorted(Comparator.comparingDouble(ExpertDistance::getDistance))
                .toList();

        return nearbyExperts;
    }


    // Mettre à jour la localisation avec géocodage inverse
    public void updateExpertLocation(String expertId, double latitude, double longitude) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert non trouvé: " + expertId));

        // Appel à l'API HERE Maps pour le géocodage inverse
        String url = String.format(
                "https://revgeocode.search.hereapi.com/v1/revgeocode" +
                        "?at=%f,%f" +
                        "&apiKey=%s" +
                        "&lang=fr",
                latitude, longitude, hereMapsApiKey
        );
        HereMapsResponse response = restTemplate.getForObject(url, HereMapsResponse.class);

        if (response != null && !response.getItems().isEmpty()) {
            HereMapsAddress address = response.getItems().get(0).getAddress();

            GeolocationExpert geolocation = geolocationExpertRepository.findByExpertId(expertId)
                    .orElse(new GeolocationExpert());

            geolocation.setExpertId(expertId);
            geolocation.setExpert(expert);
            geolocation.setLatitude(latitude);
            geolocation.setLongitude(longitude);
            geolocation.setPays(address.getCountryName());
            geolocation.setVille(address.getCity());
            geolocation.setQuartier(address.getDistrict());
            geolocation.setLastLocationUpdate(LocalDateTime.now());

            geolocationExpertRepository.save(geolocation);
        }}

        private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

}
