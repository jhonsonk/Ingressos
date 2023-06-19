package com.digitalingressos.ingressos.network;

public interface HttpRequestListener<T> {
    void onSuccess(T response);

    void onError(int statusCode, String mensagem);
}