
package com.estudos.desafio_itau.service;

import com.estudos.desafio_itau.dto.EstatisticaResponse;
import com.estudos.desafio_itau.dto.TransacaoRequest;
import com.estudos.desafio_itau.exception.TransacaoInvalidaException;
import com.estudos.desafio_itau.model.Transacao;
import com.estudos.desafio_itau.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransacaoServiceTest {

    private TransacaoRepository repository;
    private TransacaoService service;

    @BeforeEach
    void setUp() {
        repository = mock(TransacaoRepository.class);
        service = new TransacaoService(repository);
    }

    @Test
    void deveProcessarTransacaoValida() {
        TransacaoRequest request = new TransacaoRequest(new BigDecimal("100.00"), OffsetDateTime.now().minusSeconds(10));
        assertDoesNotThrow(() -> service.processar(request));
        verify(repository, times(1)).salvar(any(Transacao.class));
    }

    @Test
    void deveLancarErro_QuandoValorNegativo() {
        TransacaoRequest request = new TransacaoRequest(new BigDecimal("-10.00"), OffsetDateTime.now().minusSeconds(10));
        assertThrows(TransacaoInvalidaException.class, () -> service.processar(request));
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarErro_QuandoDataNoFuturo() {
        TransacaoRequest request = new TransacaoRequest(new BigDecimal("10.00"), OffsetDateTime.now().plusSeconds(60));
        assertThrows(TransacaoInvalidaException.class, () -> service.processar(request));
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveRetornarEstatisticasZeradas_QuandoSemTransacoes() {
        when(repository.buscarUltimosSegundos(anyLong())).thenReturn(List.of());
        EstatisticaResponse response = service.calcularEstatisticas();

        assertEquals(0, response.getCount());
        assertEquals(BigDecimal.ZERO, response.getSum());
        assertEquals(BigDecimal.ZERO, response.getAvg());
        assertEquals(BigDecimal.ZERO, response.getMin());
        assertEquals(BigDecimal.ZERO, response.getMax());
    }

    @Test
    void deveRetornarEstatisticasValidas_QuandoExistemTransacoes() {
        List<Transacao> transacoes = List.of(
                new Transacao(new BigDecimal("10.00"), OffsetDateTime.now()),
                new Transacao(new BigDecimal("30.00"), OffsetDateTime.now())
        );

        when(repository.buscarUltimosSegundos(anyLong())).thenReturn(transacoes);
        EstatisticaResponse response = service.calcularEstatisticas();

        assertEquals(2, response.getCount());
        assertEquals(new BigDecimal("40.00"), response.getSum());
        assertEquals(new BigDecimal("20.00"), response.getAvg());
        assertEquals(new BigDecimal("10.00"), response.getMin());
        assertEquals(new BigDecimal("30.00"), response.getMax());
    }
}
