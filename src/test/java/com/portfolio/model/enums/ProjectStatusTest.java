package com.portfolio.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectStatusTest {

    @Test
    @DisplayName("Deve permitir transição na sequência correta")
    void devePermitirTransicaoCorreta() {
        assertTrue(ProjectStatus.EM_ANALISE.podeTransitarPara(ProjectStatus.ANALISE_REALIZADA));
        assertTrue(ProjectStatus.ANALISE_REALIZADA.podeTransitarPara(ProjectStatus.ANALISE_APROVADA));
        assertTrue(ProjectStatus.ANALISE_APROVADA.podeTransitarPara(ProjectStatus.INICIADO));
        assertTrue(ProjectStatus.INICIADO.podeTransitarPara(ProjectStatus.PLANEJADO));
        assertTrue(ProjectStatus.PLANEJADO.podeTransitarPara(ProjectStatus.EM_ANDAMENTO));
        assertTrue(ProjectStatus.EM_ANDAMENTO.podeTransitarPara(ProjectStatus.ENCERRADO));
    }

    @Test
    @DisplayName("Deve bloquear pulo de etapas")
    void deveBloquearPuloDeEtapas() {
        assertFalse(ProjectStatus.EM_ANALISE.podeTransitarPara(ProjectStatus.INICIADO));
        assertFalse(ProjectStatus.EM_ANALISE.podeTransitarPara(ProjectStatus.EM_ANDAMENTO));
        assertFalse(ProjectStatus.ANALISE_REALIZADA.podeTransitarPara(ProjectStatus.INICIADO));
        assertFalse(ProjectStatus.ANALISE_APROVADA.podeTransitarPara(ProjectStatus.EM_ANDAMENTO));
        assertFalse(ProjectStatus.PLANEJADO.podeTransitarPara(ProjectStatus.ENCERRADO));
    }

    @Test
    @DisplayName("Deve permitir cancelar a qualquer momento")
    void devePermitirCancelarSempre() {
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status != ProjectStatus.ENCERRADO && status != ProjectStatus.CANCELADO) {
                assertTrue(status.podeTransitarPara(ProjectStatus.CANCELADO),
                        "Deveria permitir cancelar a partir de " + status);
            }
        }
    }

    @Test
    @DisplayName("Não deve permitir transição a partir de ENCERRADO exceto CANCELADO")
    void naoDeveTransitarDeEncerrado() {
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status == ProjectStatus.CANCELADO) {
                assertTrue(ProjectStatus.ENCERRADO.podeTransitarPara(status));
            } else {
                assertFalse(ProjectStatus.ENCERRADO.podeTransitarPara(status));
            }
        }
    }

    @Test
    @DisplayName("Não deve permitir transição a partir de CANCELADO exceto CANCELADO")
    void naoDeveTransitarDeCancelado() {
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status == ProjectStatus.CANCELADO) {
                assertTrue(ProjectStatus.CANCELADO.podeTransitarPara(status));
            } else {
                assertFalse(ProjectStatus.CANCELADO.podeTransitarPara(status));
            }
        }
    }

    @Test
    @DisplayName("Deve retornar descrição correta")
    void deveRetornarDescricao() {
        assertEquals("Em análise", ProjectStatus.EM_ANALISE.getDescricao());
        assertEquals("Encerrado", ProjectStatus.ENCERRADO.getDescricao());
    }

}
