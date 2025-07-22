package com.estudos.desafio_itau.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {
    private BigDecimal valor;
    private OffsetDateTime dataHora;
}
