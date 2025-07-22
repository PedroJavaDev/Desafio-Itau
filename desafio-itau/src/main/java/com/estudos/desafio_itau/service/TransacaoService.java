package com.estudos.desafio_itau.service;

import com.estudos.desafio_itau.dto.TransacaoRequest;
import com.estudos.desafio_itau.dto.EstatisticaResponse;
import com.estudos.desafio_itau.exception.TransacaoInvalidaException;
import com.estudos.desafio_itau.model.Transacao;
import com.estudos.desafio_itau.repository.TransacaoRepository;
import com.estudos.desafio_itau.util.TempoExecucaoLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
public class TransacaoService {

    private static final Logger logger = LoggerFactory.getLogger(TransacaoService.class);

    private final TransacaoRepository repository;

    @Value("${estatistica.tempo.segundos:60}")
    private long segundos;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    public void processar(TransacaoRequest request) {
        validar(request);
        Transacao transacao = new Transacao(request.getValor(), request.getDataHora());
        repository.salvar(transacao);
    }

    public void limpar() {
        repository.limparTudo();
    }

    public EstatisticaResponse calcularEstatisticas() {
        final EstatisticaResponse[] resposta = new EstatisticaResponse[1];

        TempoExecucaoLogger.medir(() -> {
            List<Transacao> transacoes = repository.buscarUltimosSegundos(segundos);

            if (transacoes.isEmpty()) {
                resposta[0] = new EstatisticaResponse(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                return;
            }

            DoubleSummaryStatistics stats = transacoes.stream()
                    .map(Transacao::getValor)
                    .mapToDouble(BigDecimal::doubleValue)
                    .summaryStatistics();

            BigDecimal sum = BigDecimal.valueOf(stats.getSum()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal avg = BigDecimal.valueOf(stats.getAverage()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal min = BigDecimal.valueOf(stats.getMin()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal max = BigDecimal.valueOf(stats.getMax()).setScale(2, RoundingMode.HALF_UP);
            long count = stats.getCount();

            resposta[0] = new EstatisticaResponse(count, sum, avg, min, max);
        }, logger, "Cálculo de estatísticas");

        return resposta[0];
    }

    private void validar(TransacaoRequest request) {
        if (request.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new TransacaoInvalidaException("Valor deve ser maior ou igual a zero");
        }

        if (request.getDataHora().isAfter(OffsetDateTime.now())) {
            throw new TransacaoInvalidaException("DataHora não pode estar no futuro");
        }
    }
}
