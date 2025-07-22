package com.estudos.desafio_itau.util;

import org.slf4j.Logger;

public class TempoExecucaoLogger {

    public static void medir(Runnable tarefa, Logger logger, String mensagem) {
        long inicio = System.nanoTime();
        tarefa.run();
        long fim = System.nanoTime();
        long duracaoMs = (fim - inicio) / 1_000_000;
        logger.info("{} - Tempo de execução: {}ms", mensagem, duracaoMs);
    }
}
