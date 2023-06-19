package com.digitalingressos.ingressos.data.database.endereco;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class Endereco {
    @ColumnInfo(name = "cep")
    @SerializedName("cep")
    public String mCep;

    @ColumnInfo(name = "logradouro")
    @SerializedName("logradouro")
    public String mLogradouro;

    @ColumnInfo(name = "numero")
    @SerializedName("numero")
    public String mNumero;

    @ColumnInfo(name = "complemento")
    @SerializedName("complemento")
    public String mComplemento;

    @ColumnInfo(name = "bairro")
    @SerializedName("bairro")
    public String mBairro;

    @ColumnInfo(name = "cidade_id")
    @SerializedName("cidade_id")
    public int mCidadeId;

    @ColumnInfo(name = "cidade")
    @SerializedName("cidade")
    public String mCidade;

    @ColumnInfo(name = "estado_id")
    @SerializedName("estado_id")
    public int mEstadoId;

    @ColumnInfo(name = "estado")
    @SerializedName("estado")
    public String mEstado;

    public int etCidadeId() {
        return mCidadeId;
    }

    public int getEstadoId() {
        return mEstadoId;
    }

    public String getBairro() {
        return mBairro;
    }

    public String getCidade() {
        return mCidade;
    }

    public String getEstado() {
        return mEstado;
    }

    public String getLogradouro() {
        return mLogradouro;
    }

    public String getCep() {
        return mCep;
    }

    public String getComplemento() {
        return mComplemento;
    }

    public String getNumero() {
        return mNumero;
    }
}
