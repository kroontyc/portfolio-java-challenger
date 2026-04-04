package com.portfolio.model.dto;

import com.portfolio.model.enums.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados para criação de um membro")
public class MemberRequestDTO {

    @NotBlank(message = "O nome do membro é obrigatório")
    @Schema(description = "Nome do membro", example = "João Silva")
    private String nome;

    @NotNull(message = "A atribuição é obrigatória")
    @Schema(description = "Atribuição/cargo do membro", example = "FUNCIONARIO")
    private MemberRole atribuicao;
}
