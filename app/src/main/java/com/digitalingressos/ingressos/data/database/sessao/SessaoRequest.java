package com.digitalingressos.ingressos.data.database.sessao;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;

import java.util.HashMap;
import java.util.Map;

public class SessaoRequest {
    private static final String TAG = SessaoRequest.class.getSimpleName();

    public static String getPath(String eventoCodigo) {
        return "/PontoVenda/Eventos/" + eventoCodigo + "/Sessoes?agrupador=NENHUM";
    }

    public void getRequest(String eventoCodigo, HttpRequestListener<Sessao> listener) {

        String url = HttpClient.getUrlBase() + this.getPath(eventoCodigo);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());
        HttpGsonRequest<Sessao> myReq = new HttpGsonRequest<Sessao>(Request.Method.GET, url, Sessao.getListType(), listener, headers);//

        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
    }
}
