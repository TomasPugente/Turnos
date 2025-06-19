package com.grupo12.models;

import com.grupo12.entities.Date;
import com.grupo12.entities.TurnStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TurnUpdateDTO {
    @NotNull
    private Date date;

    @NotBlank
    private TurnStatus status;

}
