package com.digitalingressos.ingressos.payment;

import static com.digitalingressos.ingressos.util.IngressosConstants.INSTALLMENT_TYPE_A_VISTA;
import static com.digitalingressos.ingressos.util.IngressosConstants.INSTALLMENT_TYPE_PARC_COMPRADOR;
import static com.digitalingressos.ingressos.util.IngressosConstants.INSTALLMENT_TYPE_PARC_VENDEDOR;
import static com.digitalingressos.ingressos.util.IngressosConstants.TYPE_CREDITO;
import static com.digitalingressos.ingressos.util.IngressosConstants.TYPE_DEBITO;
import static com.digitalingressos.ingressos.util.IngressosConstants.TYPE_PIX;
import static com.digitalingressos.ingressos.util.IngressosConstants.TYPE_QRCODE_CREDITO;
import static com.digitalingressos.ingressos.util.IngressosConstants.TYPE_QRCODE_DEBITO;
import static com.digitalingressos.ingressos.util.IngressosConstants.TYPE_VOUCHER;
import static com.digitalingressos.ingressos.util.IngressosConstants.USER_REFERENCE;

import java.util.List;
import java.util.Locale;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagCardInfoResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagCustomPrinterLayout;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInstallment;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterListener;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagVoidData;
import br.com.uol.pagseguro.plugpagservice.wrapper.exception.PlugPagException;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPaymentData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;


public class PaymentsUseCase {
    private final PlugPag mPlugPag;
    private PlugPagPaymentData mPlugPagPaymentData = null;
    public PaymentsUseCase(PlugPag plugPag) {
        mPlugPag = plugPag;
    }
    private static final String CUSTOM_PRINT_MESSAGE = "Imprimir via do cliente?";
    private final int VOID_QRCODE = 2;

    //Payment Methods
    public Observable<ActionResult> doDebitPayment(int value, boolean isCarne) {
        return doPayment(new PlugPagPaymentData(
                TYPE_DEBITO,
                value,
                INSTALLMENT_TYPE_A_VISTA,
                1,
                USER_REFERENCE,
                true,
                false,
                isCarne));
    }

    public Observable<ActionResult> doCreditPayment(int value, boolean isCarne) {
        return doPayment(new PlugPagPaymentData(
                TYPE_CREDITO,
                value,
                INSTALLMENT_TYPE_A_VISTA,
                1,
                USER_REFERENCE,
                true,
                true,
                isCarne
        ));
    }

    public Observable<ActionResult> doCreditPaymentBuyerInstallments(int value, int installments) {
        return doPayment(new PlugPagPaymentData(
                TYPE_CREDITO,
                value,
                INSTALLMENT_TYPE_PARC_COMPRADOR,
                installments,
                USER_REFERENCE,
                true,
                false,
                false
        ));
    }

    public Observable<ActionResult> doCreditPaymentSellerInstallments(int value, int installments) {
        return doPayment(new PlugPagPaymentData(
                TYPE_CREDITO,
                value,
                INSTALLMENT_TYPE_PARC_VENDEDOR,
                installments,
                USER_REFERENCE,
                true,
                false,
                false
        ));
    }

    public Observable<ActionResult> doVoucherPayment(int value) {
        return doPayment(new PlugPagPaymentData(
                TYPE_VOUCHER,
                value,
                INSTALLMENT_TYPE_A_VISTA,
                1,
                USER_REFERENCE,
                true));
    }

    public Observable<ActionResult> doPixPayment(int value) {
        return doPayment(new PlugPagPaymentData(
                TYPE_PIX,
                value,
                INSTALLMENT_TYPE_A_VISTA,
                1,
                USER_REFERENCE,
                true,
                false,
                false
        ));
    }

    public Observable<ActionResult> doQRCodePaymentInCashDebit(int value) {
        return doPayment(new PlugPagPaymentData(
                TYPE_QRCODE_DEBITO,
                value,
                INSTALLMENT_TYPE_A_VISTA,
                1,
                USER_REFERENCE,
                true,
                false,
                false
        ));
    }

