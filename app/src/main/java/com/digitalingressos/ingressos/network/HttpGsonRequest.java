package com.digitalingressos.ingressos.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class HttpGsonRequest<T> extends Request<T> {
    //private final Gson gson;
    private final Type type;
    private final HttpRequestListener<T> listenerCustom;
    private final Response.Listener<T> listener;
    private final Map<String, String> headers;
    private String parameters;

    /**
     * Faz a requisicao e retorna um objeto JSON.
     * authorization implicito
     *
     * @param method   Metodo de requisicao (POST, GET, PUT)
     * @param url      URL para a requisição
     * @param type     tipo do to objeto para retorno
     * @param listener listener para o callback
     */
    public HttpGsonRequest(int method, String url, Type type, HttpRequestListener<T> listener) {
        super(method, url, HttpGsonRequest.errorListener(listener));
        //this.gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
        this.type = type;
        this.listenerCustom = listener;
        this.listener = null;
        this.headers = new HashMap<String, String>();
        this.headers.put("authorization", "Bearer " + HttpClient.getBearer());
    }

    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization implicito
     *
     * @param method     Metodo de requisicao (POST, GET, PUT)
     * @param url        URL para a requisição
     * @param type       tipo do to objeto para retorno
     * @param listener   listener para o callback
     * @param parameters parametros para o boby
     */
    public HttpGsonRequest(int method, String url, Type type, HttpRequestListener<T> listener, String parameters) {
        this(method, url, type, listener);
        this.parameters = parameters;
    }

    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization deve ser enviado no header
     *
     * @param method   Metodo de requisicao (POST, GET, PUT)
     * @param url      URL para a requisição
     * @param type     tipo do to objeto para retorno
     * @param listener listener para o callback
     * @param headers  Headers da requisicao
     */
    public HttpGsonRequest(int method, String url, Type type, HttpRequestListener<T> listener, Map<String, String> headers) {
        super(method, url, HttpGsonRequest.errorListener(listener));
        //this.gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
        this.type = type;
        this.listenerCustom = listener;
        this.listener = null;
        this.headers = headers;
    }

    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization deve ser enviado no header
     *
     * @param method     Metodo de requisicao (POST, GET, PUT)
     * @param url        URL para a requisição
     * @param type       tipo do to objeto para retorno
     * @param listener   listener para o callback
     * @param headers    Headers da requisicao
     * @param parameters parametros para o boby
     */

    public HttpGsonRequest(int method, String url, Type type, HttpRequestListener<T> listener, Map<String, String> headers, String parameters) {
        this(method, url, type, listener, headers);
        this.parameters = parameters;
    }


    /**/

    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization deve ser enviado no header
     *
     * @param method        Metodo de requisicao (POST, GET, PUT)
     * @param url           URL para a requisição
     * @param type          tipo do to objeto para retorno
     * @param errorListener listener no erro para o callback
     * @param headers       Headers da requisicao
     */
    public HttpGsonRequest(int method, String url, Type type, Response.ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, errorListener);
        this.type = type;
        //this.gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
        this.listener = null;
        this.parameters = null;
        this.listenerCustom = null;
        this.headers = headers;

    }

    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization deve ser enviado no header
     * O Listener de erro é tratodo pelo invador
     *
     * @param method        Metodo de requisicao (POST, GET, PUT)
     * @param url           URL para a requisição
     * @param type          tipo do to objeto para retorno
     * @param errorListener listener no erro para o callback
     * @param headers       Headers da requisicao
     * @param parameters    parametros para o boby
     */

    public HttpGsonRequest(int method, String url, Type type, Response.ErrorListener errorListener, Map<String, String> headers, String parameters) {
        this(method, url, type, errorListener, headers);
        this.parameters = parameters;

    }


    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization deve ser enviado no header
     * O Listener de erro é tratodo pelo invocador
     *
     * @param method        Metodo de requisicao (POST, GET, PUT)
     * @param url           URL para a requisição
     * @param type          tipo do to objeto para retorno
     * @param listener      listener de sucesso
     * @param errorListener listener no erro para o callback
     * @param headers       Headers da requisicao
     */
    public HttpGsonRequest(int method, String url, Type type, Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, errorListener);
        this.type = type;
        //this.gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
        this.listener = listener;
        this.headers = headers;
        this.parameters = null;
        this.listenerCustom = null;
    }

    /**
     * Faz a requisicao e retorna um objeto JSON
     * authorization deve ser enviado no header
     * O Listener de erro é tratado pelo invocador
     * O listener de sucesso é tratado pelo invocador
     *
     * @param method        Metodo de requisicao (POST, GET, PUT)
     * @param url           URL para a requisição
     * @param type          tipo do to objeto para retorno
     * @param listener      listener de sucesso
     * @param errorListener listener no erro para o callback
     * @param headers       Headers da requisicao
     * @param parameters    parametros para o boby
     */
    public HttpGsonRequest(int method, String url, Type type, Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers, String parameters) {
        this(method, url, type, listener, errorListener, headers);
        this.parameters = parameters;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return parameters.getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            //DLog.d(e);
        }
        return null;
    }

    private static Response.ErrorListener errorListener(HttpRequestListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        IngressosApplication.setAuthHasExpired();
                        String msg = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        listener.onError(error.networkResponse.statusCode, "Login Expirou!");
                        return;
                    }
                    String msg = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    listener.onError(error.networkResponse.statusCode, msg);
                    return;
                }

                if (error instanceof NetworkError) {
                    try {
                        listener.onError(501, "Ocorreu um erro na rede. Tente novamente mais tarde");
                        return;
                    } catch (NullPointerException npe) {
                        System.err.println(npe);
                        return;
                    }
                } else if (error instanceof ServerError) {
                    try {
                        listener.onError(500, "Ocorreu um problema com o servidor. Tente novamente mais tarde");
                        return;
                    } catch (NullPointerException npe) {
                        System.err.println(npe);
                        return;
                    }
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                    try {
                        listener.onError(502, "Sem conexão. Verifique a conexão do aparelho");
                        return;
                    } catch (NullPointerException npe) {
                        System.err.println(npe);
                        return;
                    }
                } else if (error instanceof TimeoutError) {
                    try {
                        listener.onError(504, "Tempo expirou.  Tente novamente mais tarde");
                        return;
                    } catch (NullPointerException npe) {
                        System.err.println(npe);
                        return;
                    }
                }
                listener.onError(999, "Ocorreu um erro desconhecido");
            }
        };
    }

    @Override
    protected void deliverResponse(T response) {
        if (listenerCustom != null) {
            listenerCustom.onSuccess(response);
        }

        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            if (type != null) {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Log.i("JSON", json);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

                return (Response<T>) Response.success
                        (gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response)
                        );
            } else {
                return (Response<T>) Response.success("", HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}