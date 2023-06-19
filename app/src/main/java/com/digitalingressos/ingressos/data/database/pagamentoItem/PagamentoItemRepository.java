package com.digitalingressos.ingressos.data.database.pagamentoItem;

import com.digitalingressos.ingressos.data.database.AppDatabase;

import java.util.List;

public class PagamentoItemRepository {

    public PagamentoItemDao mPagamentoItemDao;

    public PagamentoItemRepository(AppDatabase db) {
        mPagamentoItemDao = db.pagamentoItemDao();
    }

    public List<PagamentoItem> getAll() {
        return mPagamentoItemDao.getAll();
    }

    public void insert(PagamentoItem pagamentoItem) {
        mPagamentoItemDao.insert(pagamentoItem);
    }

    public void insertAll(List<PagamentoItem> pagamentoItem) {
        mPagamentoItemDao.insertAll(pagamentoItem);
    }

    public void asyncInsert(PagamentoItem pagamentoItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mPagamentoItemDao.insert(pagamentoItem);
        });
    }
}

