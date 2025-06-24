package com.grupo12.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.grupo12.entities.Locality;
import com.grupo12.entities.ServiceEntity;
import com.grupo12.repositories.ILocalityRepository;
import com.grupo12.repositories.IServiceRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ILocalityRepository localityRepository;
    private final IServiceRepository serviceRepository;

    public DataInitializer(ILocalityRepository localityRepository, IServiceRepository serviceRepository) {
        this.localityRepository = localityRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void run(String... args) {
        if (localityRepository.count() == 0) {
            localityRepository.save(new Locality("Presidente Peron"));
            localityRepository.save(new Locality("Almirante Brown"));
            localityRepository.save(new Locality("Lomas de Zamora"));
            localityRepository.save(new Locality("Lanus"));
            localityRepository.save(new Locality("San Vicente"));
            
            System.out.println("Localidades insertadas correctamente.");
        }
        
        if (serviceRepository.count() == 0) {
        	serviceRepository.save(new ServiceEntity("Pediatría", "Control de salud y desarrollo infantil.", 60));
        	serviceRepository.save(new ServiceEntity("Dermatología", "Diagnóstico y tratamiento de afecciones cutáneas.", 60));
        	serviceRepository.save(new ServiceEntity("Vacunación", "Aplicación de vacunas según calendario nacional o especial.", 60));
        	serviceRepository.save(new ServiceEntity("Electrocardiograma", "Estudio no invasivo del ritmo cardíaco.", 60));
        	serviceRepository.save(new ServiceEntity("Radiologia", "Utiliza técnicas de imagen para diagnóstico y tratamiento.", 60));
        	serviceRepository.save(new ServiceEntity("Psiquiatría", "Se enfoca en la salud mental y el tratamiento de trastornos mentales.", 60));
        	serviceRepository.save(new ServiceEntity("Otorrinolaringología", "Se enfoca en enfermedades del oído, nariz y garganta.", 60));
        	serviceRepository.save(new ServiceEntity("Oftalmología", "Se dedica al cuidado de la salud visual, incluyendo enfermedades del ojo.", 60));
        	
            System.out.println("Servicio por defecto insertado.");
        }
    }
}
