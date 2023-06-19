package com.digitalingressos.ingressos.data;

import com.digitalingressos.ingressos.data.database.ingresso.Ingresso;
import com.digitalingressos.ingressos.data.database.pagamentoItem.PagamentoItem;

import java.text.DecimalFormat;
import java.util.List;

//VAI FICAR ASSIM
public class ResumoCompra {

    private List<PagamentoItem> itemPosse;
    public int quantidade;
    public double valorUnitario;
    public double valorTotal;
    private String resumoIngresso;

    public double getValorUnitario() {
        return valorUnitario;
    }


    public String getResumoIngresso() {
        return this.resumoIngresso;
    }

    public void setResumoIngresso(String resumo) {
        this.resumoIngresso = resumo;
    }


    public String getValorTotal() {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        return "R$" + dFormat.format(this.valorTotal);
    }


    public int getQuantidade() {
        return quantidade;
    }

    public List<PagamentoItem> getPosse() {
        return this.itemPosse;
    }

    public void setPosse(List<PagamentoItem> posse) {
        this.itemPosse = posse;
    }


    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValorTotal() {
        this.valorTotal = this.valorUnitario * this.quantidade;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
