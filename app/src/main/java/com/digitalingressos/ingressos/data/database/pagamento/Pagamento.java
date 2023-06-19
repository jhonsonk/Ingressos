package com.digitalingressos.ingressos.data.database.pagamento;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.data.database.enumeradores.StatusPagamento;
import com.digitalingressos.ingressos.data.database.enumeradores.TipoPagamento;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.pagamentoItem.PagamentoItem;
import com.digitalingressos.ingressos.util.UtilMoney;

import java.util.Date;
import java.util.List;

//@Entity(tableName = "pagamento")
@Entity(tableName = "pagamento", indices = {@Index("usuario_logado_id")},
        foreignKeys = {@ForeignKey(entity = Auth.class, parentColumns = "usuario_uuid", childColumns = "usuario_logado_id"),
                @ForeignKey(entity = Evento.class, parentColumns = "codigo", childColumns = "evento_codigo"),
                @ForeignKey(entity = Caixa.class, parentColumns = "id", childColumns = "caixa_id"),}
)
public class Pagamento {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    /**
     * Esse tipo se refere pagamento interno na maquina.
     */
    @ColumnInfo(name = "tipo")
    private TipoPagamento tipo;
    @ColumnInfo(name = "checkout_codigo")
    private String checkoutCodigo;

    /**
     * CODIGO de Pagament junto a digital
     */
    @ColumnInfo(name = "codigo")
    private String codigoDigital;
    @ColumnInfo(name = "uuid")
    private String uuid;
    @ColumnInfo(name = "status")
    private StatusPagamento status;

    /**
     * Numero de parcelas
     */
    @ColumnInfo(name = "numero_parcelas")
    private int numeroParcelas;

    /**
     * Valor total da compra
     */
    @ColumnInfo(name = "valor_total")
    private double valorTotal;

    /**
     * Valor total com juros
     */
    @ColumnInfo(name = "valor_toral_final")
    private double valorTotalFinal;

    /**
     * Taxa de Servico
     */
    @ColumnInfo(name = "taxa_servico")
    private double taxaServico;

    /**
     * Valor Total dos Ingressos, Sem taxas e Descontos
     */
    @ColumnInfo(name = "valor_parcial")
    private double valorParcial;


    /**
     * Valor Total dos Descontos
     */
    @ColumnInfo(name = "valor_desconto")
    private double valorDesconto;

    /**
     * Data e hora do pagamento
     */
    @ColumnInfo(name = "criado_em")
    private Date criadoEm;

    /**
     * DADOS DA TRANSACAO COM O CARTAO
     */
    @ColumnInfo(name = "transacao_codigo")
    private String transacaoCodigo;

    @ColumnInfo(name = "transacao_id")
    private String transacaoId;

    @ColumnInfo(name = "transacao_data")
    private String transacaoData;

    @ColumnInfo(name = "transacao_hora")
    private String transacaoHora;

    @ColumnInfo(name = "transacao_terminal_serial_number")
    private String transacaoTerminalSerialNumber;

    @ColumnInfo(name = "transacao_nsu")
    private String transacaoNsu;

    @ColumnInfo(name = "transacao_host_nsu")
    private String transacaoHostNsu;

    @ColumnInfo(name = "transacao_card_brand")
    private String transacaoCardBrand;

    @ColumnInfo(name = "pagador_cpf")
    private String pagadorCpf;

    @ColumnInfo(name = "pagador_telefone")
    private String pagadorTelefone;

    @ColumnInfo(name = "usuario_logado_id")
    public String usuarioLogadoId;


    @ColumnInfo(name = "caixa_id")
    public long caixaId;

    @Ignore
    private List<PagamentoItem> pagamentoItemList;


    // Getters e setters para a lista de PagamentoItem
    public List<PagamentoItem> getPagamentoItemList() {
        return pagamentoItemList;
    }

    public void setPagamentoItemList(List<PagamentoItem> pagamentoItemList) {
        this.pagamentoItemList = pagamentoItemList;
    }

