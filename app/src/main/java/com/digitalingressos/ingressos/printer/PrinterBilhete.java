package com.digitalingressos.ingressos.printer;

import android.graphics.Bitmap;

public class PrinterBilhete {
    private Bitmap eventoLogo;
    public String eventoNome;
    public String eventoLocal;
    public String eventoData;
    public String eventoHora;
    public String eventoCidade;
    public String eventoEstado;

    public String bilheteValor;
    public String bilheteNome;
    public String bilheteSexo;
    public String bilheteTipo;
    public String bilheteCodigo;
    public String bilheteCpf;
    public String bilheteValorUuid;
    public String bilheteCombo;

    public String vendaData;
    public String vendaHora;
    public String vendaTransacao;
    public String vendaFormaPagamento;
    public String vendaCodigoPagamento;
    public String pdvNome;
    public String pdvCodigo;

    public Bitmap getEventoLogo() {
        return eventoLogo;
    }

    public void setEventoLogo(Bitmap eventoLogo) {
        this.eventoLogo = eventoLogo;
    }
    public String getBilheteCodigo() {
        return bilheteCodigo;
    }

    public String getBilheteCombo() {
        return bilheteCombo;
    }

    public String getBilheteCpf() {
        return bilheteCpf;
    }

    public String getBilheteNome() {
        return bilheteNome;
    }

    public String getBilheteSexo() {
        return bilheteSexo;
    }

    public String getBilheteTipo() {
        return bilheteTipo;
    }

    public String getBilheteValor() {
        return bilheteValor;
    }

    public String getBilheteValorUuid() {
        return bilheteValorUuid;
    }

    public String getEventoCidade() {
        return eventoCidade;
    }

    public String getEventoData() {
        return eventoData;
    }

    public String getEventoEstado() {
        return eventoEstado;
    }

    public String getEventoHora() {
        return eventoHora;
    }

    public String getEventoLocal() {
        return eventoLocal;
    }

    public String getEventoNome() {
        return eventoNome;
    }

    public String getPdvCodigo() {
        return pdvCodigo;
    }

    public String getPdvNome() {
        return pdvNome;
    }

    public String getVendaCodigoPagamento() {
        return vendaCodigoPagamento;
    }

    public String getVendaData() {
        return vendaData;
    }

    public String getVendaFormaPagamento() {
        return vendaFormaPagamento;
    }

    public String getVendaHora() {
        return vendaHora;
    }

    public String getVendaTransacao() {
        return vendaTransacao;
    }
}
