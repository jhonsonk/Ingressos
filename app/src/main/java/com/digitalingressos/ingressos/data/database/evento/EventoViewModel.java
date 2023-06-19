package com.digitalingressos.ingressos.data.database.evento;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.recyclercomlivedata.User;

import java.util.List;
/*
public class EventoViewModel extends ViewModel {

    LiveData<Evento> userLiveData;

    public EventoViewModel() {
        userLiveData = new MutableLiveData<>();

        // call your Rest API in init method
        init();
    }

    public LiveData<Evento> getUserMutableLiveData() {
        return userLiveData;
    }

    public void init() {
        EventoRepository eventoRepository = new EventoRepository(IngressosApplication.getAppDatabase());
        //populateList();
        userLiveData.setValue(eventoRepository.get());
    }

}

 */
