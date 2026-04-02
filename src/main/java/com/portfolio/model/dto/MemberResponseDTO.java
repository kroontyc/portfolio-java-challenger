package com.portfolio.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {

    private Long id;
    private String nome;
    private String atribuicao;
    private String atribuicaoDescricao;
}
