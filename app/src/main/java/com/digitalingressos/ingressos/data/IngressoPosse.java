package com.digitalingressos.ingressos.data;

import com.digitalingressos.ingressos.data.database.checkout.CheckoutItemPosse;
import com.digitalingressos.ingressos.data.database.ingresso.Ingresso;
import com.digitalingressos.ingressos.util.MaskEditUtil;
import com.digitalingressos.ingressos.util.StringUtil;
import com.digitalingressos.ingressos.util.ValidaCPF;

import java.util.List;
import java.util.stream.Collectors;

public class IngressoPosse {
    private static final String TAG = IngressoPosse.class.getSimpleName();

    //INGRESSO SELECIONADO
    public Ingresso mIngresso;
    public String valorLoteIngressoUuid;
    public String cpfPosse;
    public String cpfPosseMascara;
    public boolean cpfError;
    public String telefonePosse;
    public String telefonePosseMascara;
    public boolean telefoneError;
    private boolean isAplicarTodos;
    private boolean isComprador;

    public boolean isComprador() {
        return this.isComprador;
    }

    public void setComprador(boolean comprador) {
        this.isComprador = comprador;
    }


    public boolean isAplicarTodos() {
        return isAplicarTodos;
    }

    public void setAplicarTodos(boolean aplicarTodos) {
        isAplicarTodos = aplicarTodos;
    }

    public List<CheckoutItemPosse> mCheckoutItemPosse;


    public IngressoPosse(Ingresso _ingresso) {
        mIngresso = _ingresso;
        valorLoteIngressoUuid = _ingresso.getValorLoteIngressoUuid();
    }

    public List<String> getListBilhetes() {
        return mCheckoutItemPosse.stream()
                .map(item -> item.mBilheteCodigo)
                .collect(Collectors.toList());
    }

    public IngressoPosse(Ingresso _ingresso, double _valorIngresso) {

        mIngresso = _ingresso;
        valorLoteIngressoUuid = _ingresso.getValorLoteIngressoUuid();
    }

    public Ingresso getIngresso() {
        return mIngresso;
    }

    public boolean isCpfError() {
        return cpfError;
    }

    public boolean isTelefoneError() {
        return telefoneError;
    }

    public void setCpfPosse(String cpfPosse) {
        if (cpfPosse != null && !cpfPosse.isEmpty()) {
            this.cpfPosse = cpfPosse.replace(".", "").replace("-", "").trim();
            this.cpfPosseMascara = MaskEditUtil.mask(this.cpfPosse, MaskEditUtil.FORMAT_CPF);
            if (this.cpfPosse.length() == 11) {
                cpfError = !ValidaCPF.isCPF(this.cpfPosse);
            } else {
                cpfError = false;
            }
        }
    }

    public void setTelefonePosse(String telefonePosse) {
        if (telefonePosse != null && !telefonePosse.isEmpty()) {
            this.telefonePosse = telefonePosse.replace(" ", "").replace("(", "").replace(")", "").replace("-", "").trim();
            this.telefonePosseMascara = MaskEditUtil.mask(this.telefonePosse, MaskEditUtil.FORMAT_FONE);
            ;
            if (this.telefonePosse.length() == 11) {
                telefoneError = false;
            } else {
                telefoneError = true;
            }
        }
    }


    public String getCpfPosse() {
        return cpfPosse;
    }

    public String getCpfPosseMascara() {
        return cpfPosseMascara;
    }


    public String getTelefonePosse() {
        return telefonePosse;
    }

    public String getTelefonePosseMascara() {
        return telefonePosseMascara;
    }

    public String geValorLoteIngressoUuid() {
        return valorLoteIngressoUuid;
    }


    @Override
    public boolean equals(Object obj) {
        IngressoPosse s = (IngressoPosse) obj;
        return this.valorLoteIngressoUuid.equalsIgnoreCase(s.valorLoteIngressoUuid);
    }
}
