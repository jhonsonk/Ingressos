package com.digitalingressos.ingressos.data.database.checkout;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutItem {

    @SerializedName("UuidValor")
    private String uuidValor;

    @SerializedName("Quantidade")
    private long quantidade;

    @SerializedName("ListaPosses")
    private List<CheckoutItemPosse> posses;

    public CheckoutItem() {
    }

    public CheckoutItem(String uuidValor, long quantidade, List<CheckoutItemPosse> posses) {
        this.uuidValor = uuidValor;
        this.quantidade = quantidade;
        this.posses = posses.stream().map(objeto -> new CheckoutItemPosse(objeto.mBilheteCodigo)).collect(Collectors.toList());
    }


    public String getUuidValor() {
        return uuidValor;
    }

    public void setUuidValor(String uuidValor) {
        this.uuidValor = uuidValor;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    public List<CheckoutItemPosse> getPosses() {
        return posses;
    }

    public void setPosses(List<CheckoutItemPosse> posses) {
        this.posses = posses;
    }

    public static Type getListType() {
        return new TypeToken<List<CheckoutItem>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<CheckoutItem>() {
        }.getType();
    }
}
