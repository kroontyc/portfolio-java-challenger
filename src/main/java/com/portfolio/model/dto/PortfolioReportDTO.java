package com.portfolio.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Relatório resumido do portfólio de projetos")
public class PortfolioReportDTO {

    @Schema(description = "Quantidade de projetos agrupados por status")
    private Map<String, Long> projetosPorStatus;

    @Schema(description = "Total orçado agrupado por status")
    private Map<String, BigDecimal> orcamentoPorStatus;

    @Schema(description = "Média de duração em dias dos projetos encerrados", example = "45.5")
    private Double mediaDuracaoEncerradosEmDias;

    @Schema(description = "Total de membros únicos alocados em projetos", example = "8")
    private Long totalMembrosUnicosAlocados;
}
