package com.grupo12.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo12.models.FAQ;


@Service("FAQService")
public class FAQService {
	
	
    public static List<FAQ> getAll() {
        return List.of(
            new FAQ("¿Cómo saco un turno?", "Debés ir a la sección 'Seleccionar turno' y elegir uno disponible."),
            new FAQ("¿Puedo cancelar un turno?", "Sí, desde la sección 'Mis turnos' podés cancelar cualquier turno activo."),
            new FAQ("¿Necesito crear una cuenta?", "No por ahora. Solo necesitás tu ID de cliente.")
        );
    }
}
