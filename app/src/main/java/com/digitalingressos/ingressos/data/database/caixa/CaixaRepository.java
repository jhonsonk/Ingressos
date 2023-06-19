package com.digitalingressos.ingressos.data.database.caixa;

import com.digitalingressos.ingressos.data.database.AppDatabase;
import com.digitalingressos.ingressos.data.database.auth.Auth;

public class CaixaRepository {
    public CaixaDao mCaixaDao;

    public CaixaRepository(AppDatabase db) {

        mCaixaDao = db.caixaDao();
    }

    public Caixa abrirCaixa(Auth auth) {
        Caixa mcaixa = mCaixaDao.obterAberto(auth.getUsuarioUuid());
        if (mcaixa == null) {
            mcaixa = new Caixa(auth.getUsuarioUuid());
            mcaixa.setId(mCaixaDao.insert(mcaixa));
        }
        return mcaixa;
    }

    public Caixa obterAberto(String auth_id) {
        return mCaixaDao.obterAberto(auth_id);
    }

    public void asyncInsert(Caixa caixa) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCaixaDao.insert(caixa);
        });
    }

    public void insert(Caixa caixa) {
        mCaixaDao.insert(caixa);
    }
}
