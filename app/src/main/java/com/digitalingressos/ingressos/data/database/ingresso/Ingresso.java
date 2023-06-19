package com.digitalingressos.ingressos.data.database.ingresso;

import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.setor.Setor;
import com.digitalingressos.ingressos.util.UtilMoney;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Ingresso {

    //#region Propriedade
    @SerializedName("nome")
    private String nome;
    @SerializedName("codigo")
    private String codigo;
    @SerializedName("sexo")
    private String sexo;
    @SerializedName("tipo")
    private String tipo;
    @SerializedName("ordem")
    private int ordem;
    @SerializedName("ativo")
    private boolean ativo;
    @SerializedName("taxa_servico")
    private double taxaServico;
    @SerializedName("preco_ingresso")
    private double precoIngresso;
    @SerializedName("combo")
    private int combo;
    @SerializedName("valor")
    private double valor;
    @SerializedName("quantidade")
    private int quantidade;
    @SerializedName("lote_nome")
    private String loteNome;
    @SerializedName("lote_codigo")
    private String loteCodigo;
    @SerializedName("valor_uuid")
    private String valorLoteIngressoUuid;
    @SerializedName("setor")
    private Setor setor;

    //#endregion

    //#region Gets e Sets
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public double getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(double taxaServico) {
        this.taxaServico = taxaServico;
    }

    public double getPrecoIngresso() {
        return precoIngresso;
    }

    public void setPrecoIngresso(double precoIngresso) {
        this.precoIngresso = precoIngresso;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getLoteNome() {
        return loteNome;
    }

    public void setLoteNome(String loteNome) {
        this.loteNome = loteNome;
    }

    public String getLoteCodigo() {
        return loteCodigo;
    }

    public void setLoteCodigo(String loteCodigo) {
        this.loteCodigo = loteCodigo;
    }

    public String getValorLoteIngressoUuid() {
        return valorLoteIngressoUuid;
    }

    public void setValorLoteIngressoUuid(String valorLoteIngressoUuid) {
        this.valorLoteIngressoUuid = valorLoteIngressoUuid;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    //#endregion

    //#region Metodos
    public String getPrecoIngressoMonetary() {
        return UtilMoney.parseDoubleToMoney(this.precoIngresso, IngressosApplication.getLocale());
    }

    public String getTaxaServicoMonetary() {
        return UtilMoney.parseDoubleToMoney(this.taxaServico, IngressosApplication.getLocale());
    }

    public String getTipoValido() {
        if (this.getTipo() == "Em_Aberto")
            return "";
        return this.getTipo().replace("_", " ");
    }

    public String getResumoIngresso() {
        return this.getNome() + " " + this.getTipoValido() + " " + this.getSexo();
    }

    public String getValorMonetary() {
        return UtilMoney.parseDoubleToMoney(this.valor, IngressosApplication.getLocale());
    }

    public static Type getListType() {
        return new TypeToken<List<Ingresso>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<Ingresso>() {
        }.getType();
    }

    @Override
    public boolean equals(Object obj) {
        Ingresso s = (Ingresso) obj;
        return this.valorLoteIngressoUuid.equalsIgnoreCase(s.valorLoteIngressoUuid);
    }
    //#endregion
}

