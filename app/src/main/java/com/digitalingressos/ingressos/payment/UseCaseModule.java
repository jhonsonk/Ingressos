package com.digitalingressos.ingressos.payment;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {
    @Provides
    PaymentsUseCase providesPaymentsUseCase(PlugPag plugPag) {
        return new PaymentsUseCase(plugPag);
    }
}