package com.grupo12.converters;

import com.grupo12.entities.Contact;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.EmployeeDTO;
import com.grupo12.models.ServiceDTO;
import com.grupo12.models.ContactDTO;


import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TurnConverter {
	@Autowired
	private ModelMapper modelMapper;
	
	public TurnDTO toDTO(Turn turn) {
	    TurnDTO dto = new TurnDTO();

	    dto.setIdTurn(turn.getIdTurn());
	    dto.setActive(turn.isActive());
	    dto.setCreationTime(turn.getCreatedAt());
	    dto.setStartTime(turn.getStartTime());
	    dto.setEndTime(turn.getEndTime());
	    if (turn.getStatus() != null) {
	        dto.setStatus(turn.getStatus().name());
	    }

	    if (turn.getClient() != null) {
	        dto.setClientIdPerson(turn.getClient().getIdPerson());
	        dto.setClientName(turn.getClient().getName());
	        dto.setClientDni(turn.getClient().getDni());
	        dto.setClientDateOfBirth(turn.getClient().getDateOfBirth());

	        if (turn.getClient().getUser() != null) {
	            dto.setClientPassword(turn.getClient().getUser().getPassword());
	            dto.setClientEmail(turn.getClient().getUser().getEmail());
	        } else {
	            dto.setClientPassword(null);
	            dto.setClientEmail(null);
	        }
	    }

	    if (turn.getEmployee() != null) {
	        dto.setEmployeeIdPerson(turn.getEmployee().getIdPerson());
	        dto.setEmployeeName(turn.getEmployee().getName());
	        dto.setEmployeeDni(turn.getEmployee().getDni());
	        dto.setEmployeeDateOfBirth(turn.getEmployee().getDateOfBirth());
	        dto.setEmployeeCuit(turn.getEmployee().getCuit());
	        dto.setEmployeeEntryDate(turn.getEmployee().getEntryDate());

	        if (turn.getEmployee().getUser() != null) {
	            dto.setEmployeePassword(turn.getEmployee().getUser().getPassword());
	            dto.setEmployeeEmail(turn.getEmployee().getUser().getEmail());
	        } else {
	            dto.setEmployeePassword(null);
	            dto.setEmployeeEmail(null);
	        }
	    }

	    if (turn.getService() != null) {
	        dto.setServiceId(turn.getService().getIdService());
	        dto.setServiceName(turn.getService().getName());
	    }

	    dto.setReminderSent(turn.isReminderSent());

	    return dto;
	}


	
	public Turn toEntity(TurnDTO dto) {
	/*	Turn entity=modelMapper.map(dto,Turn.class);
		//La asignacion de Client, Employee, Service por ID se manejara en el servicio
		if(dto.getStatus()!=null) {
			entity.setStatus(TurnStatus.valueOf(dto.getStatus()));//Convertir String a enum
		}
<<<<<<< HEAD
		return entity;*/
//		 return modelMapper.map(dto, Turn.class);
		Turn entity = modelMapper.map(dto, Turn.class);

	    // Asegurarse de que reminderSent no sea null
	    if (dto.getReminderSent() != null) {
	        entity.setReminderSent((Boolean) dto.getReminderSent());
	    } else {
	        entity.setReminderSent(false); // o true, según tu lógica
	    }

	    return entity;
		 //return modelMapper.map(dto, Turn.class);

	}
	
	public void setModelMapper(ModelMapper modelMapper) {
	    this.modelMapper = modelMapper;
	}
}

