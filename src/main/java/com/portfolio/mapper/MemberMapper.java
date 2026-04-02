package com.portfolio.mapper;

import com.portfolio.model.dto.MemberRequestDTO;
import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.model.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(MemberRequestDTO dto) {
        return Member.builder()
                .nome(dto.getNome())
                .atribuicao(dto.getAtribuicao())
                .build();
    }

    public MemberResponseDTO toResponseDTO(Member entity) {
        return MemberResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .atribuicao(entity.getAtribuicao().name())
                .atribuicaoDescricao(entity.getAtribuicao().getDescricao())
                .build();
    }
}
