/*package com.grupo12.controllers;
import com.grupo12.controllers.TurnController;
import com.grupo12.models.TurnDTO;
import com.grupo12.services.ITurnService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = TurnController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TurnControllerTest {
		@Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private ITurnService turnService;

	    @Test
	    public void callNextTurn_shouldReturnTurnDTO() throws Exception {
	    	// Arrange: crear TurnDTO simulado
	        TurnDTO mockTurn = new TurnDTO();
	        mockTurn.setIdTurn(1);
	        mockTurn.setStatus("EN_ATENCION");

	        int empleadoId = 5;

	        when(turnService.callNextTurnForEmployee(empleadoId)).thenReturn(mockTurn);

	        // Act & Assert: simular GET /turns/llamar/5
	        mockMvc.perform(get("/turns/llamar/{id}", empleadoId))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.idTurn").value(1))
	                .andExpect(jsonPath("$.status").value("EN_ATENCION"));
	    }
}*/
