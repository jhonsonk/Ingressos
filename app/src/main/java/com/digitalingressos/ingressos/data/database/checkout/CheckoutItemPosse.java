package com.digitalingressos.ingressos.data.database.checkout;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CheckoutItemPosse {

    @SerializedName("BilheteCodigo")
    public String mBilheteCodigo;

    public  CheckoutItemPosse (String bilheteCodigo){
        this.mBilheteCodigo = bilheteCodigo;
    }
    public static Type getListType() {
        return new TypeToken<List<CheckoutItemPosse>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<CheckoutItemPosse>() {
        }.getType();
    }
}
