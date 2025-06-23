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
	        new FAQ("¿Necesito crear una cuenta?", "No por ahora. Solo necesitás tu ID de cliente."),
	        new FAQ("¿Qué hago si olvidé mi contraseña?", "Usá la opción 'Recuperar contraseña' en la pantalla de login para resetearla."),
	        new FAQ("¿Puedo cambiar el horario de un turno?", "Solo podés cancelar el turno y sacar uno nuevo, no es posible modificarlo directamente."),
	        new FAQ("¿Cómo sé si mi turno fue confirmado?", "Recibirás un correo electrónico con la confirmación y podrás verlo en 'Mis turnos'."),
	        new FAQ("¿Qué debo llevar el día del turno?", "Llevá una identificación válida y, si es necesario, cualquier documentación solicitada para el servicio."),
	        new FAQ("¿Puedo sacar un turno para otra persona?", "Sí, pero necesitás tener los datos personales de esa persona para completar el formulario."),
	        new FAQ("¿Hay algún costo por sacar un turno?", "No, el sistema de turnos es completamente gratuito."),
	        new FAQ("¿Cómo contacto soporte si tengo problemas?", "Podés escribir a soporte@tuempresa.com o llamar al 1234-5678.")
	    );
	}
}
