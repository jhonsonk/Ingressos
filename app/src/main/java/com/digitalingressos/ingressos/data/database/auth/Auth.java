package com.digitalingressos.ingressos.data.database.auth;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "auth")
public class Auth {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "usuario_uuid")
    private String mUsuarioUuid;

    @NonNull
    @ColumnInfo(name = "login")
    private String mLogin;

    @NonNull
    @ColumnInfo(name = "senha")
    private String mSenha;

    @ColumnInfo(name = "pdv_empresa")
    private String mPdvEmpresa;

    @ColumnInfo(name = "pdv_nome")
    private String mPdvNome;

    @NonNull
    @ColumnInfo(name = "dominio")
    private String mDominio;

    @NonNull
    @ColumnInfo(name = "access_token")
    private String mAccessToken;

    @NonNull
    @ColumnInfo(name = "expires_in")
    private Date mExpiresIn;

    @ColumnInfo(name = "logado")
    private boolean logado;

    @NonNull
    public Date getExpiresIn() {
        return mExpiresIn;
    }

    public void setExpiresIn(@NonNull Date mExpiresIn) {
        this.mExpiresIn = mExpiresIn;
    }


    public Auth(@NonNull String login, @NonNull String senha, @NonNull String dominio, @NonNull String accessToken, String pdvEmpresa, String pdvNome) {

        this.mLogin = login;
        this.mSenha = senha;
        this.mPdvEmpresa = pdvEmpresa;
        this.mDominio = dominio;
        this.mAccessToken = accessToken;
        this.mPdvNome = pdvNome;
        this.logado = true;
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }

    public String getPdvNome() {
        return this.mPdvNome;
    }

    @NonNull
    public String getAccessToken() {
        return this.mAccessToken;
    }

    @NonNull
    public String getDominio() {
        return mDominio;
    }

    @NonNull
    public String getLogin() {
        return mLogin;
    }

    @NonNull
    public String getPdvEmpresa() {
        return mPdvEmpresa;
    }

    @NonNull
    public String getSenha() {
        return mSenha;
    }

    @NonNull
    public String getUsuarioUuid() {
        return mUsuarioUuid;
    }

    public void setUsuarioUuid(@NonNull String mUsuarioUuid) {
        this.mUsuarioUuid = mUsuarioUuid;
    }
}