package com.portfolio.model.enums;

public enum ProjectStatus {

    EM_ANALISE("Em análise"),
    ANALISE_REALIZADA("Análise realizada"),
    ANALISE_APROVADA("Análise aprovada"),
    INICIADO("Iniciado"),
    PLANEJADO("Planejado"),
    EM_ANDAMENTO("Em andamento"),
    ENCERRADO("Encerrado"),
    CANCELADO("Cancelado");

    private final String descricao;

    ProjectStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean podeTransitarPara(ProjectStatus novoStatus) {
        if (novoStatus == CANCELADO) {
            return true;
        }

        return switch (this) {
            case EM_ANALISE -> novoStatus == ANALISE_REALIZADA;
            case ANALISE_REALIZADA -> novoStatus == ANALISE_APROVADA;
            case ANALISE_APROVADA -> novoStatus == INICIADO;
            case INICIADO -> novoStatus == PLANEJADO;
            case PLANEJADO -> novoStatus == EM_ANDAMENTO;
            case EM_ANDAMENTO -> novoStatus == ENCERRADO;
            case ENCERRADO, CANCELADO -> false;
        };
    }
}