    public String getValorTotalFinalMonetery() {
        return UtilMoney.parseDoubleToMoney(valorTotalFinal, IngressosApplication.getLocale());
    }

    @Ignore
    private Auth usuarioLogado;

    public String getUsuarioLogadoId() {
        return usuarioLogadoId;
    }

    public void setUsuarioLogadoId(String usuarioLogadoId) {
        this.usuarioLogadoId = usuarioLogadoId;
    }

    public long getCaixaId() {
        return caixaId;
    }

    public void setCaixaId(long caixaId) {
        this.caixaId = caixaId;
    }

    public Auth getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Auth usuarioLogado) {
        this.usuarioLogadoId = usuarioLogado.getUsuarioUuid();
        this.usuarioLogado = usuarioLogado;
    }

    public String getPagadorCpf() {
        return pagadorCpf;
    }

    public void setPagadorCpf(String compradorCpf) {
        this.pagadorCpf = compradorCpf;
    }

    public String getPagadorTelefone() {
        return pagadorTelefone;
    }

    public void setPagadorTelefone(String compradorTelefone) {
        this.pagadorTelefone = compradorTelefone;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }

    public String getCheckoutCodigo() {
        return checkoutCodigo;
    }

    public void setCheckoutCcodigo(String checkoutCcodigo) {
        this.checkoutCodigo = checkoutCcodigo;
    }

    public void setCheckoutCodigo(String checkoutCodigo) {
        this.checkoutCodigo = checkoutCodigo;
    }

    public double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }


    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigoDigital() {
        return codigoDigital;
    }

    public void setCodigoDigital(String codigo) {
        this.codigoDigital = codigo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(double taxaServico) {
        this.taxaServico = taxaServico;
    }

    public double getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(double valorParcial) {
        this.valorParcial = valorParcial;
    }

    public double getValorTotalFinal() {
        return valorTotalFinal;
    }

    public void setValorTotalFinal(double valorTotalComJuros) {
        this.valorTotalFinal = valorTotalComJuros;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getTransacaoCodigo() {
        return transacaoCodigo;
    }

    public void setTransacaoCodigo(String transacaoCodigo) {
        this.transacaoCodigo = transacaoCodigo;
    }

    public String getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(String transacaoId) {
        this.transacaoId = transacaoId;
    }

    public String getTransacaoData() {
        return transacaoData;
    }

    public void setTransacaoData(String transacaoData) {
        this.transacaoData = transacaoData;
    }

    public String getTransacaoHora() {
        return transacaoHora;
    }

    public void setTransacaoHora(String transacaoHora) {
        this.transacaoHora = transacaoHora;
    }

    public String getTransacaoTerminalSerialNumber() {
        return transacaoTerminalSerialNumber;
    }

    public void setTransacaoTerminalSerialNumber(String transacaoTerminalSerialNumber) {
        this.transacaoTerminalSerialNumber = transacaoTerminalSerialNumber;
    }

    public String getTransacaoNsu() {
        return transacaoNsu;
    }

    public void setTransacaoNsu(String transacaoNsu) {
        this.transacaoNsu = transacaoNsu;
    }

    public String getTransacaoHostNsu() {
        return transacaoHostNsu;
    }

    public void setTransacaoHostNsu(String transacaoHostNsu) {
        this.transacaoHostNsu = transacaoHostNsu;
    }

    public String getTransacaoCardBrand() {
        return transacaoCardBrand;
    }

    public void setTransacaoCardBrand(String transacaoCardBrand) {
        this.transacaoCardBrand = transacaoCardBrand;
    }

    @Ignore
    private Evento evento;

    public String getEventoCodigo() {
        return eventoCodigo;
    }

    public void setEventoCodigo(String eventoCodigo) {
        this.eventoCodigo = eventoCodigo;
    }

    @ColumnInfo(name = "evento_codigo")
    public String eventoCodigo;

    public Evento getEvento() {
        if (evento == null)
            return new Evento();
        return evento;
    }

    public void setEvento(Evento evento) {
        this.eventoCodigo = evento.getCodigo();
        this.evento = evento;
    }

}
