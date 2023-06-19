package com.digitalingressos.ingressos.data.database.enumeradores;

public enum TipoPagamento {
    DINHEIRO(0, "DINHEIRO"),
    CARTAO_DEBITO(1, "DEBITO"),
    CARTAO_CREDITO(2, "CREDITO"),
    PIX(3, "PIX"),
    PIC_PAY(5, "PIC PAY");

    private int codigo;
    private String descricao;

    TipoPagamento(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}