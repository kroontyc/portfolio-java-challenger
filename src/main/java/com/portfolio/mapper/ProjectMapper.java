package com.portfolio.mapper;

import com.portfolio.model.dto.ProjectRequestDTO;
import com.portfolio.model.dto.ProjectResponseDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.model.entity.Project;
import com.portfolio.model.enums.Risk;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    private final MemberMapper memberMapper;

    public ProjectMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public Project toEntity(ProjectRequestDTO dto, Member gerente) {
        return Project.builder()
                .nome(dto.getNome())
                .dataInicio(dto.getDataInicio())
                .previsaoTermino(dto.getPrevisaoTermino())
                .dataRealTermino(dto.getDataRealTermino())
                .orcamentoTotal(dto.getOrcamentoTotal())
                .descricao(dto.getDescricao())
                .gerente(gerente)
                .build();
    }

    public void updateEntity(Project project, ProjectRequestDTO dto, Member gerente) {
        project.setNome(dto.getNome());
        project.setDataInicio(dto.getDataInicio());
        project.setPrevisaoTermino(dto.getPrevisaoTermino());
        project.setDataRealTermino(dto.getDataRealTermino());
        project.setOrcamentoTotal(dto.getOrcamentoTotal());
        project.setDescricao(dto.getDescricao());
        project.setGerente(gerente);
    }

    public ProjectResponseDTO toResponseDTO(Project entity) {
        Risk risco = Risk.calcular(
                entity.getOrcamentoTotal(),
                entity.getDataInicio(),
                entity.getPrevisaoTermino()
        );

        return ProjectResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .dataInicio(entity.getDataInicio())
                .previsaoTermino(entity.getPrevisaoTermino())
                .dataRealTermino(entity.getDataRealTermino())
                .orcamentoTotal(entity.getOrcamentoTotal())
                .descricao(entity.getDescricao())
                .status(entity.getStatus().name())
                .statusDescricao(entity.getStatus().getDescricao())
                .risco(risco.name())
                .riscoDescricao(risco.getDescricao())
                .gerente(entity.getGerente() != null
                        ? memberMapper.toResponseDTO(entity.getGerente())
                        : null)
                .membros(entity.getMembros() != null
                        ? entity.getMembros().stream()
                            .map(memberMapper::toResponseDTO)
                            .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}
