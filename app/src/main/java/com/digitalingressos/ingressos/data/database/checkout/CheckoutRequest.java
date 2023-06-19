package com.digitalingressos.ingressos.data.database.checkout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.digitalingressos.ingressos.data.database.pdvEmpresa.PdvEmpresa;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutRequest {

    private static final String TAG = CheckoutRequest.class.getSimpleName();
    @SerializedName("uuid_valor")
    public String mUuidValorLoteIngresso;

    @SerializedName("quantidade")
    public long mQuantidade;

    public static String getPath() {
        return "/PontoVenda/Checkout";
    }

    public CheckoutRequest (String uuidValorLoteIngresso, long quantidade){
        this.mUuidValorLoteIngresso =uuidValorLoteIngresso;
        mQuantidade = quantidade;
    }
    public static void checkout(List<CheckoutRequest> listaCheckout, HttpRequestListener<Checkout> listener) {

        String url = HttpClient.getUrlBase() + CheckoutRequest.getPath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());

        Gson gson = new GsonBuilder().create();
        String boby = gson.toJson(listaCheckout);

        HttpGsonRequest<Checkout> myReq = new HttpGsonRequest<Checkout>(Request.Method.POST, url, Checkout.getType(), listener, headers, boby);

        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
        queue.start();

    }
}
