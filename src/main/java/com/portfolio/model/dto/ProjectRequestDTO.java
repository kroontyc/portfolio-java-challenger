package com.portfolio.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados para criação ou atualização de um projeto")
public class ProjectRequestDTO {

    @NotBlank(message = "O nome do projeto é obrigatório")
    @Schema(description = "Nome do projeto", example = "Sistema de Vendas")
    private String nome;

    @NotNull(message = "A data de início é obrigatória")
    @Schema(description = "Data de início do projeto", example = "2026-04-01")
    private LocalDate dataInicio;

    @NotNull(message = "A previsão de término é obrigatória")
    @Schema(description = "Data prevista para término", example = "2026-08-01")
    private LocalDate previsaoTermino;

    @Schema(description = "Data real de término (preenchida ao encerrar)", example = "2026-07-20")
    private LocalDate dataRealTermino;

    @NotNull(message = "O orçamento total é obrigatório")
    @Positive(message = "O orçamento total deve ser positivo")
    @Schema(description = "Orçamento total do projeto", example = "150000.00")
    private BigDecimal orcamentoTotal;

    @Schema(description = "Descrição do projeto", example = "Novo sistema de vendas online")
    private String descricao;

    @NotNull(message = "O gerente responsável é obrigatório")
    @Schema(description = "ID do membro gerente responsável", example = "2")
    private Long gerenteId;
}
