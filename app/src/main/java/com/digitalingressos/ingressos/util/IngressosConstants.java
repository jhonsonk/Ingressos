package com.digitalingressos.ingressos.util;

import com.digitalingressos.ingressos.R;

public class IngressosConstants {
    public static final String USER_REFERENCE = "Digital";

    //Payments Constants
    public static final int TYPE_CREDITO = 1;
    public static final int TYPE_DEBITO = 2;
    public static final int TYPE_VOUCHER = 3;
    public static final int TYPE_QRCODE_DEBITO = 4;
    public static final int TYPE_PIX = 5;
    public static final int TYPE_QRCODE_CREDITO = 7;
    public static final int VALUE_MINIMAL_INSTALLMENT = 1000;
    public static final int INSTALLMENT_TYPE_A_VISTA = 1;
    public static final int INSTALLMENT_TYPE_PARC_VENDEDOR = 2;
    public static final int INSTALLMENT_TYPE_PARC_COMPRADOR = 3;
    public static final String CREDIT_VALUE = "valueCredit";
    public static final String CARD_METODO = "CardMetodo";
    public static final String PAYMENT_CARD_MESSAGE = "Aproxime, insira ou passe o cartão";

    //NFC Constants
    public static final int NFC_OK = 1;
    public static final int RET_WAITING_REMOVE_CARD = 2;
    public static final String NFC_START_FAIL = "Ocorreu um erro ao iniciar o serviço nfc: ";
    public static final String APDU_COMMAND_FAIL = "Ocorreu um erro no comando APDU: ";
    public static final String WAITING_REMOVE_CARD = "Aguardando a remoção do cartão...";
    public static final String REMOVED_CARD = "Cartão removido com sucesso";
    public static final String CARD_NOT_REMOVED = "Cartão removido com sucesso";
    public static final String AUTH_CARD_SUCCESS = "Cartão autenticado com sucesso";
    public static final String AUTH_BLOCK_B_CARD_SUCCESS = "Autenticado com sucesso com o cartão bloco B";
    public static final String CARD_DETECTED_SUCCESS = "Cartão detectado com sucesso - cid: ";
    public static final String VALUE_RESULT = "Valor do result: ";
    public static final String NO_NEAR_FIELD_FOUND = "No Near Field Card found";
    public static final String CARD_NOT_FOUND = "Cartão não identificado ";
    public static final String TEST_16_BYTES = "teste_com16bytes";

    //LED and Beep Constants
    public static final String LED_ON_SUCCESS = "Led ligado com sucesso";
    public static final String LED_OFF_SUCCESS = "Led desligado com sucesso";
    public static final String LED_FAIL = "Não foi possível setar o led";
    public static final String BEEP_SUCCESS = "Beep realizado com sucesso";
    public static final String BEEP_FAIL = "Não foi possível realizar o beep";

    //PlugPagStyle Constants
    public static final int HEAD_TEXT_COLOR = 0xFF6ea8fe;
    public static final int HEAD_BACKGROUND_COLOR = 0xFF6ea8fe;
    public static final int CONTENT_TEXT_COLOR = 0xFF6ea8fe;
    public static final int CONTENT_TEXT_VALUE_1_COLOR = 0xFF6ea8fe;
    public static final int CONTENT_TEXT_VALUE_2_COLOR = 0xFF6ea8fe;
    public static final int POSITIVE_BUTTON_TEXT_COLOR = 0xFF6ea8fe;
    public static final int POSITIVE_BUTTON_BACKGROUND = 0xFF6ea8fe;
    public static final int NEGATIVE_BUTTON_TEXT_COLOR = 0xFF6ea8fe;
    public static final int NEGATIVE_BUTTON_BACKGROUND = 0xFF6ea8fe;
    public static final int LINE_COLOR = 0xFF6ea8fe;

    //Payment
    public static final String PAYMENT_ERROR_CODE = "errorCode";
    public static final String PAYMENT_TRANSACTION_CODE = "transactionCode";
    public static final String PAYMENT_TRANSACTION_ID = "transactionId";
    public static final String PAYMENT_DATE = "date";
    public static final String PAYMENT_TIME = "time";
    public static final String PAYMENT_HOST_NSU = "hostNsu";
    public static final String PAYMENT_CARD_BRAND = "cardBrand";
    public static final String PAYMENT_BIN = "bin";
    public static final String PAYMENT_HOLDER = "holder";
    public static final String PAYMENT_TERMINAL_SERIAL_NUMBER = "terminalSerialNumber";
    public static final String PAYMENT_AMOUNT = "amount";
    public static final String PAYMENT_LABEL = "label";
    public static final String PAYMENT_READER_MODEL = "A930";
    public static final String PAYMENT_NSU = "nsu";
    public static final String PAYMENT_AUTO_CODE = "autoCode";
    public static final String PAYMENT_INSTALMENTS = "installments";
    public static final String PAYMENT_ORIGINAL_AMOUNT = "originalAmount";
    public static final String PAYMENT_TYPE_TRANSACTION = "typeTransaction";

    public static final String PAYMENT_PRINT_RECEIPT = "printReceipt";

}
