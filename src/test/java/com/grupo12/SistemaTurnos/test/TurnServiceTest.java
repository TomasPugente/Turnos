/*package com.grupo12.test;

import com.grupo12.converters.TurnConverter;
import com.grupo12.entities.Employee;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.repositories.IEmployeeRepository;
import com.grupo12.repositories.IServiceRepository;
import com.grupo12.repositories.ITurnRepository;
import com.grupo12.services.implementation.TurnService;
import com.grupo12.models.TurnDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TurnServiceTest {

	@Mock
	private IEmployeeRepository employeeRepository;

	private TurnService turnService;
	private TurnConverter turnConverter;
    private ITurnRepository turnRepository;
	
	@BeforeEach
	void setUp() {
		turnRepository = mock(ITurnRepository.class);
        employeeRepository = mock(IEmployeeRepository.class);
        turnConverter = mock(TurnConverter.class);

        turnService = new TurnService();
        turnService.setTurnRepository(turnRepository);
        turnService.setEmployeeRepository(employeeRepository);
        turnService.setTurnConverter(turnConverter);
	}
	@Test
    public void testCallNextTurnForEmployee_shouldReturnTurnDTO() {
        int employeeId = 1;

        Employee employee = new Employee();
        employee.setIdPerson(employeeId);

        Turn turno = new Turn();
        turno.setIdTurn(10);
        turno.setStatus(TurnStatus.PENDIENTE);
        turno.setStartTime(LocalDateTime.now());
        turno.setEndTime(LocalDateTime.now().plusMinutes(30));
        turno.setEmployee(employee);

        TurnDTO turnoDTO = new TurnDTO();
        turnoDTO.setIdTurn(10);
        turnoDTO.setStatus("EN_ATENCION");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(turnRepository.findNextPendingTurnByEmployeeId(eq(employeeId), any()))
                .thenReturn(List.of(turno));
        when(turnRepository.save(any(Turn.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(turnConverter.toDTO(any(Turn.class))).thenReturn(turnoDTO);

        TurnDTO resultado = turnService.callNextTurnForEmployee(employeeId);

        assertNotNull(resultado);
        assertEquals("EN_ATENCION", resultado.getStatus());
    }
    @Test
    public void testCallNextTurnForEmployee() {
        // Repositorios mockeados
        ITurnRepository turnRepo = mock(ITurnRepository.class);
        IEmployeeRepository empRepo = mock(IEmployeeRepository.class);

        // Servicio a testear
        TurnService turnService = new TurnService();
        turnService.setTurnRepository(turnRepo);
        turnService.setEmployeeRepository(empRepo);

        // TurnConverter con ModelMapper inyectado
        TurnConverter turnConverter = new TurnConverter();
        turnConverter.setModelMapper(new ModelMapper());
        turnService.setTurnConverter(turnConverter);

        // Datos de prueba
        int employeeId = 1;
        Employee emp = new Employee();
        emp.setIdPerson(employeeId);

        Turn turno = new Turn();
        turno.setIdTurn(10);
        turno.setEmployee(emp);
        turno.setStartTime(LocalDateTime.now().plusMinutes(30));
        turno.setEndTime(LocalDateTime.now().plusMinutes(60));
        turno.setStatus(TurnStatus.PENDIENTE);

        // Mocks
        when(empRepo.findById(employeeId)).thenReturn(Optional.of(emp));
        when(turnRepo.findNextPendingTurnByEmployeeId(eq(employeeId), any(PageRequest.class)))
            .thenReturn(List.of(turno));
        when(turnRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        
    

        // Ejecutar
        TurnDTO result = turnService.callNextTurnForEmployee(employeeId);

        // Verificaciones
        assertNotNull(result);
       // assertEquals(TurnStatus.EN_ATENCION, result.getStatus(), "El estado debe ser EN_ATENCION");
        assertEquals(TurnStatus.EN_ATENCION.name(), result.getStatus(), "El estado debe ser EN_ATENCION");
        assertEquals(employeeId, result.getEmployeeIdPerson());
        assertEquals(10, result.getIdTurn());
    }
    @Test
    public void testCallNextTurnForEmployee_shouldThrowException_whenNoTurnAvailable() {
        int employeeId = 2;

        Employee empleado = new Employee();
        empleado.setIdPerson(employeeId);

        // Simula que el empleado existe
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(empleado));

        // Simula que no hay turnos pendientes
        when(turnRepository.findNextPendingTurnByEmployeeId(eq(employeeId), any()))
                .thenReturn(List.of());

        // Ejecutar y verificar que lanza IllegalStateException
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            turnService.callNextTurnForEmployee(employeeId);
        });

        assertEquals("No hay turnos pendientes para el empleado con ID: " + employeeId, exception.getMessage());
    }
    @Test
    public void testEnableSingleTurn() {
    	 // Repositorios mockeados
        ITurnRepository turnRepo = mock(ITurnRepository.class);
        IEmployeeRepository empRepo = mock(IEmployeeRepository.class);
        IServiceRepository serviceRepo = mock(IServiceRepository.class);

        // Servicio a testear
        TurnService turnService = new TurnService();
        turnService.setTurnRepository(turnRepo);
        turnService.setEmployeeRepository(empRepo);
        turnService.setServiceRepository(serviceRepo);

        // TurnConverter con ModelMapper
        TurnConverter converter = new TurnConverter();
        converter.setModelMapper(new ModelMapper());
        turnService.setTurnConverter(converter);

        // Datos de prueba
        int employeeId = 1;
        int serviceId = 1;
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 10, 0);
        LocalDateTime end = start.plusMinutes(30);

        Employee emp = new Employee();
        emp.setIdPerson(employeeId);

        com.grupo12.entities.Service service = new com.grupo12.entities.Service();
        service.setIdServicio(serviceId);
        service.setDurationMinutes(30);

        // Mocks
        when(empRepo.findById(employeeId)).thenReturn(Optional.of(emp));
        when(serviceRepo.findById(serviceId)).thenReturn(Optional.of(service));
        when(turnRepo.findConflictingTurnForEmployee(eq(employeeId), eq(start), eq(end)))
            .thenReturn(List.of());
        when(turnRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // DTO de entrada
        TurnDTO dto = new TurnDTO();
        dto.setEmployeeIdPerson(employeeId);
        dto.setIdServicio(serviceId);
        dto.setStartTime(start);

        // Ejecutar
        TurnDTO result = turnService.enableSingleTurn(dto);

        // Verificaciones
        assertNotNull(result);
        assertEquals(TurnStatus.PENDIENTE.name(), result.getStatus());
        assertEquals(employeeId, result.getEmployeeIdPerson());
        assertEquals(serviceId, result.getIdServicio());
    }
    
    
    //metodos de prueba para CU009
    @Test
    public void testEnableMultipleTurns() {
        // Repos y servicio
        ITurnRepository turnRepo = mock(ITurnRepository.class);
        IEmployeeRepository empRepo = mock(IEmployeeRepository.class);
        IServiceRepository serviceRepo = mock(IServiceRepository.class);

        TurnService turnService = new TurnService();
        turnService.setTurnRepository(turnRepo);
        turnService.setEmployeeRepository(empRepo);
        turnService.setServiceRepository(serviceRepo);

        TurnConverter converter = new TurnConverter();
        converter.setModelMapper(new ModelMapper());
        turnService.setTurnConverter(converter);

        // Datos
        int employeeId = 1;
        int serviceId = 1;
        int duration = 30;
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 20, 11, 0);

        Employee emp = new Employee();
        emp.setIdPerson(employeeId);

        com.grupo12.entities.Service service = new com.grupo12.entities.Service();
        service.setIdServicio(serviceId);
        service.setDurationMinutes(duration);

        when(empRepo.findById(employeeId)).thenReturn(Optional.of(emp));
        when(serviceRepo.findById(serviceId)).thenReturn(Optional.of(service));
        when(turnRepo.findConflictingTurnForEmployee(anyInt(), any(), any()))
            .thenReturn(List.of());  // sin solapamientos
        when(turnRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar
        List<TurnDTO> created = turnService.enableMultipleTurns(employeeId, serviceId, start, end, duration);

        // Verificación
        assertEquals(4, created.size()); // 2 horas / 30 minutos = 4 turnos
        for (TurnDTO dto : created) {
            assertEquals(TurnStatus.PENDIENTE.name(), dto.getStatus());
            assertEquals(employeeId, dto.getEmployeeIdPerson());
            assertEquals(serviceId, dto.getIdServicio());
        }
    }
}*/
