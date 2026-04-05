package com.portfolio.service;

import com.portfolio.model.dto.PortfolioReportDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.model.entity.Project;
import com.portfolio.model.enums.MemberRole;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Deve gerar relatório com dados corretos")
    void deveGerarRelatorio() {
        Member func = Member.builder().id(1L).nome("João").atribuicao(MemberRole.FUNCIONARIO).build();

        Project p1 = Project.builder()
                .id(1L).nome("Projeto 1").status(ProjectStatus.EM_ANALISE)
                .orcamentoTotal(new BigDecimal("100000"))
                .membros(new ArrayList<>(List.of(func)))
                .build();

        Project p2 = Project.builder()
                .id(2L).nome("Projeto 2").status(ProjectStatus.EM_ANALISE)
                .orcamentoTotal(new BigDecimal("200000"))
                .membros(new ArrayList<>())
                .build();

        Project p3 = Project.builder()
                .id(3L).nome("Projeto 3").status(ProjectStatus.ENCERRADO)
                .orcamentoTotal(new BigDecimal("50000"))
                .dataInicio(LocalDate.of(2026, 1, 1))
                .dataRealTermino(LocalDate.of(2026, 3, 1))
                .membros(new ArrayList<>(List.of(func)))
                .build();

        when(projectRepository.findAllWithMembros()).thenReturn(List.of(p1, p2, p3));

        PortfolioReportDTO relatorio = reportService.gerarRelatorio();

        assertEquals(2, relatorio.getProjetosPorStatus().get("Em análise"));
        assertEquals(1, relatorio.getProjetosPorStatus().get("Encerrado"));
        assertEquals(new BigDecimal("300000"), relatorio.getOrcamentoPorStatus().get("Em análise"));
        assertNotNull(relatorio.getMediaDuracaoEncerradosEmDias());
        assertEquals(1, relatorio.getTotalMembrosUnicosAlocados());
    }

    @Test
    @DisplayName("Deve retornar relatório vazio sem projetos")
    void deveGerarRelatorioVazio() {
        when(projectRepository.findAllWithMembros()).thenReturn(List.of());

        PortfolioReportDTO relatorio = reportService.gerarRelatorio();

        assertTrue(relatorio.getProjetosPorStatus().isEmpty());
        assertTrue(relatorio.getOrcamentoPorStatus().isEmpty());
        assertNull(relatorio.getMediaDuracaoEncerradosEmDias());
        assertEquals(0, relatorio.getTotalMembrosUnicosAlocados());
    }

    @Test
    @DisplayName("Média deve ser nula quando não há projetos encerrados")
    void mediaDeveSerNulaSemEncerrados() {
        Project p1 = Project.builder()
                .id(1L).nome("Projeto 1").status(ProjectStatus.EM_ANALISE)
                .orcamentoTotal(new BigDecimal("100000"))
                .membros(new ArrayList<>())
                .build();

        when(projectRepository.findAllWithMembros()).thenReturn(List.of(p1));

        PortfolioReportDTO relatorio = reportService.gerarRelatorio();

        assertNull(relatorio.getMediaDuracaoEncerradosEmDias());
    }
}
