package com.digitalingressos.ingressos.data.database.migration;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/*
* NAO ESTA SENDO UTILIZADA*/
public class MigrationFrom3To4 extends Migration {
    public MigrationFrom3To4() {
        super(3, 4);
    }

    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // Execute as alterações de esquema necessárias aqui
        database.execSQL("CREATE TABLE IF NOT EXISTS `caixa` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, 'uuid' TEXT  , `auth_id` INTEGER, `aberto_em` INTEGER, `fechado_em` INTEGER, FOREIGN KEY(`auth_id`) REFERENCES `auth`(`usuario_uuid`) ON UPDATE NO ACTION ON DELETE NO ACTION)");
    }
}