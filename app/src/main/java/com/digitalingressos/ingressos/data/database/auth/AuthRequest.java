package com.digitalingressos.ingressos.data.database.auth;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthRequest {
    private static final String TAG = AuthRequest.class.getSimpleName();
    @SerializedName("login")
    private String mLogin;
    @SerializedName("usuario_uuid")
    private String mUsuarioUuid;

    @SerializedName("expires_in")
    private Date mExpiresIn;
    @SerializedName("senha")
    private String mSenha;

    @SerializedName("pdv_empresa")
    private String mPdvEmpresa;

    @SerializedName("dominio")
    private String mDominio;

    @SerializedName("access_token")
    private String mAccessToken;

    public static String getPathLogin() {
        return "/PontoVenda/Login";
    }

    public static Type getListType() {
        return new TypeToken<List<AuthRequest>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<AuthRequest>() {
        }.getType();
    }


    public void login(String login, String senha, String pdv_empresa, HttpRequestListener<AuthRequest> listener) {

        this.mLogin = login;
        this.mSenha = senha;
        this.mPdvEmpresa = pdv_empresa;

        String url = HttpClient.getUrlBase() + this.getPathLogin();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"login\":\"" + login + "\"");
        stringBuilder.append(",\"senha\":\"" + senha + "\"");
        stringBuilder.append(",\"pdv_empresa\":\"" + pdv_empresa + "\"");
        stringBuilder.append("}");

        String boby = stringBuilder.toString();

        HttpGsonRequest<AuthRequest> myReq = new HttpGsonRequest<AuthRequest>(Request.Method.POST, url, this.getType(), listener, headers, boby);

        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
        queue.start();
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

    public void setSenha(String senha) {
        this.mSenha = senha;
    }

    public void setPdvEmpresa(String pdv_empresa) {
        this.mPdvEmpresa = pdv_empresa;
    }

    public String getUsuarioUuid() {
        return mUsuarioUuid;
    }

    public void setUsuarioUuid(String mUsuarioUuid) {
        this.mUsuarioUuid = mUsuarioUuid;
    }

    public Date getExpiresIn() {
        return mExpiresIn;
    }

    public void setExpiresIn(Date mExpiresIn) {
        this.mExpiresIn = mExpiresIn;
    }
}