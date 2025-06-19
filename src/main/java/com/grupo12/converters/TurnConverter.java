package com.grupo12.converters;

import com.grupo12.entities.*;
import com.grupo12.models.*;
import com.grupo12.repositories.IDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.grupo12.services.ILocalityService;

@Component("turnConverter")
public class TurnConverter {

    @Autowired
    @Qualifier("dateRepository")
    private IDateRepository dateRepository;

    @Autowired
    @Qualifier("clientConverter")
    private ClientConverter clientConverter;

    @Autowired
    @Qualifier("employeeConverter")
    private EmployeeConverter employeeConverter;

    public TurnDTO entityToDTO(Turn turn) {
        ClientDTO clientDTO = null;
        if (turn.getClient() != null) {
            clientDTO = clientConverter.entityToDTO(turn.getClient());
        }

        EmployeeDTO employeeDTO = null;
        if (turn.getEmployee() != null) {
            employeeDTO = employeeConverter.entityToDTO((Employee) turn.getEmployee());
        }

        System.out.println(turn.getDate().getDate());
        System.out.println(turn.getDate().getHour());
        return new TurnDTO(
                turn.getIdTurn(),
                turn.getStatus(),
                turn.isActive(),
                turn.getObservation(),
                clientDTO,
                employeeDTO,
                turn.getDate()
        );
    }

    public Turn DTOToEntity(TurnDTO turnDTO) {
        Client client = clientConverter.DTOToEntity(turnDTO.getClient());
        Employee  employee = employeeConverter.DTOToEntity(turnDTO.getEmployee());

        return new Turn(
                turnDTO.getIdTurn(),
                turnDTO.getStatus(),
                turnDTO.isActive(),
                turnDTO.getObservation(),
                client,
                employee,
                turnDTO.getDate()
        );
    }

    public void updateEntityFromDTO(Turn existing, TurnDTO dto) {
        Date date = dto.getDate();
        dateRepository.save(date);

        existing.setDate(date);
        existing.setStatus(dto.getStatus());
        existing.setActive(dto.isActive());
        existing.setObservation(dto.getObservation());
    }

}
