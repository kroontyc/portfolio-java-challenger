package com.portfolio.service;

import com.portfolio.exception.BusinessException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.MemberMapper;
import com.portfolio.mapper.ProjectMapper;
import com.portfolio.model.dto.ProjectRequestDTO;
import com.portfolio.model.dto.ProjectResponseDTO;
import com.portfolio.model.dto.ProjectStatusUpdateDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.model.entity.Project;
import com.portfolio.model.enums.MemberRole;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.repository.MemberRepository;
import com.portfolio.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private ProjectService projectService;

    private Member gerente;
    private Member funcionario;
    private Project projeto;
    private ProjectRequestDTO requestDTO;
    private ProjectResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        gerente = Member.builder()
                .id(1L)
                .nome("Maria Gerente")
                .atribuicao(MemberRole.GERENTE)
                .build();

        funcionario = Member.builder()
                .id(2L)
                .nome("João Funcionário")
                .atribuicao(MemberRole.FUNCIONARIO)
                .build();

        projeto = Project.builder()
                .id(1L)
                .nome("Projeto Teste")
                .dataInicio(LocalDate.of(2026, 4, 1))
                .previsaoTermino(LocalDate.of(2026, 6, 1))
                .orcamentoTotal(new BigDecimal("80000"))
                .descricao("Um projeto de teste")
                .status(ProjectStatus.EM_ANALISE)
                .gerente(gerente)
                .membros(new ArrayList<>())
                .build();

        requestDTO = ProjectRequestDTO.builder()
                .nome("Projeto Teste")
                .dataInicio(LocalDate.of(2026, 4, 1))
                .previsaoTermino(LocalDate.of(2026, 6, 1))
                .orcamentoTotal(new BigDecimal("80000"))
                .descricao("Um projeto de teste")
                .gerenteId(1L)
                .build();

        responseDTO = ProjectResponseDTO.builder()
                .id(1L)
                .nome("Projeto Teste")
                .status("EM_ANALISE")
                .build();
    }
}
