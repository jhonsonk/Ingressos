package com.digitalingressos.ingressos.data.database.caixa;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CaixaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Caixa caixa);

    @Query("select * FROM caixa where auth_id = :auth_id AND fechado_em IS NULL limit 1")
    Caixa obterAberto(String auth_id);

}
