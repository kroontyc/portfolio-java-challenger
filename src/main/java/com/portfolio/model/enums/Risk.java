package com.portfolio.model.enums;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum Risk {

    BAIXO("Baixo risco"),
    MEDIO("Médio risco"),
    ALTO("Alto risco");

    private final String descricao;

    private static final BigDecimal LIMITE_BAIXO = new BigDecimal("100000");
    private static final BigDecimal LIMITE_MEDIO = new BigDecimal("500000");

    Risk(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Risk calcular(BigDecimal orcamento, LocalDate dataInicio, LocalDate previsaoTermino) {
        if (orcamento == null || dataInicio == null || previsaoTermino == null) {
            return BAIXO;
        }

        long meses = ChronoUnit.MONTHS.between(dataInicio, previsaoTermino);

        boolean orcamentoAlto = orcamento.compareTo(LIMITE_MEDIO) > 0;
        boolean prazoAlto = meses > 6;

        if (orcamentoAlto || prazoAlto) {
            return ALTO;
        }

        boolean orcamentoMedio = orcamento.compareTo(LIMITE_BAIXO) > 0;
        boolean prazoMedio = meses > 3;

        if (orcamentoMedio || prazoMedio) {
            return MEDIO;
        }

        return BAIXO;
    }
}
