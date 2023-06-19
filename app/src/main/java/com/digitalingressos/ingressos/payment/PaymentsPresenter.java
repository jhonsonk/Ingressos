package com.digitalingressos.ingressos.payment;


import com.digitalingressos.ingressos.R;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import javax.inject.Inject;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagEventData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PaymentsPresenter extends MvpNullObjectBasePresenter<PaymentsContract> {

    private final PaymentsUseCase mUseCase;
    private Disposable mSubscribe;
    private int countPassword = 0;
    private int erroPassword = 3;

    @Inject
    public PaymentsPresenter(PaymentsUseCase useCase) {
        mUseCase = useCase;
    }

    public void doDebitPayment(int value) {
        doAction(mUseCase.doDebitPayment(value, false), value);
    }

    public void doCreditPaymentInCash(int value) {
        doAction(mUseCase.doCreditPayment(value, false), value);
    }

    public void doCreditPaymentBuyerInstallments(int value, int installments) {
        doAction(mUseCase.doCreditPaymentBuyerInstallments(value, installments), value);
    }

    public void doCreditPaymentSellerInstallments(int value, int installments) {
        doAction(mUseCase.doCreditPaymentSellerInstallments(value, installments), value);
    }

    public void doRefund(ActionResult actionResult) {
        if (actionResult.getMessage() != null) {
            getView().showTransactionDialog(actionResult.getMessage());
        } else {
            doAction(mUseCase.doRefund(actionResult), 0);
        }
    }


    public void doPixPayment(int value) {
        doAction(mUseCase.doPixPayment(value), value);
    }


    //nesse trecho realiza
    private void doAction(Observable<ActionResult> action, int value) {
        mSubscribe = action
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> getView().showTransactionSuccess())
                .doOnDispose(() -> getView().disposePayment())
                .subscribe((ActionResult result) -> {
                            writeToFile(result);
                            updateValue(result.getTransactionResult());
                            transactionResult(result.getTransactionResult());
                            checkReturnPinpad(result.getEventCode(), value, result.getMessage());//ESTOU COLOCANDO AGORA
                        },
                        throwable -> {
                            getView().setTransactionAproved(false);
                            getView().setTransactionRejected(true);
                            getView().showUnauthorizedExceptionDialog(throwable.getMessage());
                            getView().disposePayment();
                        });
    }

    //CHECK O PASSWORD
    private void checkPassword(int eventCode, int value) {
        StringBuilder strPassword = new StringBuilder();

        if (eventCode == PlugPagEventData.EVENT_CODE_DIGIT_PASSWORD) {
            countPassword++;
        }
        if (eventCode == PlugPagEventData.EVENT_CODE_NO_PASSWORD) {
            countPassword = 0;
        }

        for (int count = countPassword; count > 0; count--) {
            strPassword.append("*");
        }
        getView().showPasswordDialog(String.format("%s", strPassword));
    }

    private String checkMessage(String message) {
        if (message != null && message.contains("SENHA INCORRETA TENTATIVAS")) {
            erroPassword--;
            return "SENHA INCORRETA " + erroPassword + " TENTATIVAS";
        }
        return message;
    }

    private void checkReturnPinpad(int eventCode, int value, String message) {
        message = checkMessage(message);
        switch (eventCode) {
            default:
            case -2://PROCESSANDO
            case -1://PROCESSANDO
            case 1://CARTAO INSERIDO
                getView().showWaitDialog(message);
                break;
            case 0://INSIRA OU PASSE
                getView().showInsertCardDialog();
                break;
            case 17://SENHA
            case 16:
                checkPassword(eventCode, value);
                break;
            case 3://SENHA DIGITDA
                getView().setTransactionInitProcessPayment(true);
            case 5://PROCESSANDO PAGAMENTO
                getView().showProcessPaymentDialog();
                break;
            case 7://REMOVER CARTAO
                getView().showRemoveCardDialog();
                break;
            case 18://AUTORIZADO
                //SET AUTORIZADO
                getView().setTransactionAproved(true);
                getView().setTransactionRejected(false);
                getView().showAuthorizedDialog();
                break;
            case 4://PAGAMENTO FINALIZADO
            case 8://CARTAO REMOVIDO
                break;
            case 19://NAO AUTORIZADO
                getView().setTransactionAproved(false);
                getView().setTransactionRejected(true);
                getView().showUnauthorizedDialog();
                break;
        }
    }


    //grava a transacao em um arquivo
    private void writeToFile(ActionResult result) {
        if (result.getTransactionCode() != null && result.getTransactionId() != null) {
            getView().writeToFile(result.getTransactionCode(), result.getTransactionId());
        }
    }

    //Atualiza o valor
    private void updateValue(PlugPagTransactionResult result) {
        if (result != null && result.getResult() == PlugPag.RET_OK && result.getPartialPayRemainingAmount() != null && !result.getPartialPayRemainingAmount().isEmpty()) {
            getView().setPaymentValue(result.getPartialPayRemainingAmount());
        }
    }

    ////
    private void transactionResult(PlugPagTransactionResult result) {
        if (result != null && result.getResult() == PlugPag.RET_OK) {
            getView().setTransactionResult(result);
        }
    }

    public void abortTransaction() {
        mSubscribe = mUseCase.abort()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getView().disposePayment())
                .subscribe();
    }


    @Override
    public void detachView(boolean retainInstance) {
        if (mSubscribe != null) {
            mSubscribe.dispose();
        }
        super.detachView(retainInstance);
    }

    //PARCELAS
    public void calculateInstallments(Integer saleValue, Integer installmentType) {
        mSubscribe = mUseCase.calculateInstallments(saleValue, installmentType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getView().showLoading(true))
                .doFinally(() -> getView().showLoading(false))
                .subscribe(
                        installments ->
                                getView().setUpAdapter(installments),
                        throwable -> getView().showMessage(throwable.getMessage()));
    }

    public void getLastTransaction() {
        mSubscribe = mUseCase.getLastTransaction()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(actionResult -> getView().showTransactionDialog(actionResult.getTransactionCode()),
                        throwable -> getView().showTransactionDialog(throwable.getMessage()));
    }

    public void printStablishmentReceipt() {
        mSubscribe = mUseCase.reprintStablishmentReceipt()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> getView().printReceiptSuccess())
                .doOnSubscribe(disposable -> getView().showLoading(true, "Reimprimindo via estabelecimento"))
                .subscribe(message -> getView().showMessage(message.getMessage()),
                        throwable -> getView().showMessage(throwable.getMessage()));
    }

    public void printCustomerReceipt() {
        mSubscribe = mUseCase.reprintCustomerReceipt()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> getView().printReceiptSuccess())
                .doOnSubscribe(disposable -> getView().showLoading(true, "Reimprimindo via cliente"))
                .doOnError(throwable -> getView().showMessage(throwable.getMessage()))
                .subscribe(message -> getView().showMessage(message.getMessage()),
                        throwable -> getView().showMessage(throwable.getMessage()));
    }
}
