package com.digitalingressos.ingressos.data.database.pagamentoItem;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PagamentoItemDao {
    @Insert
    void insert(PagamentoItem pagamentoItem);

    @Update
    void update(PagamentoItem pagamentoItem);

    @Delete
    void delete(PagamentoItem pagamentoItem);

    @Query("SELECT * FROM pagamento_item")
    List<PagamentoItem> getAll();

    @Insert
    void insertAll(List<PagamentoItem> pagamentoItem);

}
