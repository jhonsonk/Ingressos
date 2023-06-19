package com.digitalingressos.ingressos.data.database.caixa;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.ResumoCompra;
import com.digitalingressos.ingressos.data.database.ResumoCaixa;
import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.data.database.enumeradores.StatusPagamento;
import com.digitalingressos.ingressos.data.database.enumeradores.TipoPagamento;
import com.digitalingressos.ingressos.data.database.pagamento.Pagamento;
import com.digitalingressos.ingressos.util.UtilMoney;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(tableName = "caixa", foreignKeys = @ForeignKey(entity = Auth.class, parentColumns = "usuario_uuid", childColumns = "auth_id"))
public class Caixa {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @NonNull
    @ColumnInfo(name = "uuid")
    private String uuid;

    @NonNull
    @ColumnInfo(name = "auth_id")
    private String authId;

    @ColumnInfo(name = "aberto_em")
    private Date abertoEm;

    @ColumnInfo(name = "fechado_em")
    private Date fechadoEm;

    @ColumnInfo(name = "total_vendas_credito")
    private double totalVendasCredito;

    @ColumnInfo(name = "total_vendas_debito")
    private double totalVendasDebito;

    @ColumnInfo(name = "total_vendas_dinheiro")
    private double totalVendasDinheiro;

    @ColumnInfo(name = "total_vendas_pix")
    private double totalVendasPix;

    @ColumnInfo(name = "quantidade_vendas_credito")
    private int quantidadeVendasCredito;

    @ColumnInfo(name = "quantidade_vendas_debito")
    private int quantidadeVendasDebito;

    @ColumnInfo(name = "quantidade_vendas_dinheiro")
    private int quantidadeVendasDinheiro;

    @ColumnInfo(name = "quantidade_vendas_pix")
    private int quantidadeVendasPix;

    @Ignore
    private Auth auth;


    // Construtor, getters e setters


    public Caixa() {
    }

    public Caixa(@NonNull String authId) {
        this.authId = authId;
        this.uuid = UUID.randomUUID().toString();
        this.abertoEm = new Date();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getAuthId() {
        return authId;
    }

    public double getTotalVendasCredito() {
        return totalVendasCredito;
    }

    public void setTotalVendasCredito(double totalVendasCredito) {
        this.totalVendasCredito = totalVendasCredito;
    }

    public double getTotalVendasDebito() {
        return totalVendasDebito;
    }

    public void setTotalVendasDebito(double totalVendasDebito) {
        this.totalVendasDebito = totalVendasDebito;
    }

    public double getTotalVendasDinheiro() {
        return totalVendasDinheiro;
    }

    public String getTotalVendasDinheiroMonetary() {
        return UtilMoney.parseDoubleToMoney(totalVendasDinheiro, IngressosApplication.getLocale());
    }

    public String getTotalVendasPixMonetary() {
        return UtilMoney.parseDoubleToMoney(totalVendasPix, IngressosApplication.getLocale());
    }

    public String getTotalVendasCreditoMonetary() {
        return UtilMoney.parseDoubleToMoney(totalVendasCredito, IngressosApplication.getLocale());
    }

    public String getTotalVendasDebitoMonetary() {
        return UtilMoney.parseDoubleToMoney(totalVendasDebito, IngressosApplication.getLocale());
    }

    public String getTotalVendasMonetary() {
        double totais = totalVendasDebito + totalVendasCredito + totalVendasPix + totalVendasDinheiro;
        return UtilMoney.parseDoubleToMoney(totais, IngressosApplication.getLocale());
    }

    public void setTotalVendasDinheiro(double totalVendasDinheiro) {
        this.totalVendasDinheiro = totalVendasDinheiro;
    }

    public int getQuantidadeTotalVendas() {
        return quantidadeVendasCredito + quantidadeVendasDebito + quantidadeVendasDinheiro + quantidadeVendasPix;
    }

    public int getQuantidadeVendasCredito() {
        return quantidadeVendasCredito;
    }

    public void setQuantidadeVendasCredito(int quantidadeVendasCredito) {
        this.quantidadeVendasCredito = quantidadeVendasCredito;
    }

    public int getQuantidadeVendasDebito() {
        return quantidadeVendasDebito;
    }

    public void setQuantidadeVendasDebito(int quantidadeVendasDebito) {
        this.quantidadeVendasDebito = quantidadeVendasDebito;
    }

    public int getQuantidadeVendasDinheiro() {
        return quantidadeVendasDinheiro;
    }

    public void setQuantidadeVendasDinheiro(int quantidadeVendasDinheiro) {
        this.quantidadeVendasDinheiro = quantidadeVendasDinheiro;
    }

    public int getQuantidadeVendasPix() {
        return quantidadeVendasPix;
    }

    public void setQuantidadeVendasPix(int quantidadeVendasPix) {
        this.quantidadeVendasPix = quantidadeVendasPix;
    }

    public double getTotalVendasPix() {
        return totalVendasPix;
    }

    public void setTotalVendasPix(double totalVendasPix) {
        this.totalVendasPix = totalVendasPix;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public Date getAbertoEm() {
        return abertoEm;
    }

    public void setAbertoEm(Date abertoEm) {
        this.abertoEm = abertoEm;
    }

    public Date getFechadoEm() {
        return fechadoEm;
    }

    public void setFechadoEm(Date fechadoEm) {
        this.fechadoEm = fechadoEm;
    }

    public void fecharCaixa() {
        this.setFechadoEm(new Date());
    }

    public void realizarCalculoFechamento(List<Pagamento> listaPagamentos) {

        Map<TipoPagamento, List<Pagamento>> pagamentosPorTipo = listaPagamentos.stream()
                .filter(p -> p.getStatus() == StatusPagamento.PAGO) // Adicione a cláusula where aqui
                .collect(Collectors.groupingBy(Pagamento::getTipo));

        for (Map.Entry<TipoPagamento, List<Pagamento>> entry : pagamentosPorTipo.entrySet()) {
            TipoPagamento tipoPagamento = entry.getKey();
            List<Pagamento> pagamentos = entry.getValue();
            double somaValores = pagamentos.stream()
                    .mapToDouble(Pagamento::getValorTotalFinal)
                    .sum();

            if (tipoPagamento == TipoPagamento.CARTAO_CREDITO) {

                this.quantidadeVendasCredito = pagamentos.size();
                this.totalVendasCredito = somaValores;
            }

            if (tipoPagamento == TipoPagamento.CARTAO_DEBITO) {
                this.quantidadeVendasDebito = pagamentos.size();
                this.totalVendasDebito = somaValores;
            }

            if (tipoPagamento == TipoPagamento.DINHEIRO) {
                this.quantidadeVendasDinheiro = pagamentos.size();
                this.totalVendasDinheiro = somaValores;
            }
            if (tipoPagamento == TipoPagamento.PIX) {
                this.quantidadeVendasPix = pagamentos.size();
                this.totalVendasPix = somaValores;
            }
        }
    }
}
