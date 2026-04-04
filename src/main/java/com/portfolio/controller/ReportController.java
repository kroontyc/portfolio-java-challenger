package com.portfolio.controller;

import com.portfolio.model.dto.PortfolioReportDTO;
import com.portfolio.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Relatórios", description = "Relatórios do portfólio")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/portfolio")
    @Operation(summary = "Gerar relatório resumido do portfólio")
    public ResponseEntity<PortfolioReportDTO> gerarRelatorio() {
        return ResponseEntity.ok(reportService.gerarRelatorio());
    }
}
