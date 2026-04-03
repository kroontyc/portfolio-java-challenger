package com.portfolio.service;

import com.portfolio.exception.BusinessException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.ProjectMapper;
import com.portfolio.model.dto.ProjectRequestDTO;
import com.portfolio.model.dto.ProjectResponseDTO;
import com.portfolio.model.dto.ProjectStatusUpdateDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.model.entity.Project;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.repository.MemberRepository;
import com.portfolio.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(
            ProjectRepository projectRepository,
            MemberRepository memberRepository,
            ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.projectMapper = projectMapper;
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

    private Project buscarProjetoOuFalhar(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com id: " + id));
    }

    private Member buscarGerente(Long gerenteId) {
        return memberRepository.findById(gerenteId).orElseThrow(() -> new ResourceNotFoundException("Gerente não encontrado com id: " + gerenteId));
    }
}
