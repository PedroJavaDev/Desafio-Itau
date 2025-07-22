package com.estudos.desafio_itau.controller;

import com.estudos.desafio_itau.dto.EstatisticaResponse;
import com.estudos.desafio_itau.dto.TransacaoRequest;
import com.estudos.desafio_itau.exception.TransacaoInvalidaException;
import com.estudos.desafio_itau.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransacaoController.class)
@Import(TransacaoControllerTest.MockConfig.class)
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransacaoService transacaoService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public TransacaoService transacaoService() {
            return Mockito.mock(TransacaoService.class);
        }
    }

    @Test
    void deveRetornarCreated_QuandoTransacaoValida() throws Exception {
        TransacaoRequest request = new TransacaoRequest(new BigDecimal("50.00"), OffsetDateTime.now().minusSeconds(5));

        mockMvc.perform(post("/api/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornarUnprocessableEntity_QuandoDataFutura() throws Exception {
        TransacaoRequest request = new TransacaoRequest(new BigDecimal("100.00"), OffsetDateTime.now().plusMinutes(1));

        doThrow(new TransacaoInvalidaException("Data futura")).when(transacaoService).processar(any());

        mockMvc.perform(post("/api/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornarUnprocessableEntity_QuandoValorNegativo() throws Exception {
        TransacaoRequest request = new TransacaoRequest(new BigDecimal("-10.00"), OffsetDateTime.now().minusSeconds(10));

        doThrow(new TransacaoInvalidaException("Valor negativo")).when(transacaoService).processar(any());

        mockMvc.perform(post("/api/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornarBadRequest_QuandoJsonInvalido() throws Exception {
        String jsonInvalido = "{ \"valor\": \"abc\", \"dataHora\": \"2024-07-22T10:00:00-03:00\" }";

        mockMvc.perform(post("/api/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequest_QuandoCampoNulo() throws Exception {
        String jsonIncompleto = "{ \"dataHora\": \"2024-07-22T10:00:00-03:00\" }";

        mockMvc.perform(post("/api/transacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonIncompleto))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarOk_QuandoDeletarTransacoes() throws Exception {
        mockMvc.perform(delete("/api/transacao"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarEstatisticasCorretas_QuandoBuscarEstatisticas() throws Exception {
        EstatisticaResponse fakeResponse = new EstatisticaResponse(2,
                new BigDecimal("40.00"),
                new BigDecimal("20.00"),
                new BigDecimal("10.00"),
                new BigDecimal("30.00"));

        when(transacaoService.calcularEstatisticas()).thenReturn(fakeResponse);

        mockMvc.perform(get("/api/estatistica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.sum").value(40.00))
                .andExpect(jsonPath("$.avg").value(20.00))
                .andExpect(jsonPath("$.min").value(10.00))
                .andExpect(jsonPath("$.max").value(30.00));
    }
}
