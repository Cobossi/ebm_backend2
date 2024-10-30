package com.expert.beaute.mobile_backend.client.geolocation;


import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.geolocation.GeolocationExpert;
import com.expert.beaute.mobile_backend.expert.geolocation.GeolocationExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeolocationClientService {

    private static final double EARTH_RADIUS = 6371; // km

    @Autowired
    private GeolocationClientRepository geolocationClientRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private GeolocationExpertRepository geolocationExpertRepository;

    public void updateClientLocation(String clientId, double latitude, double longitude, String country, String city, String district) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException(clientId));

        GeolocationClient geolocation = geolocationClientRepository.findByClientId(clientId)
                .orElse(new GeolocationClient());

        geolocation.setClientId(clientId);
        geolocation.setLatitude(latitude);
        geolocation.setLongitude(longitude);
        geolocation.setPays(country);
        geolocation.setVille(city);
        geolocation.setQuartier(district);
        geolocation.setLastLocationUpdate(LocalDateTime.now());

        geolocationClientRepository.save(geolocation);
    }



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


    public List<Expert> findNearbyExperts(String clientId, double radiusInKm) {
        GeolocationClient clientLocation = geolocationClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException(clientId));

        double lat = clientLocation.getLatitude();
        double lon = clientLocation.getLongitude();

        // Calculer les deltas approximatifs pour la latitude et la longitude
        double latDelta = radiusInKm / EARTH_RADIUS * (180 / Math.PI);
        double lonDelta = radiusInKm / (EARTH_RADIUS * Math.cos(Math.toRadians(lat))) * (180 / Math.PI);

        List<GeolocationExpert> potentialNearbyExperts = geolocationExpertRepository.findExpertsInRadius(lat, lon, latDelta, lonDelta);

        return potentialNearbyExperts.stream()
                .filter(expertLocation -> calculateDistance(lat, lon, expertLocation.getLatitude(), expertLocation.getLongitude()) <= radiusInKm)
                .map(expertLocation -> expertRepository.findById(expertLocation.getExpertId()).orElse(null))
                .filter(expert -> expert != null)
                .collect(Collectors.toList());
    }

}
