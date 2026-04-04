package com.portfolio.controller;

import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.model.dto.ProjectRequestDTO;
import com.portfolio.model.dto.ProjectResponseDTO;
import com.portfolio.model.dto.ProjectStatusUpdateDTO;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Gerenciamento de projetos do portfólio")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Criar novo projeto")
    public ResponseEntity<ProjectResponseDTO> criar(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO response = projectService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID")
    public ResponseEntity<ProjectResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar projetos com filtros e paginação")
    public ResponseEntity<Page<ProjectResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) ProjectStatus status,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(projectService.listar(nome, status, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto")
    public ResponseEntity<ProjectResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir projeto")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        projectService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do projeto")
    public ResponseEntity<ProjectResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProjectStatusUpdateDTO dto) {
        return ResponseEntity.ok(projectService.atualizarStatus(id, dto));
    }

    @PostMapping("/{id}/members/{memberId}")
    @Operation(summary = "Associar membro ao projeto")
    public ResponseEntity<ProjectResponseDTO> associarMembro(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(projectService.associarMembro(id, memberId));
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @Operation(summary = "Desassociar membro do projeto")
    public ResponseEntity<ProjectResponseDTO> desassociarMembro(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(projectService.desassociarMembro(id, memberId));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Listar membros do projeto")
    public ResponseEntity<List<MemberResponseDTO>> listarMembros(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.listarMembros(id));
    }
}
