package com.digitalingressos.ingressos.data.database.pagamento;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.evento.EventoRequest;
import com.digitalingressos.ingressos.network.HttpClient;
import com.digitalingressos.ingressos.network.HttpGsonRequest;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagamentoRequest {

    private static final String TAG = PagamentoRequest.class.getSimpleName();
    @SerializedName("checkout_codigo")
    private String checkoutCodigo;

    @SerializedName("numero_parcelas")
    private int numeroParcelas;

    @SerializedName("metodo_pagamento")
    private String metodoPagamento;

    @SerializedName("bandeira")
    private String bandeira;

    @SerializedName("cliente_pagador_cpf")
    private String clientePagadorCpf;

    @SerializedName("cliente_pagador_telefone")
    private String clientePagadorTelefone;

    @SerializedName("posses")
    private List<PagamentoIngressoPosseRequest> posses;

    //GET E SET
    public String getCheckoutCodigo() {
        return checkoutCodigo;
    }

    public void setCheckoutCodigo(String checkoutCodigo) {
        this.checkoutCodigo = checkoutCodigo;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getClientePagadorCpf() {
        return clientePagadorCpf;
    }

    public void setClientePagadorCpf(String clientePagadorCpf) {
        this.clientePagadorCpf = clientePagadorCpf;
    }

    public String getClientePagadorTelefone() {
        return clientePagadorTelefone;
    }

    public void setClientePagadorTelefone(String clientePagadorTelefone) {
        this.clientePagadorTelefone = clientePagadorTelefone;
    }

    public List<PagamentoIngressoPosseRequest> getPosses() {
        return posses;
    }

    public void setPosses(List<PagamentoIngressoPosseRequest> posses) {
        this.posses = posses;
    }

    /**/
    public static String getPath() {
        return "/PontoVenda/Pagamento/PdvFisico";
    }
    public static String getPathEstorno() {
        return "/PontoVenda/Pagamento/";
    }
    public void estornar(String codigoPagemento, HttpRequestListener<String> listener) {

        //Gson gson = new GsonBuilder().create();
        //String boby = gson.toJson(pagamentoRequest);

        String url = HttpClient.getUrlBase() + this.getPathEstorno() + codigoPagemento;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());
        HttpGsonRequest myReq = new HttpGsonRequest(Request.Method.DELETE, url, null, listener, headers);
        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
    }
    public void getRequest(PagamentoRequest pagamentoRequest, HttpRequestListener<PagamentoResponse> listener) {

        Gson gson = new GsonBuilder().create();
        String boby = gson.toJson(pagamentoRequest);

        String url = HttpClient.getUrlBase() + this.getPath();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", "Bearer " + HttpClient.getBearer());
        HttpGsonRequest<PagamentoResponse> myReq = new HttpGsonRequest<PagamentoResponse>(Request.Method.POST, url, PagamentoResponse.getType(), listener, headers, boby);

        RequestQueue queue = HttpClient.getRequestQueue();
        queue.add(myReq);
    }
}
