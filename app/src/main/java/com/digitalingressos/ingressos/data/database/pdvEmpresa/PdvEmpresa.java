package com.digitalingressos.ingressos.data.database.pdvEmpresa;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PdvEmpresa {

    private static final String TAG = PdvEmpresa.class.getSimpleName();
    @SerializedName("uuid")
    private String mUuid;

    @SerializedName("token")
    private String mToken;

    @SerializedName("nome")
    private String mNome;

    @SerializedName("cpf_cnpj")
    private String mCpfCnpj;
    @SerializedName("codigo")
    private String mCodigo;

    @SerializedName("imagem")
    private String mImagem;

    @SerializedName("imagem_link")
    private String mImagemLink;

    @SerializedName("ativo")
    private boolean mAtivo;

    @SerializedName("contato_nome")
    private String mContatoNome;

    @SerializedName("contato_celular")
    private String mContatoCelular;

    @SerializedName("contato_email")
    private String mContatoEmail;


    public static Type getListType() {
        return new TypeToken<List<PdvEmpresa>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<PdvEmpresa>() {
        }.getType();
    }

    public void setNome(String nome) {
        this.mNome = nome;
    }

    public String getCodigo() {
        return mCodigo;
    }

    public String getNome() {
        return mNome;
    }

    @Override
    public String toString() {
        return mNome;
    }
}
