package com.grupo12.configuration.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Evita que falle por diferencias leves en nombres
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Si querés, podés desactivar errores de campos no mapeados:
        // modelMapper.getConfiguration().setSkipNullEnabled(true);

        return modelMapper;
    }
    
    
}
