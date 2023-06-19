package com.digitalingressos.ingressos.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.data.database.auth.AuthDao;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.data.database.caixa.CaixaDao;
import com.digitalingressos.ingressos.data.database.converts.ConvertersDate;
import com.digitalingressos.ingressos.data.database.converts.StringListConverter;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.evento.EventoDao;
import com.digitalingressos.ingressos.data.database.migration.*;
import com.digitalingressos.ingressos.data.database.pagamento.Pagamento;
import com.digitalingressos.ingressos.data.database.pagamento.PagamentoDao;
import com.digitalingressos.ingressos.data.database.pagamentoItem.PagamentoItem;
import com.digitalingressos.ingressos.data.database.pagamentoItem.PagamentoItemDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Auth.class, Evento.class, Pagamento.class, PagamentoItem.class, Caixa.class}, version = 4, exportSchema = false)
@TypeConverters({ConvertersDate.class, StringListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AuthDao authDao();

    public abstract CaixaDao caixaDao();

    public abstract EventoDao eventoDao();

    public abstract PagamentoDao pagamentoDao();

    public abstract PagamentoItemDao pagamentoItemDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app.db")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                //AuthDao dao = INSTANCE.authDao();
                //dao.deleteAll();
//                dao.deleteAll();
//
//                Word word = new Word("Hello");
//                dao.insert(word);
//                word = new Word("World");
//                dao.insert(word);
            });
        }

    };

}