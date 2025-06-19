package com.grupo12.test;
import com.grupo12.converters.EmployeeConverter;

import com.grupo12.entities.Contact;
import com.grupo12.entities.Employee;
import com.grupo12.entities.Locality;
import com.grupo12.models.ContactDTO;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.models.LocalityDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class DtoTest {
	 public static void main(String[] args) {
	       /* // Simulamos una localidad asociada al contacto
	        LocalityDTO locality = new LocalityDTO(1, "Córdoba");

	        // Creamos un contacto completo con calle, número, mail, etc.
	        ContactDTO contact = new ContactDTO(
	                10,                // idContact
	                "Av. Colón",       // street
	                "1234",            // number
	                "ana@example.com", // email
	                "3511234567",      // phone
	                locality           // LocalityDTO
	        );

	        // Creamos un empleado DTO completo
	        EmployeeDTO employee = new EmployeeDTO(
	                1,                        // idPerson
	                "Ana",                   // name
	                "12345678",              // dni
	                LocalDate.of(1990, 1, 1),// dateOfBirth
	                "password123",           // password
	                contact,                 // ContactDTO
	                LocalDate.of(2020, 3, 1),// entryDate
	                "20-34567890-1"          // cuit
	        );

	        // Mostramos los datos
	        System.out.println("Empleado:");
	        System.out.println("  Nombre: " + employee.getName());
	        System.out.println("  CUIT: " + employee.getCuit());
	        System.out.println("  Email: " + employee.getContact().getEmail());
	        System.out.println("  Ciudad: " + employee.getContact().getLocality().getName());*/
	    }
		 
	    @Test
	    public void testEmployeeDtoConversion() {
	        Contact contact = new Contact(
	                1, 
	                "Calle Falsa", 
	                "123", 
	                "mail@mail.com", 
	                "123456789", 
	                new Locality(1, "Localidad")
	        );

	        Employee employee = new Employee(
	                1, 
	                "Juan", 
	                "12345678", 
	                LocalDate.of(1990, 1, 1), 
	                "password", 
	                contact, 
	                LocalDate.of(2020, 1, 1), 
	                "20-12345678-9"
	        );

	        EmployeeConverter converter = new EmployeeConverter();
	        EmployeeDTO dto = converter.entityToDTO(employee);

	        assertEquals("Juan", dto.getName());
	        assertEquals("12345678", dto.getDni());
	        assertEquals("Calle Falsa", dto.getContact().getStreet());
	        assertEquals("mail@mail.com", dto.getContact().getEmail());
	        assertEquals("Localidad", dto.getContact().getLocality().getName());
	        assertEquals("20-12345678-9", dto.getCuit());
	        
	        System.out.println("Nombre: " + dto.getName());
	        System.out.println("DNI: " + dto.getDni());
	        System.out.println("Calle: " + dto.getContact().getStreet());
	    }
	    
	    @Test
	    public void testLocalityDtoConversion() {
	        Locality locality = new Locality(10, "Córdoba");
	        LocalityDTO dto = new LocalityDTO(locality.getIdLocality(), locality.getName());

	        assertEquals(10, dto.getIdLocality());
	        assertEquals("Córdoba", dto.getName());
	    }

	    @Test
	    public void testContactDtoConversion() {
	        LocalityDTO localityDTO = new LocalityDTO(2, "Mendoza");
	        ContactDTO contactDTO = new ContactDTO(20, "Av. Colón", "456", "ana@mail.com", "3511111111", localityDTO);

	        assertEquals(20, contactDTO.getIdContact());
	        assertEquals("Av. Colón", contactDTO.getStreet());
	        assertEquals("456", contactDTO.getNumber());
	        assertEquals("ana@mail.com", contactDTO.getEmail());
	        assertEquals("3511111111", contactDTO.getPhone());
	        assertEquals("Mendoza", contactDTO.getLocality().getName());
	    }
}
