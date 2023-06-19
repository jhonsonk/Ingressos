package com.digitalingressos.ingressos.data.database.evento;

import com.digitalingressos.ingressos.data.database.AppDatabase;

import java.util.List;

public class EventoRepository {
    public EventoDao mEventoDao;
    public EventoRepository(AppDatabase db) {
        mEventoDao = db.eventoDao();
    }

    public List<Evento> getAll() {
        return mEventoDao.getAll();
    }

    public List<Evento> getAtivos() {
        return mEventoDao.getAtivos();
    }

    public List<Evento> getAptosParaVenda() {
        return mEventoDao.getAptosParaVenda();
    }

    public void insert(Evento evento) {
        mEventoDao.insert(evento);
    }

    public void insertAll(List<Evento> eventos) {
        mEventoDao.insertAll(eventos);
    }

    public void asyncInsert(Evento evento) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mEventoDao.insert(evento);
        });
    }
}
