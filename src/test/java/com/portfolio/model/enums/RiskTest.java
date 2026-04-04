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

    @Test
    @DisplayName("Deve retornar MEDIO para prazo entre 3 e 6 meses")
    void deveRetornarMedioRiscoPorPrazo() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 5, 1);

        assertEquals(Risk.MEDIO, Risk.calcular(new BigDecimal("50000"), inicio, fim));
    }

    @Test
    @DisplayName("Deve retornar ALTO para orçamento acima de 500k")
    void deveRetornarAltoRiscoPorOrcamento() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 3, 1);

        assertEquals(Risk.ALTO, Risk.calcular(new BigDecimal("500001"), inicio, fim));
        assertEquals(Risk.ALTO, Risk.calcular(new BigDecimal("1000000"), inicio, fim));
    }

    @Test
    @DisplayName("Deve retornar ALTO para prazo acima de 6 meses")
    void deveRetornarAltoRiscoPorPrazo() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 8, 1);

        assertEquals(Risk.ALTO, Risk.calcular(new BigDecimal("50000"), inicio, fim));
    }

    @Test
    @DisplayName("Deve retornar BAIXO quando valores são nulos")
    void deveRetornarBaixoQuandoNulo() {
        assertEquals(Risk.BAIXO, Risk.calcular(null, null, null));
        assertEquals(Risk.BAIXO, Risk.calcular(new BigDecimal("50000"), null, null));
        assertEquals(Risk.BAIXO, Risk.calcular(null, LocalDate.now(), null));
    }
}
