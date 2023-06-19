package com.digitalingressos.ingressos.data.database.auth;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AuthDao {

    @Query("SELECT * FROM auth limit 1")
    Auth get();

    @Query("SELECT * FROM auth WHERE  logado = 1")
    Auth obterLogado();

    @Query("UPDATE auth SET logado = 0")
    void deslogarTodos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Auth auth);

    @Delete
    void delete(Auth auth);

    @Update(onConflict = OnConflictStrategy.FAIL)
    void update(Auth auth);

    @Query("DELETE FROM auth")
    void deleteAll();
}
