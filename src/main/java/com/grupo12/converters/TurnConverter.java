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
		  TurnDTO dto = new TurnDTO();

	        dto.setIdTurn(turn.getIdTurn());
	        dto.setActive(turn.isActive());
	        dto.setCreationTime(turn.getCreationTime());
	        dto.setStartTime(turn.getStartTime());
	        dto.setEndTime(turn.getEndTime());
	        dto.setStatus(turn.getStatus().name()); // Enum → String
	        

	        if (turn.getClient() != null) {
	            dto.setClientIdPerson(turn.getClient().getIdPerson());
	            dto.setClientName(turn.getClient().getName());
	            dto.setClientDni(turn.getClient().getDni());
	            dto.setClientDateOfBirth(turn.getClient().getDateOfBirth());
	            dto.setClientPassword(turn.getClient().getUser().getPassword());
	            if (turn.getClient().getContact() != null) {
	                dto.setClientEmail(turn.getClient().getContact().getEmail());
	            }
	        }

	        if (turn.getEmployee() != null) {
	            dto.setEmployeeIdPerson(turn.getEmployee().getIdPerson());
	            dto.setEmployeeName(turn.getEmployee().getName());
	            dto.setEmployeeDni(turn.getEmployee().getDni());
	            dto.setEmployeeDateOfBirth(turn.getEmployee().getDateOfBirth());
	            dto.setEmployeePassword(turn.getEmployee().getUser().getPassword());
	            dto.setEmployeeCuit(turn.getEmployee().getCuit());
	            dto.setEmployeeEntryDate(turn.getEmployee().getEntryDate());
	            if (turn.getEmployee().getContact() != null) {
	                dto.setEmployeeEmail(turn.getEmployee().getContact().getEmail());
	            }
	        }

	        if (turn.getService() != null) {
	            dto.setIdServicio(turn.getService().getIdService());
	            dto.setServiceName(turn.getService().getName());
	        }
	        dto.setReminderSent(turn.isReminderSent());
	        return dto;
	    }
		/*TurnDTO dto=modelMapper.map(turn, TurnDTO.class);
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
			dto.setIdServicio(turn.getService().getIdServicio());
			dto.setServiceName(turn.getService().getName());
		}
		if (turn.getStatus() != null) {
	        dto.setStatus(turn.getStatus().name());
	    }
		//dto.setStatus(turn.getStatus().name()); //convertir enum a String
		return dto;*/
	    
	
	public Turn toEntity(TurnDTO dto) {
	/*	Turn entity=modelMapper.map(dto,Turn.class);
		//La asignacion de Client, Employee, Service por ID se manejara en el servicio
		if(dto.getStatus()!=null) {
			entity.setStatus(TurnStatus.valueOf(dto.getStatus()));//Convertir String a enum
		}
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
		
	
	}
	public void setModelMapper(ModelMapper modelMapper) {
	    this.modelMapper = modelMapper;
	}
}

