package com.portfolio.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados de retorno de um membro")
public class MemberResponseDTO {

    @Schema(description = "ID do membro", example = "1")
    private Long id;

    @Schema(description = "Nome do membro", example = "João Silva")
    private String nome;

    @Schema(description = "Atribuição do membro", example = "FUNCIONARIO")
    private String atribuicao;

    @Schema(description = "Descrição da atribuição", example = "Funcionário")
    private String atribuicaoDescricao;
}
