package com.grupo12.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TurnMultipleDTO {
	private Integer employeeIdPerson;
    private Integer idServicio;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime startHour;
    private LocalTime endHour;
    private int durationMinutes;
}
