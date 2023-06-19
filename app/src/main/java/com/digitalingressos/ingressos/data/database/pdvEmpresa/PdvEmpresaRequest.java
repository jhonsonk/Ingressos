package com.digitalingressos.ingressos.data.database.pdvEmpresa;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;

import java.util.HashMap;
import java.util.Map;

public class PdvEmpresaRequest extends PdvEmpresa {

    public static String getPathStep() {
        return "/PontoVenda/Step/Login";
    }

    public void step(String login, String senha, HttpRequestListener<PdvEmpresa> listener) {

        String url = HttpClient.getUrlBase() + this.getPathStep();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"login\":\"" + login + "\"");
        stringBuilder.append(",\"senha\":\"" + senha + "\"");
        stringBuilder.append("}");

        String boby = stringBuilder.toString();

        HttpGsonRequest<PdvEmpresa> myReq = new HttpGsonRequest<PdvEmpresa>(Request.Method.POST, url, PdvEmpresa.getListType(), listener, headers, boby);

        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
        queue.start();

    }
}
