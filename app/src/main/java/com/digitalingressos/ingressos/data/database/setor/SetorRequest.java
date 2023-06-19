package com.digitalingressos.ingressos.data.database.setor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;

import java.util.HashMap;
import java.util.Map;

public class SetorRequest {
    private static final String TAG = SetorRequest.class.getSimpleName();

    public static String getPath(String sessaoCodigo) {
        return "/PontoVenda/Sessoes/" + sessaoCodigo + "/Ingressos";
    }

    public void getRequest(String sessaoCodigo, HttpRequestListener<Setor> listener) {

        String url = HttpClient.getUrlBase() + this.getPath(sessaoCodigo);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());
        HttpGsonRequest<Setor> myReq = new HttpGsonRequest<Setor>(Request.Method.GET, url, Setor.getListType(), listener, headers);//
        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
    }
}
