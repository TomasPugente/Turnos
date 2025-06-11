/*package com.grupo12.test;

import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.models.TurnDTO;
import com.grupo12.services.implementation.TurnService;
import com.grupo12.converters.TurnConverter;
import com.grupo12.repositories.ITurnRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestLlamarTurno {
	 @Mock
	    private ITurnRepository turnRepository;

	    @InjectMocks
	    private TurnService turnService;

	    @Test
	    public void testLlamarTurno() {
	        // Arrange
	        Turn turno = new Turn();
	        turno.setIdTurn(1);
	        turno.setStatus(TurnStatus.PENDIENTE);

	        when(turnRepository.findById(1)).thenReturn(Optional.of(turno));
	        when(turnRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

	        // Converter necesario
	        TurnConverter converter = new TurnConverter();
	        turnService.setTurnConverter(converter);

	        // Act
	        TurnDTO result = turnService.callNextTurnForEmployee(1);

	        // Assert
	        assertEquals("EN_ATENCION", result.getStatus());
	        verify(turnRepository).save(any());
	    }
}*/
