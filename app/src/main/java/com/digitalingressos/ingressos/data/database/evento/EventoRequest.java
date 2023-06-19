package com.digitalingressos.ingressos.data.database.evento;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;

import java.util.HashMap;
import java.util.Map;

public class EventoRequest {
    private static final String TAG = EventoRequest.class.getSimpleName();

    public static String getPath() {
        return "/PontoVenda/Eventos";
    }

    public void getRequest(HttpRequestListener<Evento> listener) {

        String url = HttpClient.getUrlBase() + this.getPath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());
        HttpGsonRequest<Evento> myReq = new HttpGsonRequest<Evento>(Request.Method.GET, url, Evento.getListType(), listener, headers);//

        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
    }
}
