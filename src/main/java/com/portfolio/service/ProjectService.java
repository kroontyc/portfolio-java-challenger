package com.portfolio.service;

import com.portfolio.exception.BusinessException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ProjectMapper;
import com.portfolio.model.dto.ProjectRequestDTO;
import com.portfolio.model.dto.ProjectResponseDTO;
import com.portfolio.model.dto.ProjectStatusUpdateDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.model.entity.Project;
import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.model.enums.MemberRole;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.mapper.MemberMapper;
import com.portfolio.repository.MemberRepository;
import com.portfolio.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final int MAX_MEMBROS_POR_PROJETO = 10;
    private static final int MAX_PROJETOS_ATIVOS_POR_MEMBRO = 3;

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMapper projectMapper;
    private final MemberMapper memberMapper;

    public ProjectService(
            ProjectRepository projectRepository,
            MemberRepository memberRepository,
            ProjectMapper projectMapper,
            MemberMapper memberMapper) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.projectMapper = projectMapper;
        this.memberMapper = memberMapper;
    }

    @Transactional
    public ProjectResponseDTO criar(ProjectRequestDTO dto) {
        Member gerente = buscarGerente(dto.getGerenteId());

        Project projeto = projectMapper.toEntity(dto, gerente);
        projeto.setStatus(ProjectStatus.EM_ANALISE);

        Project salvo = projectRepository.save(projeto);
        return projectMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO buscarPorId(Long id) {
        Project projeto = buscarProjetoOuFalhar(id);
        return projectMapper.toResponseDTO(projeto);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> listar(String nome, ProjectStatus status, Pageable pageable) {
        Page<Project> projetos = projectRepository.findByFiltros(nome, status, pageable);
        return projetos.map(projectMapper::toResponseDTO);
    }

    @Transactional
    public ProjectResponseDTO atualizar(Long id, ProjectRequestDTO dto) {
        Project projeto = buscarProjetoOuFalhar(id);
        Member gerente = buscarGerente(dto.getGerenteId());

        projectMapper.updateEntity(projeto, dto, gerente);

        Project salvo = projectRepository.save(projeto);
        return projectMapper.toResponseDTO(salvo);
    }

    @Transactional
    public void excluir(Long id) {
        Project projeto = buscarProjetoOuFalhar(id);
        if (projeto.getStatus() == ProjectStatus.INICIADO
                || projeto.getStatus() == ProjectStatus.EM_ANDAMENTO
                || projeto.getStatus() == ProjectStatus.ENCERRADO) {
            throw new BusinessException("Não é possível excluir um projeto com status: " + projeto.getStatus().getDescricao());
        }

        projectRepository.delete(projeto);
    }

    @Transactional
    public ProjectResponseDTO atualizarStatus(Long id, ProjectStatusUpdateDTO dto) {
        Project projeto = buscarProjetoOuFalhar(id);

        ProjectStatus statusAtual = projeto.getStatus();
        ProjectStatus novoStatus = dto.getNovoStatus();

        if (!statusAtual.podeTransitarPara(novoStatus)) {
            throw new BusinessException(
                "Transição de status inválida: " + statusAtual.getDescricao() + " → " + novoStatus.getDescricao()
            );
        }
        projeto.setStatus(novoStatus);
        if (novoStatus == ProjectStatus.ENCERRADO && projeto.getDataRealTermino() == null) {
            projeto.setDataRealTermino(java.time.LocalDate.now());
        }

        Project salvo = projectRepository.save(projeto);
        return projectMapper.toResponseDTO(salvo);
    }

    @Transactional
    public ProjectResponseDTO associarMembro(Long projetoId, Long membroId) {
        Project projeto = buscarProjetoOuFalhar(projetoId);
        Member membro = memberRepository.findById(membroId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + membroId));

        // so funcionario pode ser associado
        if (membro.getAtribuicao() != MemberRole.FUNCIONARIO) {
            throw new BusinessException("Apenas membros com atribuição 'funcionário' podem ser associados a projetos");
        }

        // verifica se ja ta no projeto
        boolean jaAssociado = projeto.getMembros().stream()
                .anyMatch(m -> m.getId().equals(membroId));
        if (jaAssociado) {
            throw new BusinessException("Membro já está associado a este projeto");
        }

        // max 10 membros por projeto
        if (projeto.getMembros().size() >= MAX_MEMBROS_POR_PROJETO) {
            throw new BusinessException("O projeto já atingiu o limite máximo de " + MAX_MEMBROS_POR_PROJETO + " membros");
        }

        // membro nao pode estar em mais de 3 projetos ativos
        List<ProjectStatus> statusInativos = List.of(ProjectStatus.ENCERRADO, ProjectStatus.CANCELADO);
        long projetosAtivos = projectRepository.contarProjetosAtivosDoMembro(membroId, statusInativos);
        if (projetosAtivos >= MAX_PROJETOS_ATIVOS_POR_MEMBRO) {
            throw new BusinessException("Membro já está alocado em " + MAX_PROJETOS_ATIVOS_POR_MEMBRO + " projetos ativos");
        }

        projeto.getMembros().add(membro);
        Project salvo = projectRepository.save(projeto);
        return projectMapper.toResponseDTO(salvo);
    }

    @Transactional
    public ProjectResponseDTO desassociarMembro(Long projetoId, Long membroId) {
        Project projeto = buscarProjetoOuFalhar(projetoId);

        boolean removido = projeto.getMembros().removeIf(m -> m.getId().equals(membroId));
        if (!removido) {
            throw new BusinessException("Membro não está associado a este projeto");
        }

        Project salvo = projectRepository.save(projeto);
        return projectMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> listarMembros(Long projetoId) {
        Project projeto = buscarProjetoOuFalhar(projetoId);
        return projeto.getMembros().stream()
                .map(memberMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Project buscarProjetoOuFalhar(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com id: " + id));
    }

    private Member buscarGerente(Long gerenteId) {
        return memberRepository.findById(gerenteId).orElseThrow(() -> new ResourceNotFoundException("Gerente não encontrado com id: " + gerenteId));
    }
}
