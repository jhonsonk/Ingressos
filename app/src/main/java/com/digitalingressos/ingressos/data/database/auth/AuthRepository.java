package com.digitalingressos.ingressos.data.database.auth;

import com.digitalingressos.ingressos.data.database.AppDatabase;

public class AuthRepository {
    public AuthDao mAuthDao;

    public AuthRepository(AppDatabase db) {
        mAuthDao = db.authDao();
    }

    public Auth get() {
        return mAuthDao.get();
    }

    public Auth obterLogado() {
        return mAuthDao.obterLogado();
    }

    public void asyncInsert(Auth auth) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAuthDao.insert(auth);
        });
    }

    public void deslogarTodos() {
        mAuthDao.deslogarTodos();
    }

    public void insert(Auth auth) {
        deslogarTodos();
        mAuthDao.insert(auth);
    }
}
