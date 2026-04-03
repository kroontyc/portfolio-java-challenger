package com.portfolio.model.enums;

public enum MemberRole {

    FUNCIONARIO("Funcionário"),
    GERENTE("Gerente"),
    DIRETOR("Diretor");

    private final String descricao;

    MemberRole(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
