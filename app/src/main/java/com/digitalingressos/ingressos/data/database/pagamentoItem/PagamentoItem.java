package com.digitalingressos.ingressos.data.database.pagamentoItem;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.IngressoPosse;
import com.digitalingressos.ingressos.data.database.pagamento.Pagamento;
import com.digitalingressos.ingressos.util.MaskEditUtil;
import com.digitalingressos.ingressos.util.StringUtil;
import com.digitalingressos.ingressos.util.UtilMoney;
import com.digitalingressos.ingressos.util.Utils;

import java.util.List;

@Entity(tableName = "pagamento_item", indices = {@Index("pagamento_id")}, foreignKeys = @ForeignKey(entity = Pagamento.class, parentColumns = "id", childColumns = "pagamento_id"))
public class PagamentoItem {

    //#region Propriedades
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "ingresso_nome")
    private String ingressoNome;
    @ColumnInfo(name = "ingresso_tipo")
    private String ingressoTipo;

    @ColumnInfo(name = "ingresso_sexo")
    private String ingressoSexo;

    @ColumnInfo(name = "ingresso_codigo")
    private String ingressoCodigo;

    @ColumnInfo(name = "lote_nome")
    private String loteNome;

    @ColumnInfo(name = "valor_ingresso")
    private double valorIngresso;

    @ColumnInfo(name = "valor_taxa_servico")
    private double valorTaxaServico;

    @ColumnInfo(name = "valor_final")
    private double valorFinal;

    @ColumnInfo(name = "posse_cpf")
    private String posseCpf;

    @ColumnInfo(name = "posse_telefone")
    private String posseTelefone;

    @ColumnInfo(name = "sessao_nome")
    private String sessaoNome;

    @ColumnInfo(name = "sessao_codigo")
    private String sessaoCodigo;

    @ColumnInfo(name = "setor_codigo")
    private String setorCodigo;

    @ColumnInfo(name = "setor_nome")
    private String setorNome;

    @ColumnInfo(name = "uuid_valor_lote_ingresso")
    private String uuidValorLoteIngresso;

    @ColumnInfo(name = "combo")
    private int combo;

    @ColumnInfo(name = "bilhetes_codigos")
    private List<String> bilhetesCodigos;

    @ColumnInfo(name = "pagamento_id")
    public long pagamentoId;

    @Ignore
    public Pagamento pagamento;
    //#endregion


    //#region Gets e Sets
    public long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Pagamento getPagamento() {
        if (pagamento == null)
            return new Pagamento();
        return pagamento;
    }


    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public String getIngressoNome() {
        return ingressoNome;
    }

    public void setIngressoNome(String ingressoNome) {
        this.ingressoNome = ingressoNome;
    }

    public String getIngressoTipo() {
        return ingressoTipo;
    }

    public void setIngressoTipo(String ingressoTipo) {
        this.ingressoTipo = ingressoTipo;
    }

    public String getIngressoSexo() {
        return ingressoSexo;
    }

    public void setIngressoSexo(String ingressoSexo) {
        this.ingressoSexo = ingressoSexo;
    }

    public String getIngressoCodigo() {
        return ingressoCodigo;
    }

    public void setIngressoCodigo(String ingressoCodigo) {
        this.ingressoCodigo = ingressoCodigo;
    }

    public String getLoteNome() {
        return loteNome;
    }

    public void setLoteNome(String loteNome) {
        this.loteNome = loteNome;
    }

    public double getValorIngresso() {
        return valorIngresso;
    }

    public void setValorIngresso(double valorIngresso) {
        this.valorIngresso = valorIngresso;
    }

    public double getValorTaxaServico() {
        return valorTaxaServico;
    }

    public void setValorTaxaServico(double valorTaxaServico) {
        this.valorTaxaServico = valorTaxaServico;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public double getValorFinal() {
        return this.valorFinal;
    }

    public String getValorFinalMonetary() {
        return UtilMoney.parseDoubleToMoney(this.getValorFinal(), IngressosApplication.getLocale());
    }


    public String getPosseCpf() {
        return posseCpf;
    }

    public String getPosseCpfMascara() {
        return MaskEditUtil.mask(this.getPosseCpf(), MaskEditUtil.FORMAT_CPF);
    }

    public void setPosseCpf(String posseCpf) {
        this.posseCpf = posseCpf;
    }

    public String getPosseTelefone() {
        return posseTelefone;
    }

    public void setPosseTelefone(String posseTelefone) {
        this.posseTelefone = posseTelefone;
    }

    public String getSessaoNome() {
        return sessaoNome;
    }

    public void setSessaoNome(String sessaoNome) {
        this.sessaoNome = sessaoNome;
    }

    public String getSessaoCodigo() {
        return sessaoCodigo;
    }

    public void setSessaoCodigo(String sessaoCodigo) {
        this.sessaoCodigo = sessaoCodigo;
    }

    public String getSetorCodigo() {
        return setorCodigo;
    }

    public void setSetorCodigo(String setorCodigo) {
        this.setorCodigo = setorCodigo;
    }

    public String getSetorNome() {
        return setorNome;
    }

    public void setSetorNome(String setorNome) {
        this.setorNome = setorNome;
    }

    public String getUuidValorLoteIngresso() {
        return uuidValorLoteIngresso;
    }

    public void setUuidValorLoteIngresso(String uuidValorLoteIngresso) {
        this.uuidValorLoteIngresso = uuidValorLoteIngresso;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public String getTelefoneMascara() {
        return MaskEditUtil.mask(this.getPosseTelefone(), MaskEditUtil.FORMAT_FONE);
    }

    public List<String> getBilhetesCodigos() {
        return bilhetesCodigos;
    }

    public void setBilhetesCodigos(List<String> bilhetesCodigos) {
        this.bilhetesCodigos = bilhetesCodigos;
    }

    //#endregion

    public String getIngressoTipoValido() {
        if (this.getIngressoTipo().toUpperCase().equals("EM_ABERTO")) {
            return "";
        }
        return this.getIngressoTipo().replace("_", " ");
    }

    public String getStringResumoIngresso() {

        int tamanho = 33;
        int tamanhoNomeEvento = this.getPagamento().getEvento().getNome().length();
        int tamanhoNomeSessao = this.getSessaoNome().length();
        String nomeEvento = this.getPagamento().getEvento().getNome();
        String nomeSessao = this.getSessaoNome();

        StringUtil builder = new StringUtil();

        if ((tamanhoNomeEvento + 1 + tamanhoNomeSessao) > tamanho) {
            nomeEvento = StringUtil.padRight(this.getPagamento().getEvento().getNome(), 20, ' ').substring(0, 20).trim();
            nomeSessao = StringUtil.padRight(this.getSessaoNome(), 13, ' ').substring(0, 13).trim();
        }
        int tamanhoNomeIngresso = this.getIngressoSexo().length();
        int tamanhoSexo = this.getIngressoSexo().length();
        int tamanhoTipo = this.getIngressoTipoValido().length();

        String nomeIngresso = this.getIngressoNome();
        String nomeSexo = this.getIngressoSexo();
        String nomeTipo = this.getIngressoTipoValido();

        if ((tamanhoNomeIngresso + tamanhoSexo + tamanhoTipo + 2) > tamanho) {

            nomeIngresso = StringUtil.padRight(this.getIngressoNome(), 26, ' ').substring(0, 26).trim();
            nomeSexo = this.getIngressoSexo().substring(0, 4).trim().concat(".");
            nomeTipo = StringUtil.padRight(this.getIngressoTipoValido(), 3, ' ').substring(0, 3).trim();
        }

        builder.appendWithSpace(nomeEvento).appendWithDash(nomeSessao)
                .appendWithNewLine(nomeIngresso).appendWithSpace(nomeSexo).appendWithSpace(nomeTipo);


        return builder.toString();
    }

    public String getStringResumoPosse() {
        return "CPF:" + this.getPosseCpfMascara() + " Whatsapp:" + this.getTelefoneMascara();
    }

    public void convertToIngressoPosse(IngressoPosse ingressoPosse) {

        this.ingressoCodigo = ingressoPosse.mIngresso.getCodigo();
        this.ingressoNome = ingressoPosse.mIngresso.getNome();
        this.ingressoSexo = ingressoPosse.mIngresso.getSexo();
        this.ingressoTipo = ingressoPosse.mIngresso.getTipo();
        this.valorIngresso = ingressoPosse.mIngresso.getPrecoIngresso();
        this.valorTaxaServico = ingressoPosse.mIngresso.getTaxaServico();
        this.valorFinal = ingressoPosse.mIngresso.getValor();
        this.uuidValorLoteIngresso = ingressoPosse.valorLoteIngressoUuid;
        this.loteNome = ingressoPosse.mIngresso.getLoteNome();
        this.posseCpf = ingressoPosse.getCpfPosse();
        this.posseTelefone = ingressoPosse.getTelefonePosse();
        this.sessaoCodigo = ingressoPosse.mIngresso.getSetor().getSessao().getCodigo();
        this.sessaoNome = ingressoPosse.mIngresso.getSetor().getSessao().getNome();
        this.setorCodigo = ingressoPosse.mIngresso.getSetor().getCodigo();
        this.setorNome = ingressoPosse.mIngresso.getSetor().getNome();
        this.combo = ingressoPosse.mIngresso.getCombo();
        this.bilhetesCodigos = ingressoPosse.getListBilhetes();
    }
}

