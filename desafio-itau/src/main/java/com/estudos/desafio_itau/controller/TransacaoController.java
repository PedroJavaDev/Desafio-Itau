package com.estudos.desafio_itau.controller;

import com.estudos.desafio_itau.dto.TransacaoRequest;
import com.estudos.desafio_itau.dto.EstatisticaResponse;
import com.estudos.desafio_itau.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transacao")
    public ResponseEntity<Void> criarTransacao(@RequestBody @Valid TransacaoRequest request) {
        transacaoService.processar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/transacao")
    public ResponseEntity<Void> deletarTransacoes() {
        transacaoService.limpar();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatistica")
    public ResponseEntity<EstatisticaResponse> obterEstatisticas() {
        EstatisticaResponse estatisticas = transacaoService.calcularEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
}
