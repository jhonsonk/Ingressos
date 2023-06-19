package com.digitalingressos.ingressos.data.database.evento;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface EventoDao {

    @Query("SELECT * FROM evento")
    List<Evento> getAll();

    @Query("SELECT * FROM evento where status = 'ATIVO'")
    List<Evento> getAtivos();

    @Query("SELECT * FROM evento where exibir_pdv_fisico = 1 and status = 'ATIVO'")
    List<Evento> getAptosParaVenda();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Evento evento);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Evento> evento);
}


