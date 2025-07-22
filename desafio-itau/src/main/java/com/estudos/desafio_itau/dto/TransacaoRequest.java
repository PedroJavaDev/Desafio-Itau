package com.estudos.desafio_itau.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRequest {

    @NotNull(message = "O valor não pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "O valor deve ser maior ou igual a zero")
    private BigDecimal valor;

    @NotNull(message = "A dataHora não pode ser nula")
    private OffsetDateTime dataHora;
}
