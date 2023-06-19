package com.digitalingressos.ingressos.payment;

import dagger.Component;

@Component(modules = {UseCaseModule.class, WrapperModule.class})
public interface PaymentsComponent {

    void inject(PaymentsActivity activity);

    PaymentsPresenter presenter();
}
