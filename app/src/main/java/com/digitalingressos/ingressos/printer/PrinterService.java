package com.digitalingressos.ingressos.printer;

import android.content.Context;
import android.graphics.Bitmap;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.util.QrCode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrinterService {

    Context ctx;
    PrinterDevice printerDevice;

    public PrinterService(Context _ctx) {
        ctx = _ctx;
        printerDevice = new PrinterDevice(ctx);
        printerDevice.init();
    }

    public void printCorteAqui() {
        printerDevice.printText("------------Corte Aqui----------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
    }

    public void printFechamentoCaixa(Caixa mCaixa) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        printerDevice.printNewLine();
        printerDevice.printPhoto(R.drawable.digital_impresso_grande);
        printerDevice.printText("FECHAMENTO DE CAIXA", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printNewLine();
        printerDevice.printText("Data de Abertura:", PrinterFontSize.NORMAL, PrinterAlign.LEFT);

        printerDevice.printText(sdf.format(mCaixa.getAbertoEm()), PrinterFontSize.NORMAL, PrinterAlign.RIGHT);

        printerDevice.printText("Data de Fechamento:", PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText(sdf.format(mCaixa.getFechadoEm()), PrinterFontSize.NORMAL, PrinterAlign.RIGHT);

        printerDevice.printText(printerDevice.leftRightAlign("NOME USUARIO:", mCaixa.getAuth().getPdvNome()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText(printerDevice.leftRightAlign("CODIGO PDV:", mCaixa.getAuth().getPdvEmpresa()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);

        printerDevice.printNewLine();
        printerDevice.printText("________________________________", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText(printerDevice.centerAlign("MEIO PAG.", "QTD", "VALOR"), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText("________________________________", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText(printerDevice.centerAlign("DINHEIRO", String.valueOf(mCaixa.getQuantidadeVendasDebito()), mCaixa.getTotalVendasDinheiroMonetary()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText(printerDevice.centerAlign("CREDITO", String.valueOf(mCaixa.getQuantidadeVendasCredito()), mCaixa.getTotalVendasCreditoMonetary()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText(printerDevice.centerAlign("DEBITO", String.valueOf(mCaixa.getQuantidadeVendasDebito()), mCaixa.getTotalVendasDebitoMonetary()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText(printerDevice.centerAlign("PIX", String.valueOf(mCaixa.getQuantidadeVendasPix()), mCaixa.getTotalVendasPixMonetary()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText("_______________________________", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText(printerDevice.centerAlign("TOTAL", String.valueOf(mCaixa.getQuantidadeTotalVendas()), mCaixa.getTotalVendasMonetary()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText("_______________________________", PrinterFontSize.NORMAL, PrinterAlign.CENTER);

        printerDevice.printText(mCaixa.getUuid().replace("-", ""), PrinterFontSize.NORMAL, PrinterAlign.CENTER);

        printerDevice.printText("", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText(sdf.format(new Date()), PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText("_______________________________", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printNewLine();
        printerDevice.printNewLine();

    }

    public void printBilhete(PrinterBilhete bilhete) {

        printerDevice.printNewLine();
        printerDevice.printPhoto(bilhete.getEventoLogo());
        printerDevice.printNewLine();
        printerDevice.printText(bilhete.getEventoNome(), PrinterFontSize.BIG, PrinterAlign.CENTER);//Nome o evento
        printerDevice.printNewLine();
        printerDevice.printText(bilhete.getEventoData() + " as " + bilhete.getEventoHora(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Data e hora
        printerDevice.printNewLine();
        printerDevice.printText(bilhete.eventoLocal, PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Local do evento
        printerDevice.printText(bilhete.eventoCidade + " - " + bilhete.eventoEstado, PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Cidade e estado
        printerDevice.printNewLine();
        printerDevice.printText(bilhete.getBilheteNome(), PrinterFontSize.BIG, PrinterAlign.CENTER);//Nome do Ingresso
        printerDevice.printText(bilhete.getBilheteTipo(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Tipo ingresso
        printerDevice.printText(bilhete.getBilheteSexo(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//SEXO
        printerDevice.printNewLine();
        printerDevice.printText("Valor", PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Valor do ingresso
        printerDevice.printText(bilhete.getBilheteValor(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Valor do ingresso
        printerDevice.printNewLine();

        byte[] bitmapbyte = QrCode.createQRToBytes(bilhete.getBilheteCodigo(), 350);

        printerDevice.printPhoto(bitmapbyte);

        printerDevice.printText(bilhete.getBilheteCodigo(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//codigo do bilhete
        printerDevice.printText("CPF" + bilhete.getBilheteCpf(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//CPF
        printerDevice.printText("--------------------------------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText("-----------" + bilhete.getBilheteCombo() + "-----------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);//COMBO
        printerDevice.printText("--------------------------------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText(bilhete.getVendaData() + " " + bilhete.getVendaHora() + " TRN:" + bilhete.getVendaTransacao(), PrinterFontSize.NORMAL, PrinterAlign.CENTER);//Data de hora de transacao
        printerDevice.printText("PDV:" + bilhete.getPdvNome(), PrinterFontSize.NORMAL, PrinterAlign.LEFT);//nome do pdv
        printerDevice.printText(printerDevice.leftRightAlign("Forma de Pagamento:", bilhete.getVendaFormaPagamento()), PrinterFontSize.NORMAL, PrinterAlign.LEFT);
        printerDevice.printText(bilhete.getVendaCodigoPagamento(), PrinterFontSize.NORMAL, PrinterAlign.LEFT);//Codigo de pagamento
        printerDevice.printText("--------------------------------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);

        printerDevice.printText("Acesse:", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText("www.digitalingressos.com.br", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printText("--------------------------------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printPhoto(R.drawable.logo_digital_impresso);
        printerDevice.printText("------------Corte Aqui----------", PrinterFontSize.NORMAL, PrinterAlign.CENTER);
        printerDevice.printNewLine();
        printerDevice.printNewLine();

    }

    public void onDestroy() {
        printerDevice.onDestroy();
    }

}

