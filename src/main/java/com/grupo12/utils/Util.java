package com.grupo12.utils;

import com.grupo12.entities.Contact;
import com.grupo12.entities.Locality;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.LocalityDTO;

public class Util {
    public static ContactDTO contactToDTO(Contact contact) {
        if (contact == null) return null;
        return new ContactDTO(
                contact.getIdContact(),
                contact.getStreet(),
                contact.getNumber(),
                contact.getEmail(),
                contact.getPhone(),
                localityToDTO(contact.getLocality())
        );
    }

    public static Contact contactToEntity(ContactDTO contactDTO) {
        if (contactDTO == null) return null;
        return new Contact(
                contactDTO.getIdContact(),
                contactDTO.getStreet(),
                contactDTO.getNumber(),
                contactDTO.getEmail(),
                contactDTO.getPhone(),
                localityToEntity(contactDTO.getLocality())
        );
    }

    public static LocalityDTO localityToDTO(Locality locality) {
        if (locality == null) return null;
        return new LocalityDTO(locality.getIdLocality(), locality.getName());
    }

    public static Locality localityToEntity(LocalityDTO localityDTO) {
        if (localityDTO == null) return null;
        return new Locality(localityDTO.getIdLocality(), localityDTO.getName());
    }
}
