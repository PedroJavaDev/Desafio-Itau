package com.estudos.desafio_itau.repository;

import com.estudos.desafio_itau.model.Transacao;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Repository
public class TransacaoRepository {

    private final ConcurrentLinkedQueue<Transacao> transacoes = new ConcurrentLinkedQueue<>();

    public void salvar(Transacao transacao) {
        transacoes.add(transacao);
    }

    public void limparTudo() {
        transacoes.clear();
    }

    public List<Transacao> buscarUltimosSegundos(long segundos) {
        OffsetDateTime limite = OffsetDateTime.now().minusSeconds(segundos);
        return transacoes.stream()
                .filter(t -> t.getDataHora().isAfter(limite))
                .collect(Collectors.toList());
    }
}
