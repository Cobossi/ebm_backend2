package com.expert.beaute.mobile_backend.client;

public class ClientMapper {


    public ClientResponse toDTO(Client client){

        if (client == null) {
            return null;
        }

        ClientResponse dto=new ClientResponse();
        dto.setId_client(client.getId_client());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setEmail(client.getEmail());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setProfilePhoto(client.getProfilePhoto());
        dto.setAccountLocked(client.isAccountLocked());
        dto.setEnabled(client.isEnabled());
        dto.setCreateDate(client.getCreateDate());
        dto.setActivationDeadline(client.getActivationDeadline());
        dto.setLastModifiedDate(client.getLastModifiedDate());

        return  dto;
    }
}
