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

 
}
