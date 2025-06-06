package com.grupo12.converters;

import com.grupo12.entities.Contact;
import com.grupo12.entities.Turn;
import com.grupo12.entities.TurnStatus;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.EmployeeDTO;
//import com.grupo12.models.ServiceDTO;
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
		TurnDTO dto=modelMapper.map(turn, TurnDTO.class);
		//Mapeo de Client, que hereda de Person
		if(turn.getClient()!=null) {
			dto.setClientIdPerson(turn.getClient().getIdPerson());
			dto.setClientName(turn.getClient().getName());
			dto.setClientDni(turn.getClient().getDni());
			dto.setClientDateOfBirth(turn.getClient().getDateOfBirth());
			dto.setClientPassword(turn.getClient().getPassword());
				if(turn.getClient().getContact()!=null) {
					dto.setClientEmail(turn.getClient().getContact().getEmail());
				}
			}
		
		if(turn.getEmployee()!=null) {
			dto.setEmployeeIdPerson(turn.getEmployee().getIdPerson());
			dto.setEmployeeCuit(turn.getEmployee().getCuit());
			dto.setEmployeeDateOfBirth(turn.getEmployee().getDateOfBirth());
			dto.setEmployeeDni(turn.getEmployee().getDni());
			dto.setEmployeeEntryDate(turn.getEmployee().getEntryDate());
			dto.setEmployeeName(turn.getEmployee().getName());
			dto.setEmployeePassword(turn.getEmployee().getPassword());
			if(turn.getEmployee().getContact()!=null) {
				dto.setEmployeeEmail(turn.getEmployee().getContact().getEmail());
			}
			
		}
		if(turn.getService()!=null) {
			dto.setIdService(turn.getService().getIdService());
			dto.setServiceName(turn.getService().getName());
		}
		dto.setStatus(turn.getStatus().name()); //convertir enum a String
		return dto;
	    
	}
	public Turn toEntity(TurnDTO dto) {
		Turn entity=modelMapper.map(dto,Turn.class);
		//La asignacion de Client, Employee, Service por ID se manejara en el servicio
		if(dto.getStatus()!=null) {
			entity.setStatus(TurnStatus.valueOf(dto.getStatus()));//Convertir String a enum
		}
		return entity;
	}
}

