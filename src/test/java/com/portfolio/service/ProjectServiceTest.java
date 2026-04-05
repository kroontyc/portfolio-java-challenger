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

    @Nested
    @DisplayName("Criar projeto")
    class CriarProjeto {

        @Test
        @DisplayName("Deve criar projeto com sucesso")
        void deveCriarProjeto() {
            when(memberRepository.findById(1L)).thenReturn(Optional.of(gerente));
            when(projectMapper.toEntity(any(), any())).thenReturn(projeto);
            when(projectRepository.save(any())).thenReturn(projeto);
            when(projectMapper.toResponseDTO(any())).thenReturn(responseDTO);

            ProjectResponseDTO result = projectService.criar(requestDTO);

            assertNotNull(result);
            assertEquals("Projeto Teste", result.getNome());
            verify(projectRepository).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando gerente não existe")
        void deveFalharQuandoGerenteNaoExiste() {
            when(memberRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> projectService.criar(requestDTO));
        }
    }

    @Nested
    @DisplayName("Buscar projeto")
    class BuscarProjeto {

        @Test
        @DisplayName("Deve buscar projeto por ID")
        void deveBuscarPorId() {
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(projectMapper.toResponseDTO(any())).thenReturn(responseDTO);

            ProjectResponseDTO result = projectService.buscarPorId(1L);

            assertNotNull(result);
            verify(projectRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando projeto não existe")
        void deveFalharQuandoNaoExiste() {
            when(projectRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> projectService.buscarPorId(99L));
        }
    }

    @Nested
    @DisplayName("Excluir projeto")
    class ExcluirProjeto {

        @Test
        @DisplayName("Deve excluir projeto com status EM_ANALISE")
        void deveExcluirEmAnalise() {
            projeto.setStatus(ProjectStatus.EM_ANALISE);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));

            projectService.excluir(1L);

            verify(projectRepository).delete(projeto);
        }

        @Test
        @DisplayName("Deve excluir projeto com status ANALISE_REALIZADA")
        void deveExcluirAnaliseRealizada() {
            projeto.setStatus(ProjectStatus.ANALISE_REALIZADA);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));

            projectService.excluir(1L);

            verify(projectRepository).delete(projeto);
        }

        @Test
        @DisplayName("Não deve excluir projeto INICIADO")
        void naoDeveExcluirIniciado() {
            projeto.setStatus(ProjectStatus.INICIADO);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));

            assertThrows(BusinessException.class, () -> projectService.excluir(1L));
            verify(projectRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Não deve excluir projeto EM_ANDAMENTO")
        void naoDeveExcluirEmAndamento() {
            projeto.setStatus(ProjectStatus.EM_ANDAMENTO);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));

            assertThrows(BusinessException.class, () -> projectService.excluir(1L));
        }

        @Test
        @DisplayName("Não deve excluir projeto ENCERRADO")
        void naoDeveExcluirEncerrado() {
            projeto.setStatus(ProjectStatus.ENCERRADO);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));

            assertThrows(BusinessException.class, () -> projectService.excluir(1L));
        }
    }

    @Nested
    @DisplayName("Atualizar status")
    class AtualizarStatus {

        @Test
        @DisplayName("Deve atualizar status na sequência correta")
        void deveAtualizarStatusCorreto() {
            projeto.setStatus(ProjectStatus.EM_ANALISE);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(projectRepository.save(any())).thenReturn(projeto);
            when(projectMapper.toResponseDTO(any())).thenReturn(responseDTO);

            ProjectStatusUpdateDTO dto = new ProjectStatusUpdateDTO(ProjectStatus.ANALISE_REALIZADA);
            projectService.atualizarStatus(1L, dto);

            assertEquals(ProjectStatus.ANALISE_REALIZADA, projeto.getStatus());
        }

        @Test
        @DisplayName("Deve lançar exceção ao pular etapas")
        void deveFalharAoPularEtapas() {
            projeto.setStatus(ProjectStatus.EM_ANALISE);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));

            ProjectStatusUpdateDTO dto = new ProjectStatusUpdateDTO(ProjectStatus.INICIADO);

            assertThrows(BusinessException.class, () -> projectService.atualizarStatus(1L, dto));
        }

        @Test
        @DisplayName("Deve permitir cancelar a qualquer momento")
        void devePermitirCancelar() {
            projeto.setStatus(ProjectStatus.INICIADO);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(projectRepository.save(any())).thenReturn(projeto);
            when(projectMapper.toResponseDTO(any())).thenReturn(responseDTO);

            ProjectStatusUpdateDTO dto = new ProjectStatusUpdateDTO(ProjectStatus.CANCELADO);
            projectService.atualizarStatus(1L, dto);

            assertEquals(ProjectStatus.CANCELADO, projeto.getStatus());
        }

        @Test
        @DisplayName("Deve preencher data real de término ao encerrar")
        void devePreencherDataRealTermino() {
            projeto.setStatus(ProjectStatus.EM_ANDAMENTO);
            projeto.setDataRealTermino(null);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(projeto));
            when(projectRepository.save(any())).thenReturn(projeto);
            when(projectMapper.toResponseDTO(any())).thenReturn(responseDTO);

            ProjectStatusUpdateDTO dto = new ProjectStatusUpdateDTO(ProjectStatus.ENCERRADO);
            projectService.atualizarStatus(1L, dto);

            assertNotNull(projeto.getDataRealTermino());
            assertEquals(LocalDate.now(), projeto.getDataRealTermino());
        }
    }
}
