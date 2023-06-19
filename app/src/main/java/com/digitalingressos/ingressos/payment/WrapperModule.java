package com.digitalingressos.ingressos.payment;


import android.content.Context;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import dagger.Module;
import dagger.Provides;

@Module
public class WrapperModule {

    private final Context mContext;

    public WrapperModule(Context context) {
        mContext = context;
    }

    @Provides
    PlugPag providesPlugPag() {
        return new PlugPag(mContext);
    }
}