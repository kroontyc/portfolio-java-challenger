package com.portfolio.model.dto;

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
public class ProjectRequestDTO {

    @NotBlank(message = "O nome do projeto é obrigatório")
    private String nome;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "A previsão de término é obrigatória")
    private LocalDate previsaoTermino;

    private LocalDate dataRealTermino;

    @NotNull(message = "O orçamento total é obrigatório")
    @Positive(message = "O orçamento total deve ser positivo")
    private BigDecimal orcamentoTotal;

    private String descricao;

    @NotNull(message = "O gerente responsável é obrigatório")
    private Long gerenteId;
}
