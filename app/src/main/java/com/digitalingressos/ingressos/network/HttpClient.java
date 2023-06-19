package com.digitalingressos.ingressos.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.digitalingressos.ingressos.R;

public class HttpClient {
    private static RequestQueue mRequestQueue;
    private static String urlbase;
    private static String bearer;
    public static String getUrlBase() {
        return urlbase;
    }

    public static String getBearer() {
        return bearer;
    }

    public static void setBearer(String mbearer) {
        bearer = mbearer;
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        urlbase = context.getResources().getString(R.string.url_desenvolvimento);
        bearer = context.getResources().getString(R.string.bearer);
    }


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }
}