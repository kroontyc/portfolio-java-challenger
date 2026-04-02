package com.portfolio.repository;

import com.portfolio.model.entity.Member;
import com.portfolio.model.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByAtribuicao(MemberRole atribuicao);

    List<Member> findByNomeContainingIgnoreCase(String nome);
}
