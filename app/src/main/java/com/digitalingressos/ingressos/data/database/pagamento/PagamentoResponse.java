package com.digitalingressos.ingressos.data.database.pagamento;

import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class PagamentoResponse {

    @SerializedName("codigo")
    public String codigo;

    @SerializedName("criado_em")
    public Date criadoEm;

    @SerializedName("modificado_em")
    public Date modificadoEm;

    @SerializedName("status")
    public String status;

    @SerializedName("numero_parcelas")
    public int numeroParcelas;

    @SerializedName("valor_total")
    public double valorTotal;

    @SerializedName("desconto")
    public int desconto;

    @SerializedName("taxa_servico")
    public double taxaServico;

    @SerializedName("valor_parcial")
    public int valorParcial;

    @SerializedName("evento_nome")
    public String eventoNome;

    @SerializedName("evento_data_inicio")
    public Date eventoDataInicio;


    //GET e SET
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Date getModificadoEm() {
        return modificadoEm;
    }

    public void setModificadoEm(Date modificadoEm) {
        this.modificadoEm = modificadoEm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getDesconto() {
        return desconto;
    }

    public void setDesconto(int desconto) {
        this.desconto = desconto;
    }

    public double getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(double taxaServico) {
        this.taxaServico = taxaServico;
    }

    public int getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(int valorParcial) {
        this.valorParcial = valorParcial;
    }

    public String getEventoNome() {
        return eventoNome;
    }

    public void setEventoNome(String eventoNome) {
        this.eventoNome = eventoNome;
    }

    public Date getEventoDataInicio() {
        return eventoDataInicio;
    }

    public void setEventoDataInicio(Date eventoDataInicio) {
        this.eventoDataInicio = eventoDataInicio;
    }

    //metodos
    public static Type getListType() {
        return new TypeToken<List<PagamentoResponse>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<PagamentoResponse>() {
        }.getType();
    }
}