    public Observable<ActionResult> doQRCodePaymentInCashCredit(int value) {
        return doPayment(new PlugPagPaymentData(
                TYPE_QRCODE_CREDITO,
                value,
                INSTALLMENT_TYPE_A_VISTA,
                1,
                USER_REFERENCE,
                true,
                true,
                false
        ));
    }

    public Observable<ActionResult> doQRCodePaymentBuyerInstallments(int value, int installments) {
        return doPayment(new PlugPagPaymentData(
                TYPE_QRCODE_CREDITO,
                value,
                INSTALLMENT_TYPE_PARC_COMPRADOR,
                installments,
                USER_REFERENCE,
                true,
                false,
                false
        ));
    }

    public Observable<ActionResult> doQRCodePaymentSellerInstallments(int value, int installments) {
        return doPayment(new PlugPagPaymentData(
                TYPE_QRCODE_CREDITO,
                value,
                INSTALLMENT_TYPE_PARC_VENDEDOR,
                installments,
                USER_REFERENCE,
                true,
                false,
                false
        ));
    }

    // Payment and Refund Implementation
    private Observable<ActionResult> doPayment(final PlugPagPaymentData paymentData) {
        mPlugPagPaymentData = paymentData;
        return Observable.create(emitter -> {
            ActionResult result = new ActionResult();
            setEventListener(emitter, result);
            //setStyle();
            mPlugPag.setPlugPagCustomPrinterLayout(getCustomPrinterDialog());
            PlugPagTransactionResult plugPagTransactionResult = mPlugPag.doPayment(paymentData);
            sendTransactionResponse(emitter, plugPagTransactionResult, result);
        });
    }

    public Observable<ActionResult> doRefund(ActionResult transaction) {
        return Observable.create(emitter -> {
            ActionResult actionResult = new ActionResult();
            setEventListener(emitter, actionResult);
            PlugPagTransactionResult result = mPlugPag.voidPayment(
                    new PlugPagVoidData(
                            transaction.getTransactionCode(),
                            transaction.getTransactionId(),
                            true
                    )
            );

            sendTransactionResponse(emitter, result, actionResult);
        });
    }

    public Observable<ActionResult> doRefundQrCode(ActionResult transaction) {
        return Observable.create(emitter -> {
            ActionResult actionResult = new ActionResult();
            setEventListener(emitter, actionResult);
            PlugPagTransactionResult result = mPlugPag.voidPayment(
                    new PlugPagVoidData(
                            transaction.getTransactionCode(),
                            transaction.getTransactionId(),
                            true,
                            VOID_QRCODE
                    )
            );

            sendTransactionResponse(emitter, result, actionResult);
        });
    }

    private void sendTransactionResponse(
            ObservableEmitter<ActionResult> emitter,
            PlugPagTransactionResult plugPagTransactionResult,
            ActionResult result
    ) {
        if (plugPagTransactionResult.getResult() != 0) {
            emitter.onError(
                    new PlugPagException(
                            plugPagTransactionResult.getMessage(),
                            plugPagTransactionResult.getErrorCode()
                    )
            );
        } else {
            result.setTransactionCode(plugPagTransactionResult.getTransactionCode());
            result.setTransactionId(plugPagTransactionResult.getTransactionId());
            result.setTransactionResult(plugPagTransactionResult);
            emitter.onNext(result);
        }
        emitter.onComplete();
    }

    private void sendPrintResponse(
            ObservableEmitter<ActionResult> emitter,
            PlugPagPrintResult printResult,
            ActionResult result
    ) {
        if (printResult.getResult() != 0) {
            result.setResult(printResult.getResult());
        }
        emitter.onComplete();
    }

    private void sendCardResponse(
            ObservableEmitter<ActionResult> emitter,
            PlugPagCardInfoResult cardResult,
            ActionResult result
    ) {
        if (cardResult.getResult() != null && !"0".equals(cardResult.getResult()) ||
                cardResult.getMessage() != null && !cardResult.getMessage().isEmpty()) {
            result.setMessage(cardResult.getMessage());
        } else {
            result.setMessage(
                    "BIN: " + cardResult.getBin() + "\n" +
                            "Holder: " + cardResult.getHolder() + "\n" +
                            "CardHolder: " + cardResult.getCardHolder()
            );
        }

        emitter.onNext(result);
        emitter.onComplete();
    }

