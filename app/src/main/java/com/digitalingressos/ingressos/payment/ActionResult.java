package com.digitalingressos.ingressos.payment;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;

///Essa classe fica
public class ActionResult {

    private String transactionCode;
    private String transactionId;
    private String message;
    private String errorCode;
    private int eventCode;
    private int result = 0;

    public ActionResult(String transactionCode, String transactionId) {
        this.transactionCode = transactionCode;
        this.transactionId = transactionId;
    }

    public ActionResult() {
    }

    private PlugPagTransactionResult transactionResult;

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public PlugPagTransactionResult getTransactionResult() {
        return transactionResult;
    }

    public void setTransactionResult(PlugPagTransactionResult transactionResult) {
        this.transactionResult = transactionResult;
    }
}
