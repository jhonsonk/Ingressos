package com.digitalingressos.ingressos.data.database.enumeradores;

public enum StatusPagamento {

    PENDENTE(1, "Pendente"),
    PAGO(2, "Pago"),
    CANCELADO(3, "Cancelado"),
    ESTORNADO(4, "Estornado");

    private int codigo;
    private String descricao;

    StatusPagamento(int codigo, String descricao) {
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