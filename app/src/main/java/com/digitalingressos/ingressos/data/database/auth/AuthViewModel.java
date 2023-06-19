package com.digitalingressos.ingressos.data.database.auth;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.digitalingressos.ingressos.application.IngressosApplication;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository mRepository;

    public AuthViewModel(Application application) {
        super(application);
        mRepository = new AuthRepository(IngressosApplication.getAppDatabase());
    }

    public void insert(Auth auth) {
        mRepository.asyncInsert(auth);
    }
}