package com.digitalingressos.ingressos.data.database.setor;

import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.ingresso.Ingresso;
import com.digitalingressos.ingressos.data.database.sessao.Sessao;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Setor {
    @SerializedName("codigo")
    public String mCodigo;

    @SerializedName("nome")
    public String mNome;

    @SerializedName("ordem")
    public int mOrdem;

    @SerializedName("venda_fechada")
    public boolean mVendaFechada;

    @SerializedName("ativo")
    public boolean mAtivo;

    @SerializedName("quantidade_cpf")
    public int mQuantidadeCpf;

    @SerializedName("capacidade")
    public int mCapacidade;

    @SerializedName("ingressos")
    public List<Ingresso> mIngressos;

    @SerializedName("sessao")
    public Sessao mSessao;

    public String getNome() {
        return mNome;
    }

    public Sessao getSessao() {
        return mSessao;
    }
    public List<Ingresso> getIngressos() {
        return mIngressos;
    }

    public int getQuantidadeCpf() {
        return mQuantidadeCpf;
    }

    public String getCodigo() {
        return mCodigo;
    }

    public int getCapacidade() {
        return mCapacidade;
    }

    public int getOrdem() {
        return mOrdem;
    }

    public static Type getListType() {
        return new TypeToken<List<Setor>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<Setor>() {
        }.getType();
    }
}
