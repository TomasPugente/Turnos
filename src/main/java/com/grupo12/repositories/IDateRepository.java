package com.grupo12.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo12.entities.Date;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface IDateRepository extends JpaRepository<Date, Integer> {
    Optional<Date> findByDateAndHour(LocalDate date, LocalTime hour);
}
