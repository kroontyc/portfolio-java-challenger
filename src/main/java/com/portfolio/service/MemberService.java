package com.portfolio.service;

import com.portfolio.mapper.MemberMapper;
import com.portfolio.model.dto.MemberRequestDTO;
import com.portfolio.model.dto.MemberResponseDTO;
import com.portfolio.model.entity.Member;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Transactional
    public MemberResponseDTO criar(MemberRequestDTO dto) {
        Member member = memberMapper.toEntity(dto);
        Member salvo = memberRepository.save(member);
        return memberMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public MemberResponseDTO buscarPorId(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + id));
        return memberMapper.toResponseDTO(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> listarTodos() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
