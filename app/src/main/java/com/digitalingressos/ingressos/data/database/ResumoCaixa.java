package com.digitalingressos.ingressos.data.database;

public class ResumoCaixa {
    private long quantidade;
    private double somaValores;

    public ResumoCaixa(long quantidade, double somaValores) {
        this.quantidade = quantidade;
        this.somaValores = somaValores;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public double getSomaValores() {
        return somaValores;
    }
}