    private void setEventListener(ObservableEmitter<ActionResult> emitter, ActionResult result) {
        mPlugPag.setEventListener(plugPagEventData -> {
            result.setEventCode(plugPagEventData.getEventCode());
            result.setMessage(plugPagEventData.getCustomMessage());
            emitter.onNext(result);
        });
    }

    public Completable abort() {
        return Completable.create(emitter -> mPlugPag.abort());
    }

    public Observable<ActionResult> getLastTransaction() {
        return Observable.create(emitter -> {
            ActionResult actionResult = new ActionResult();

            PlugPagTransactionResult result = mPlugPag.getLastApprovedTransaction();

            sendTransactionResponse(emitter, result, actionResult);
        });
    }

    public PlugPagPaymentData getEventPaymentData() {
        return mPlugPagPaymentData;
    }

    public Observable<ActionResult> getCardData() {
        return Observable.create(emitter -> {
            ActionResult action = new ActionResult();
            PlugPagCardInfoResult result = mPlugPag.getCardData();
            sendCardResponse(emitter, result, action);
        });
    }

    //Printer
    public Observable<ActionResult> reprintCustomerReceipt() {
        return Observable.create(emitter -> {
            ActionResult actionResult = new ActionResult();
            setPrintListener(emitter, actionResult);
            PlugPagPrintResult result = mPlugPag.reprintCustomerReceipt();
            sendPrintResponse(emitter, result, actionResult);
        });
    }

    //reimprimivir
    public Observable<ActionResult> reprintStablishmentReceipt() {
        return Observable.create(emitter -> {
            ActionResult actionResult = new ActionResult();
            setPrintListener(emitter, actionResult);
            PlugPagPrintResult result = mPlugPag.reprintStablishmentReceipt();
            sendPrintResponse(emitter, result, actionResult);
        });
    }

    private void setPrintListener(ObservableEmitter<ActionResult> emitter, ActionResult result) {
        mPlugPag.setPrinterListener(new PlugPagPrinterListener() {
            @Override
            public void onError(PlugPagPrintResult printResult) {
                result.setResult(printResult.getResult());
                result.setMessage(String.format("Error %s %s", printResult.getErrorCode(), printResult.getMessage()));
                result.setErrorCode(printResult.getErrorCode());
                emitter.onNext(result);
            }

            @Override
            public void onSuccess(PlugPagPrintResult printResult) {
                result.setResult(printResult.getResult());
                result.setMessage(String.format(Locale.getDefault(), "Print OK: Steps [%d]", printResult.getSteps()));
                result.setErrorCode(printResult.getErrorCode());
                emitter.onNext(result);
            }
        });
    }

    //Custom Printer Layouts
    public PlugPagCustomPrinterLayout getCustomPrinterDialog() {
        PlugPagCustomPrinterLayout customDialog = new PlugPagCustomPrinterLayout();
        customDialog.setTitle(CUSTOM_PRINT_MESSAGE);
        customDialog.setMaxTimeShowPopup(30);
        return customDialog;
    }

    //PARCELAMENTO
    public Observable<List<PlugPagInstallment>> calculateInstallments(
            Integer saleValue,
            Integer transactionType
    ) {
        return Observable.create(emitter -> {
            List<PlugPagInstallment> result;

            try {
                if (transactionType.equals(INSTALLMENT_TYPE_PARC_VENDEDOR)) {
                    result = calculateInstallmentsSeller(saleValue);
                } else {
                    result = calculateInstallmentsBuyer(saleValue);
                }
                emitter.onNext(result);
            } catch (PlugPagException exception) {
                emitter.onError(exception);
            }
            emitter.onComplete();
        });
    }

    public List<PlugPagInstallment> calculateInstallmentsSeller(Integer saleValue) {
        return mPlugPag.calculateInstallments(saleValue.toString(), PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR);
    }

    public List<PlugPagInstallment> calculateInstallmentsBuyer(Integer saleValue) {
        return mPlugPag.calculateInstallments(saleValue.toString(), PlugPag.INSTALLMENT_TYPE_PARC_COMPRADOR);
    }
}
