package com.digitalingressos.ingressos.data.database.classificacaoIndicativa;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class ClassificacaoIndicativa {

    @ColumnInfo(name = "id")
    @SerializedName("id")
    public int mId;

    @ColumnInfo(name = "nome")
    @SerializedName("nome")
    public String mNome;

    @ColumnInfo(name = "sigla")
    @SerializedName("sigla")
    public String mSigla;

    @ColumnInfo(name = "cor_rgb")
    @SerializedName("cor_rgb")
    public String mCorRgb;

    public String getmNome() {
        return mNome;
    }

    public String getCorRgb() {
        return mCorRgb;
    }

    @NonNull
    public int getId() {
        return mId;
    }

    public String getSigla() {
        return mSigla;
    }
}
