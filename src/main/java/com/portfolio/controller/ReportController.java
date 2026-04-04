package com.portfolio.controller;

import com.portfolio.model.dto.PortfolioReportDTO;
import com.portfolio.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Relatórios", description = "Relatórios e métricas do portfólio de projetos")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/portfolio")
    @Operation(summary = "Relatório resumido do portfólio", description = "Retorna quantidade de projetos por status, total orçado por status, média de duração dos projetos encerrados e total de membros únicos alocados")
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    public ResponseEntity<PortfolioReportDTO> gerarRelatorio() {
        return ResponseEntity.ok(reportService.gerarRelatorio());
    }
}
