package com.portfolio.service;

import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.mapper.MemberMapper;
import com.portfolio.model.dto.MemberRequestDTO;
import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.model.enums.MemberRole;
import com.portfolio.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberService memberService;

    private Member membro;
    private MemberRequestDTO requestDTO;
    private MemberResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        membro = Member.builder()
                .id(1L)
                .nome("João Silva")
                .atribuicao(MemberRole.FUNCIONARIO)
                .build();

        requestDTO = MemberRequestDTO.builder()
                .nome("João Silva")
                .atribuicao(MemberRole.FUNCIONARIO)
                .build();

        responseDTO = MemberResponseDTO.builder()
                .id(1L)
                .nome("João Silva")
                .atribuicao("FUNCIONARIO")
                .atribuicaoDescricao("Funcionário")
                .build();
    }

    @Test
    @DisplayName("Deve criar membro com sucesso")
    void deveCriarMembro() {
        when(memberMapper.toEntity(any())).thenReturn(membro);
        when(memberRepository.save(any())).thenReturn(membro);
        when(memberMapper.toResponseDTO(any())).thenReturn(responseDTO);

        MemberResponseDTO result = memberService.criar(requestDTO);

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        assertEquals("FUNCIONARIO", result.getAtribuicao());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("Deve buscar membro por ID")
    void deveBuscarPorId() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(memberMapper.toResponseDTO(any())).thenReturn(responseDTO);

        MemberResponseDTO result = memberService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar membro inexistente")
    void deveFalharBuscarInexistente() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Deve listar todos os membros")
    void deveListarTodos() {
        when(memberRepository.findAll()).thenReturn(List.of(membro));
        when(memberMapper.toResponseDTO(any())).thenReturn(responseDTO);

        List<MemberResponseDTO> result = memberService.listarTodos();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
