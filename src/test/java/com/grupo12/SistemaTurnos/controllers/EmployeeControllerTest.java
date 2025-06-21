package com.grupo12.controllers;

import com.grupo12.SistemaTurnosApplication;
import com.grupo12.config.SecurityConfig;
import com.grupo12.controllers.EmployeeController;
import com.grupo12.controllers.TurnController;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnMultipleDTO;
import com.grupo12.services.ITurnService;
import com.grupo12.services.implementation.ServiceService;
import com.grupo12.testConfig.TestSecurityConfig;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest(classes = SistemaTurnosApplication.class)
@Import(TestSecurityConfig.class) // si usás una clase personalizada
@AutoConfigureMockMvc(addFilters = false) // <--- evita autenticación real
@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {
	
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITurnService turnService;

    @MockBean
    private ServiceService serviceService;

    @Test
    @WithMockUser(username = "1", roles = {"EMPLOYEE"})
    void callNextTurn_shouldRedirect() throws Exception {
        TurnDTO turno = new TurnDTO();
        turno.setIdTurn(10);
        turno.setStatus("EN_ATENCION");

        when(turnService.callNextTurnForEmployee(1)).thenReturn(turno);

        mockMvc.perform(post("/employee/turns/call-next"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/turns/manage"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"EMPLOYEE"})
    void enableTurn_shouldRedirect() throws Exception {
        TurnDTO dummy = new TurnDTO();
        dummy.setIdTurn(1);
        dummy.setStatus("PENDIENTE");

        when(turnService.enableSingleTurn(any(TurnDTO.class))).thenReturn(dummy);

        mockMvc.perform(post("/employee/turns/enable")
                        .param("idServicio", "1")
                        .param("startTime", LocalDateTime.now().plusDays(1).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/turns"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"EMPLOYEE"})
    void enableMultipleTurns_shouldRedirect() throws Exception {
    	 List<TurnDTO> dummyTurns = List.of(new TurnDTO());

    	    when(turnService.enableMultipleTurns(any(TurnMultipleDTO.class)))
    	            .thenReturn(dummyTurns);

    	    mockMvc.perform(post("/employee/turns/enable-multiple")
    	                    .param("employeeIdPerson", "1")
    	                    .param("serviceId", "1")
    	                    .param("startDate", "2025-07-07T09:00")
    	                    .param("endDate", "2025-07-07T11:00")
    	                    .param("durationMinutes", "30"))
    	            .andExpect(status().is3xxRedirection())
    	            .andExpect(redirectedUrl("/employee/turns"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"EMPLOYEE"})
    void updateTurnStatus_shouldRedirect() throws Exception {
        TurnDTO dummy = new TurnDTO();
        dummy.setIdTurn(2);
        dummy.setStatus("ATENDIDO");

        when(turnService.updateTurnStatus(2, "ATENDIDO")).thenReturn(dummy);

        mockMvc.perform(post("/employee/turns/update-status")
                        .param("turnId", "2")
                        .param("newStatus", "ATENDIDO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employee/turns/manage"));
    }
}
