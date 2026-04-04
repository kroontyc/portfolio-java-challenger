package com.portfolio.controller;

import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.model.dto.ProjectRequestDTO;
import com.portfolio.model.dto.ProjectResponseDTO;
import com.portfolio.model.dto.ProjectStatusUpdateDTO;
import com.portfolio.model.enums.ProjectStatus;
import com.portfolio.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos do portfólio")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Criar novo projeto", description = "Cria um projeto com status inicial EM_ANALISE. O risco é calculado automaticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Gerente não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> criar(@Valid @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO response = projectService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar projetos", description = "Lista projetos com paginação e filtros opcionais por nome e status")
    @ApiResponse(responseCode = "200", description = "Lista de projetos retornada")
    public ResponseEntity<Page<ProjectResponseDTO>> listar(
            @Parameter(description = "Filtrar por nome (parcial, case insensitive)") @RequestParam(required = false) String nome,
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) ProjectStatus status,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(projectService.listar(nome, status, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto", description = "Atualiza todos os dados do projeto. O risco é recalculado automaticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Projeto ou gerente não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.ok(projectService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir projeto", description = "Não é permitido excluir projetos com status INICIADO, EM_ANDAMENTO ou ENCERRADO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Projeto excluído"),
            @ApiResponse(responseCode = "400", description = "Exclusão não permitida para o status atual"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        projectService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do projeto", description = "A transição deve seguir a sequência: EM_ANALISE → ANALISE_REALIZADA → ANALISE_APROVADA → INICIADO → PLANEJADO → EM_ANDAMENTO → ENCERRADO. CANCELADO pode ser aplicado a qualquer momento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProjectStatusUpdateDTO dto) {
        return ResponseEntity.ok(projectService.atualizarStatus(id, dto));
    }

    @PostMapping("/{id}/members/{memberId}")
    @Operation(summary = "Associar membro ao projeto", description = "Apenas membros com atribuição FUNCIONARIO podem ser associados. Máximo 10 por projeto, máximo 3 projetos ativos por membro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Membro associado"),
            @ApiResponse(responseCode = "400", description = "Regra de negócio violada"),
            @ApiResponse(responseCode = "404", description = "Projeto ou membro não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> associarMembro(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(projectService.associarMembro(id, memberId));
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @Operation(summary = "Desassociar membro do projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Membro desassociado"),
            @ApiResponse(responseCode = "400", description = "Membro não está no projeto"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> desassociarMembro(
            @PathVariable Long id,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(projectService.desassociarMembro(id, memberId));
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Listar membros do projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de membros retornada"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<List<MemberResponseDTO>> listarMembros(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.listarMembros(id));
    }
}
