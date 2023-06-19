package com.digitalingressos.ingressos.data.database.evento;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.classificacaoIndicativa.ClassificacaoIndicativa;
import com.digitalingressos.ingressos.data.database.endereco.Endereco;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity(tableName = "evento")
public class Evento {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "codigo")
    @SerializedName("codigo")
    public String mCodigo;

    @NonNull
    @ColumnInfo(name = "nome")
    @SerializedName("nome")
    public String mNome;

    @NonNull
    @ColumnInfo(name = "imagem_banner_interno_link")
    @SerializedName("imagem_banner_interno_link")
    public String mImagemBannerInternoLink;

    @ColumnInfo(name = "imagem_banner_evento_link")
    @SerializedName("imagem_banner_evento_link")
    public String mImagemBannerEventoLink;

    @ColumnInfo(name = "imagem_link")
    @SerializedName("imagem_link")
    public String mImagemLink;

    @ColumnInfo(name = "imagem_sobre_link")
    @SerializedName("imagem_sobre_link")
    public String mImagemSobreLink;

    @ColumnInfo(name = "exibir_banner_principal")
    @SerializedName("exibir_banner_principal")
    public boolean mExibirBannerPrincipal;

    @ColumnInfo(name = "imagem_banner_principal_link")
    @SerializedName("imagem_banner_principal_link")
    public String mImagemBannerPrincipalLink;

    @ColumnInfo(name = "empresa_nome")
    @SerializedName("empresa_nome")
    public String mEmpresaNome;

    @ColumnInfo(name = "empresa_codigo")
    @SerializedName("empresa_codigo")
    public String mEmpresaCodigo;

    @ColumnInfo(name = "tipo_taxa_servico")
    @SerializedName("tipo_taxa_servico")
    public String mTipoTaxaServico;

    @ColumnInfo(name = "taxa_servico")
    @SerializedName("taxa_servico")
    public double mTaxaServico;

    @ColumnInfo(name = "ativo")
    @SerializedName("ativo")
    public boolean mAtivo;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    public String mStatus;

    @ColumnInfo(name = "exibir_pdv_fisico")
    @SerializedName("exibir_pdv_fisico")
    public boolean mExibirPdvFisico;

    @ColumnInfo(name = "tipo_cupom")
    @SerializedName("tipo_cupom")
    public String mTipoCupom;

    @ColumnInfo(name = "quantidade_cpf")
    @SerializedName("quantidade_cpf")
    public int mQuantidadeCpf;

    @ColumnInfo(name = "ordem_exibicao")
    @SerializedName("ordem_exibicao")
    public int mOrdemExibicao;

    @ColumnInfo(name = "local")
    @SerializedName("local")
    public String mLocal;

    @ColumnInfo(name = "estilo_descricao")
    @SerializedName("estilo_descricao")
    public String mEstiloDescricao;

    @ColumnInfo(name = "observacao")
    @SerializedName("observacao")
    public String mObservacao;

    @ColumnInfo(name = "mais_informacao")
    @SerializedName("mais_informacao")
    public String mMaisInformacao;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    public double mLatitude;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    public double mLongitude;

    @ColumnInfo(name = "imagem_logo_ingresso_link")
    @SerializedName("imagem_logo_ingresso_link")
    public String mImagemLogoIngressoLink;

    @ColumnInfo(name = "data_inicio")
    @SerializedName("data_inicio")
    public Date mDataInicio;

    @ColumnInfo(name = "data_fim")
    @SerializedName("data_fim")
    public Date mDataFim;

    @SerializedName("classificacao_indicativa")
    @Embedded(prefix = "class_")
    public ClassificacaoIndicativa mClassificacaoIndicativa;

    @SerializedName("endereco")
    @Embedded(prefix = "endereco_")
    public Endereco mEndereco;


    public Endereco getmEndereco() {
        return mEndereco;
    }

    public void setmEndereco(Endereco mEndereco) {
        this.mEndereco = mEndereco;
    }


    @NonNull
    public String getCodigo() {
        return mCodigo;
    }

    @NonNull
    public String getImagemBannerInternoLink() {

        if (mImagemBannerInternoLink.contentEquals("http") == false) {
            return "https://" + mImagemBannerInternoLink;
        }
        return mImagemBannerInternoLink;
    }

    @NonNull
    public String getNome() {
        return mNome;
    }

    public String getStatus() {
        return mStatus;
    }

    public boolean isExibirBannerPrincipal() {
        return mExibirBannerPrincipal;
    }

    public boolean isAtivo() {
        return mAtivo;
    }

    public int getOrdemExibicao() {
        return mOrdemExibicao;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public int getQuantidadeCpf() {
        return mQuantidadeCpf;
    }

    public double getTaxaServico() {
        return mTaxaServico;
    }

    public Date getDataInicio() {
        return mDataInicio;
    }


    public Bitmap getImagemLogo() {
        return BitmapFactory.decodeResource(IngressosApplication.getAppContext().getResources(), R.drawable.logo_digital_impresso);
    }

    public String getDataInicioDate() {
        SimpleDateFormat formatador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", IngressosApplication.getLocale());

        String dataString = formatador.format(this.getDataInicio());
        return dataString;
    }

    public String getDataInicioHora() {
        SimpleDateFormat formatador = new SimpleDateFormat("HH'h'mm", IngressosApplication.getLocale());

        String dataString = formatador.format(this.getDataInicio());
        return dataString;
    }

    public String getEmpresaCodigo() {
        return mEmpresaCodigo;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getEmpresaNome() {
        return mEmpresaNome;
    }

    public String getEstiloDescricao() {
        return mEstiloDescricao;
    }

    public String getImagemBannerEventoLink() {
        return mImagemBannerEventoLink;
    }

    public Date getDataFim() {
        return mDataFim;
    }

    public String getImagemBannerPrincipalLink() {
        return mImagemBannerPrincipalLink;
    }

    public String getImagemLink() {
        return mImagemLink;
    }

    public String getImagemSobreLink() {
        return mImagemSobreLink;
    }

    public String getLocal() {
        return mLocal;
    }

    public String getTipoCupom() {
        return mTipoCupom;
    }

    public String getObservacao() {
        return mObservacao;
    }

    public String getTipoTaxaServico() {
        return mTipoTaxaServico;
    }

    public String getMaisInformacao() {
        return mMaisInformacao;
    }

    public static Type getListType() {
        return new TypeToken<List<Evento>>() {
        }.getType();
    }

    public static Type getType() {
        return new TypeToken<Evento>() {
        }.getType();
    }
}
