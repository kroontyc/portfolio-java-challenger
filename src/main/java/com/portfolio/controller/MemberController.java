package com.portfolio.controller;

import com.portfolio.model.dto.MemberRequestDTO;
import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Membros", description = "API externa mockada para gerenciamento de membros")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @Operation(summary = "Criar novo membro", description = "Cria um membro enviando nome e atribuição (cargo)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Membro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MemberResponseDTO> criar(@Valid @RequestBody MemberRequestDTO dto) {
        MemberResponseDTO response = memberService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar membro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Membro encontrado"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado")
    })
    public ResponseEntity<MemberResponseDTO> buscarPorId(@PathVariable Long id) {
        MemberResponseDTO response = memberService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os membros")
    @ApiResponse(responseCode = "200", description = "Lista de membros retornada com sucesso")
    public ResponseEntity<List<MemberResponseDTO>> listarTodos() {
        List<MemberResponseDTO> response = memberService.listarTodos();
        return ResponseEntity.ok(response);
    }
}
