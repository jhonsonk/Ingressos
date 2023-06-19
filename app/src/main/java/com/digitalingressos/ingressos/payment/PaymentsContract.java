package com.digitalingressos.ingressos.payment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInstallment;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;

interface PaymentsContract extends MvpView {
    void showTransactionSuccess();

    void showTransactionDialog(String message);

    //PARTE 1
    void showWaitDialog(String message);

    //PARTE 2
    void showInsertCardDialog();

    //PARTE 3
    void showPasswordDialog(String message);

    //PARTE 4
    void showProcessPaymentDialog();

    //PARTE 5
    void showAuthorizedDialog();

    //PARTE 6
    void showUnauthorizedDialog();

    //PARTE 6
    void showRemoveCardDialog();

    void writeToFile(String transactionCode, String transactionId);

    void disposePayment();

    void setPaymentValue(String value);

    void setTransactionResult(PlugPagTransactionResult result);

    void showUnauthorizedExceptionDialog(String message);

    void setTransactionInit(boolean value);

    void setTransactionInitProcessPayment(boolean value);

    void setTransactionAproved(boolean value);

    void setTransactionRejected(boolean value);

    void setUpAdapter(List<PlugPagInstallment> installments);

    void showLoading(Boolean bool);

    void showLoading(Boolean bool, String message);

    void showMessage(String message);

    void printReceiptSuccess();
}
