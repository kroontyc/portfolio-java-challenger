package com.portfolio.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {

    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate previsaoTermino;
    private LocalDate dataRealTermino;
    private BigDecimal orcamentoTotal;
    private String descricao;
    private String status;
    private String statusDescricao;
    private String risco;
    private String riscoDescricao;
    private MemberResponseDTO gerente;
    private List<MemberResponseDTO> membros;
}
