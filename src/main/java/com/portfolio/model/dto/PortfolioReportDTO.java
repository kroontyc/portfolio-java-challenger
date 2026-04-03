package com.portfolio.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioReportDTO {

    private Map<String, Long> projetosPorStatus;
    private Map<String, BigDecimal> orcamentoPorStatus;
    private Double mediaDuracaoEncerradosEmDias;
    private Long totalMembrosUnicosAlocados;
}
