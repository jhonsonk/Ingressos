package com.digitalingressos.ingressos.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.data.database.AppDatabase;
import com.digitalingressos.ingressos.data.database.auth.AuthRepository;
import com.digitalingressos.ingressos.data.database.caixa.CaixaRepository;
import com.digitalingressos.ingressos.network.HttpClient;

import java.util.Locale;

public class IngressosApplication extends Application {
    private static IngressosApplication instance;
    private static Context appContext;
    private static Auth mAuthAplication;
    private static Locale mLocale;
    private static AppDatabase mAppDatabase;

    public static IngressosApplication getInstance() {
        return instance;
    }

    public static AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

    public static Auth getAppAuth() {
        return mAuthAplication;
    }

    public static void setAppAuth(Auth auth) {
        mAuthAplication = auth;
        initHttpClient();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    public static void initHttpClient() {
        HttpClient.init(getAppContext());//INICIA REQUEST

        if (mAuthAplication != null) {
            HttpClient.setBearer(mAuthAplication.getAccessToken());
        }
    }

    public static void setAuthHasExpired() {
        AuthRepository authRepository = new AuthRepository(IngressosApplication.getAppDatabase());
        authRepository.deslogarTodos();
        mAuthAplication = null;
        HttpClient.setBearer("");
    }


    public static Locale getLocale() {
        return mLocale;
    }

    @Override
    public void onCreate() {

        mLocale = new Locale("pt", "BR");

        super.onCreate();
        instance = this;
        appContext = this.getApplicationContext();

        mAppDatabase = AppDatabase.getDatabase(instance);

        AuthRepository authRepository = new AuthRepository(mAppDatabase);
        mAuthAplication = authRepository.obterLogado();

        if (mAuthAplication != null) {
            CaixaRepository caixaRepository = new CaixaRepository(mAppDatabase);
            caixaRepository.abrirCaixa(mAuthAplication);
        }

        initHttpClient();

        this.setAppContext(getApplicationContext());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}