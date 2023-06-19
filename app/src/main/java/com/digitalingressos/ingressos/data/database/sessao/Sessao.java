package com.digitalingressos.ingressos.data.database.sessao;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

//@Entity(tableName = "sessao")
public class Sessao {

    //@ColumnInfo(name = "codigo")
    @SerializedName("codigo")
    public String mCodigo;

    //@ColumnInfo(name = "nome")
    @SerializedName("nome")
    public String mNome;

    //@ColumnInfo(name = "inicio")
    @SerializedName("inicio")
    public Date mInicio;

    //@ColumnInfo(name = "fim")
    @SerializedName("fim")
    public Date mFim;

    //@ColumnInfo(name = "status")
    @SerializedName("status")
    public String mStatus;

    //@Embedded(prefix = "evento_")
    @SerializedName("evento")
    public Evento mEvento;

    public Date getFim() {
        return mFim;
    }

    public Date getInicio() {
        return mInicio;
    }

    @NonNull
    public String getCodigo() {
        return mCodigo;
    }

    public String getNome() {
        return mNome;
    }

    public String getStatus() {
        return mStatus;
    }

    public Evento getEvento (){ return mEvento;}

    public static Type getListType() {
        return new TypeToken<List<Sessao>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<Sessao>() {
        }.getType();
    }
}
