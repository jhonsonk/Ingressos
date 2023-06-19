package com.digitalingressos.ingressos.data.database.pagamento;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PagamentoDao {
    @Insert
    long insert(Pagamento pagamento);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Pagamento pagamento);

    @Delete
    void delete(Pagamento pagamento);

    @Query("SELECT * FROM pagamento")
    List<Pagamento> getAll();

    @Query("SELECT * FROM pagamento WHERE id =:id limit 1")
    Pagamento obterPeloId(long id);

    @Query("SELECT * FROM pagamento where caixa_id =:caixaId ")
    List<Pagamento> obterPagamentosCaixa(long caixaId);

    @Transaction
    @Query("SELECT * FROM pagamento")
    List<PagamentoComItens> getPagamentosComItens();

    @Query("SELECT * FROM pagamento where transacao_codigo = :transacao_codigo limit 1")
    Pagamento obterPorTransacaoCodigo(String transacao_codigo);
}
