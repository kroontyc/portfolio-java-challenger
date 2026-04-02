package com.portfolio.model.entity;

import com.portfolio.model.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole atribuicao;

    @ManyToMany(mappedBy = "membros")
    @Builder.Default
    private List<Project> projetos = new ArrayList<>();
}
