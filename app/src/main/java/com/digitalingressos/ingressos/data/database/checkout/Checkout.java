package com.digitalingressos.ingressos.data.database.checkout;

import com.digitalingressos.ingressos.data.database.pdvEmpresa.PdvEmpresa;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Checkout {
    private static final String TAG = CheckoutRequest.class.getSimpleName();

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("expirado_em")
    private Date expiradoEm;

    @SerializedName("preco")
    private double preco;

    @SerializedName("taxa_servico")
    private double taxaServico;

    @SerializedName("preco_ingresso")
    private double precoIngresso;
    @SerializedName("itens")
    private String itens;

    @SerializedName("parcelas")
    private int parcelas;

    public List<CheckoutItem> checkoutItem;


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getExpiradoEm() {
        return expiradoEm;
    }

    public void setExpiradoEm(Date expirado_em) {
        this.expiradoEm = expirado_em;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getTaxaServico() {
        return this.taxaServico;
    }

    public void setTaxaServico(double taxa_servico) {
        this.taxaServico = taxa_servico;
    }

    public double getPrecoIngresso() {
        return precoIngresso;
    }

    public void setPrecoIngresso(double preco_ingresso) {
        this.precoIngresso = preco_ingresso;
    }

    public String getItens() {
        return itens;
    }

    public void setItens(String itens) {
        this.itens = itens;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public List<CheckoutItem> getCheckoutItem() {
        return checkoutItem;
    }

    public void setCheckoutItem(List<CheckoutItem> mCheckoutItem) {
        this.checkoutItem = mCheckoutItem;
    }

    public static Type getListType() {
        return new TypeToken<List<Checkout>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<Checkout>() {
        }.getType();
    }
}
