package com.portfolio.model.dto;

import com.portfolio.model.enums.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequestDTO {

    @NotBlank(message = "O nome do membro é obrigatório")
    private String nome;

    @NotNull(message = "A atribuição é obrigatória")
    private MemberRole atribuicao;
}
