package com.digitalingressos.ingressos.data.database.pagamento;

import com.digitalingressos.ingressos.data.database.AppDatabase;
import com.digitalingressos.ingressos.data.database.enumeradores.StatusPagamento;
import com.digitalingressos.ingressos.network.HttpRequestListener;

import java.util.List;

public class PagamentoRepository {

    public PagamentoDao mPagamentoDao;

    public PagamentoRepository(AppDatabase db) {
        mPagamentoDao = db.pagamentoDao();
    }


    public long insert(Pagamento pagamento) {
        return mPagamentoDao.insert(pagamento);
    }

    public void update(Pagamento pagamento) {
        mPagamentoDao.update(pagamento);
    }

    public List<Pagamento> getAll() {
        return mPagamentoDao.getAll();
    }

    public  void estornarPagamento  (Pagamento pagamento){
        pagamento.setStatus(StatusPagamento.ESTORNADO);
        mPagamentoDao.update(pagamento);
    }

    public Pagamento obterPorTransacaoCodigo(String obterPorTransacaoCodigo) {
        return mPagamentoDao.obterPorTransacaoCodigo(obterPorTransacaoCodigo);
    }

    public List<Pagamento> obterPagamentosCaixa(long caixaId) {
        return mPagamentoDao.obterPagamentosCaixa(caixaId);
    }

    public void asyncInsert(Pagamento pagamento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mPagamentoDao.insert(pagamento);
        });
    }
}

