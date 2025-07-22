package com.estudos.desafio_itau.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "estatistica.tempo")
@Getter
@Setter
public class EstatisticaConfig {
    private long segundos = 60;
}
