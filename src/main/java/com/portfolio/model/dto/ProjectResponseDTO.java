package com.portfolio.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de retorno de um projeto")
public class ProjectResponseDTO {

    @Schema(description = "ID do projeto", example = "1")
    private Long id;

    @Schema(description = "Nome do projeto", example = "Sistema de Vendas")
    private String nome;

    private LocalDate dataInicio;
    private LocalDate previsaoTermino;
    private LocalDate dataRealTermino;

    @Schema(description = "Orçamento total", example = "150000.00")
    private BigDecimal orcamentoTotal;

    private String descricao;

    @Schema(description = "Status atual do projeto", example = "EM_ANALISE")
    private String status;

    @Schema(description = "Descrição do status", example = "Em análise")
    private String statusDescricao;

    @Schema(description = "Classificação de risco calculada", example = "MEDIO")
    private String risco;

    @Schema(description = "Descrição do risco", example = "Médio risco")
    private String riscoDescricao;

    private MemberResponseDTO gerente;
    private List<MemberResponseDTO> membros;
}
