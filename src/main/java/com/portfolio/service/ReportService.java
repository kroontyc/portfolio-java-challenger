package com.portfolio.service;

import com.portfolio.model.dto.PortfolioReportDTO;
import com.portfolio.model.entity.Project;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ProjectRepository projectRepository;

    public ReportService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public PortfolioReportDTO gerarRelatorio() {
        List<Project> todos = projectRepository.findAllWithMembros();

        Map<String, Long> projetosPorStatus = todos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getStatus().getDescricao(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        Map<String, BigDecimal> orcamentoPorStatus = todos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getStatus().getDescricao(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, p -> p.getOrcamentoTotal() != null ? p.getOrcamentoTotal() : BigDecimal.ZERO, BigDecimal::add)
                ));

        List<Project> encerrados = todos.stream()
                .filter(p -> p.getStatus() == ProjectStatus.ENCERRADO)
                .filter(p -> p.getDataInicio() != null && p.getDataRealTermino() != null)
                .toList();

        Double mediaDuracao = null;
        if (!encerrados.isEmpty()) {
            mediaDuracao = encerrados.stream()
                    .mapToLong(p -> ChronoUnit.DAYS.between(p.getDataInicio(), p.getDataRealTermino()))
                    .average()
                    .orElse(0);
        }

        long membrosUnicos = todos.stream()
                .flatMap(p -> p.getMembros().stream())
                .map(m -> m.getId())
                .distinct()
                .count();

        return PortfolioReportDTO.builder()
                .projetosPorStatus(projetosPorStatus)
                .orcamentoPorStatus(orcamentoPorStatus)
                .mediaDuracaoEncerradosEmDias(mediaDuracao)
                .totalMembrosUnicosAlocados(membrosUnicos)
                .build();
    }
}
