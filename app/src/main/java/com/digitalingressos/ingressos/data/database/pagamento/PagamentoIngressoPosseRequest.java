package com.digitalingressos.ingressos.data.database.pagamento;

import com.google.gson.annotations.SerializedName;

public class PagamentoIngressoPosseRequest {
    @SerializedName("cliente_cpf")
    private String clienteCpf;

    @SerializedName("cliente_telefone")
    private String clienteTelefone;

    @SerializedName("bilhete_codigo")
    private String bilheteCodigo;

    @SerializedName("uuid_valor_ingresso")
    private String uuidValorIngresso;


    //GET E SET
    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public String getBilheteCodigo() {
        return bilheteCodigo;
    }

    public void setBilheteCodigo(String bilheteCodigo) {
        this.bilheteCodigo = bilheteCodigo;
    }

    public String getUuidValorIngresso() {
        return uuidValorIngresso;
    }

    public void setUuidValorIngresso(String uuidValorIngresso) {
        this.uuidValorIngresso = uuidValorIngresso;
    }
}
