package com.portfolio.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RiskTest {

    @Test
    @DisplayName("Deve retornar BAIXO para orçamento até 100k e prazo até 3 meses")
    void deveRetornarBaixoRisco() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 3, 1);

        assertEquals(Risk.BAIXO, Risk.calcular(new BigDecimal("80000"), inicio, fim));
        assertEquals(Risk.BAIXO, Risk.calcular(new BigDecimal("100000"), inicio, fim));
    }

    @Test
    @DisplayName("Deve retornar MEDIO para orçamento entre 100k e 500k")
    void deveRetornarMedioRiscoPorOrcamento() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 3, 1);

        assertEquals(Risk.MEDIO, Risk.calcular(new BigDecimal("100001"), inicio, fim));
        assertEquals(Risk.MEDIO, Risk.calcular(new BigDecimal("300000"), inicio, fim));
        assertEquals(Risk.MEDIO, Risk.calcular(new BigDecimal("500000"), inicio, fim));
    }
}
