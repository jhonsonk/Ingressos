package com.digitalingressos.ingressos.ui.fragment;

import static com.digitalingressos.ingressos.util.IngressosConstants.CARD_METODO;
import static com.digitalingressos.ingressos.util.IngressosConstants.CREDIT_VALUE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.IngressoPosse;
import com.digitalingressos.ingressos.data.ResumoCompra;
import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.data.database.caixa.CaixaRepository;
import com.digitalingressos.ingressos.data.database.enumeradores.StatusPagamento;
import com.digitalingressos.ingressos.data.database.enumeradores.TipoPagamento;
import com.digitalingressos.ingressos.data.database.pagamento.*;
import com.digitalingressos.ingressos.data.database.pagamentoItem.*;
import com.digitalingressos.ingressos.data.database.sessao.*;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.payment.*;
import com.digitalingressos.ingressos.printer.PrinterBilhete;
import com.digitalingressos.ingressos.printer.PrinterService;
import com.digitalingressos.ingressos.ui.activity.MainActivity;
import com.digitalingressos.ingressos.ui.adpater.*;
import com.digitalingressos.ingressos.util.*;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResumoCompraFragment extends Fragment {

    private CarrinhoViewModel mCarrinho;
    //UTILIZADDO NO RESUMO DA VENDA
    private List<ResumoCompra> mResumoCompra;

    //ITENS EFETIVOS COMPRADOS
    private List<PagamentoItem> mPagamentoItems;

    //OBJETO DE PAGAMENTO
    private Pagamento mPagamento;

    //UTILIZADO NO RETORNO
    private Sessao mSessao;

    private ProgressDialog pDialog;
    private CustomDialog mDialogCustom;
    private String mTitleDialog;

    private View mViewPrincipal;
    private RecyclerView mRecyclerView;
    private LinearLayout mLayCart;
    private LinearLayout mLayBtVoltar;
    private LinearLayout mLayBtComprar;
    private LinearLayout mLayBtDinheiro;
    private LinearLayout mLayBtCredito;
    private LinearLayout mLayBtDebito;
    private LinearLayout mLayPtPix;
    private TextView mTvValor;
    private ResumoCompraAdapter mAdapter;
    AlertDialog.Builder builder ;
    public ResumoCompraFragment(Sessao sessao, CarrinhoViewModel carrinho) {
        mCarrinho = carrinho;
        mPagamento = new Pagamento();
        mPagamento.setCheckoutCcodigo(carrinho.getCheckout().getCodigo());
        mPagamento.setEvento(carrinho.getEvento());
        mPagamento.setValorTotal(carrinho.getCheckout().getPreco());
        mPagamento.setTaxaServico(carrinho.getCheckout().getTaxaServico());
        mPagamento.setValorParcial(carrinho.getCheckout().getPrecoIngresso());
        mPagamento.setStatus(StatusPagamento.PENDENTE);
        mPagamento.setUsuarioLogado(IngressosApplication.getAppAuth());
        mPagamento.setPagadorTelefone(carrinho.getPagadorPadrao().getTelefonePosse());
        mPagamento.setPagadorCpf(carrinho.getPagadorPadrao().getCpfPosse());

        CaixaRepository caixaRepository = new CaixaRepository(IngressosApplication.getAppDatabase());
        Caixa mEmAberto = caixaRepository.obterAberto(mPagamento.getUsuarioLogado().getUsuarioUuid());
        mPagamento.setCaixaId(mEmAberto.getId());
        mSessao = sessao;
        mResumoCompra = new ArrayList<>();
        mPagamentoItems = new ArrayList<>();
        for (IngressoPosse ingressoPosse : carrinho.getSelectedItemList()) {
            PagamentoItem pagamentoItem = new PagamentoItem();
            pagamentoItem.convertToIngressoPosse(ingressoPosse);
            pagamentoItem.setPagamento(mPagamento);
            mPagamentoItems.add(pagamentoItem);
        }

        HashMap<String, List<PagamentoItem>> compras = (HashMap) mPagamentoItems.stream().collect(Collectors.groupingBy(order -> order.getUuidValorLoteIngresso()));

        compras.forEach((key, value) -> {
            PagamentoItem primeiro = value.get(0);
            ResumoCompra resumoCompra = new ResumoCompra();
            resumoCompra.setResumoIngresso(primeiro.getStringResumoIngresso());
            resumoCompra.setValorUnitario(primeiro.getValorFinal());
            resumoCompra.setQuantidade(value.size());
            resumoCompra.setPosse(value);
            resumoCompra.setValorTotal();
            mResumoCompra.add(resumoCompra);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void initDialogs() {

        pDialog = new ProgressDialog(this.getContext());
        pDialog.setMessage("Carregando...");

        mTitleDialog = "Atenção";
        mDialogCustom = new CustomDialog(this.getContext());
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewPrincipal = inflater.inflate(R.layout.fragment_resumo_compra, container, false);

        initDialogs();
        pDialog.show();

        builder = new AlertDialog.Builder(getContext());
        //BOTAO FISICO DE VOLTAR
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

        //Get Layout of cart
        mLayCart = mViewPrincipal.findViewById(R.id.layout_carrinho_id);

        //Botao de voltar
        mLayBtVoltar = mLayCart.findViewById(R.id.lay_voltar);
        mLayBtVoltar.setOnClickListener(viewa -> onVoltar(mViewPrincipal));

        //Botao de Cancelar
        mLayBtComprar = mLayCart.findViewById(R.id.lay_cancelar);
        mLayBtComprar.setOnClickListener(viewa -> onCancelarComprar(mViewPrincipal));

        //botao de dinheiro
        mLayBtDinheiro = mLayCart.findViewById(R.id.lay_dinheiro);
        mLayBtDinheiro.setOnClickListener(viewa -> onComprarDinheiro(mViewPrincipal));

        //Botao Credito
        mLayBtCredito = mLayCart.findViewById(R.id.lay_credito);
        mLayBtCredito.setOnClickListener(viewa -> onComprarCredito(mViewPrincipal));

        //Botao debito
        mLayBtDebito = mLayCart.findViewById(R.id.lay_debito);
        mLayBtDebito.setOnClickListener(viewa -> onComprarDebito(mViewPrincipal));

        //Botao debito
        mLayPtPix = mLayCart.findViewById(R.id.lay_pix);
        mLayPtPix.setOnClickListener(viewa -> onComprarPix(mViewPrincipal));

        //Valor do carrinho
        mTvValor = mLayCart.findViewById(R.id.valor_total_carrinho);
        mTvValor.setText(mCarrinho.getPrecoTotalString());

        setUpToolbar(mViewPrincipal);

        mRecyclerView = mViewPrincipal.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));
        mAdapter = new ResumoCompraAdapter(mResumoCompra, mCarrinho);
        mRecyclerView.setAdapter(mAdapter);

        pDialog.dismiss();
        return mViewPrincipal;
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            onVoltar(mViewPrincipal);
        }
    };

    public void onVoltar(View view) {
        ((NavigationHost) view.getContext()).navigateTo(new IngressoPosseFragment(mSessao, mCarrinho), false);
    }

    public void onCancelarComprar(View view) {
        ((NavigationHost) view.getContext()).navigateTo(new EventoFragment(), false);
    }


    //REFAZER
    public void onComprarDinheiro(View view) {
        mPagamento.setTipo(TipoPagamento.DINHEIRO);

        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialog_dinheiro);
        dialog.show();

        TextView tvTroca = (TextView) dialog.findViewById(R.id.tv_troco);


        Locale mLocale = new Locale("pt", "BR");

        EditText mEtValorRecebido = (EditText) dialog.findViewById(R.id.et_valor_recebido);
        mEtValorRecebido.setText("R$0,00");
        mEtValorRecebido.setSelection(mEtValorRecebido.getText().length());
        mEtValorRecebido.addTextChangedListener(new MoneyTextWatcher(mEtValorRecebido, mLocale));

        mEtValorRecebido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                double valorTotal = mCarrinho.getPrecoTotal();
                String valorRecebido = mEtValorRecebido.getText().toString();
                double valorRecebidoDouble = UtilMoney.parseMoneyToDouble(valorRecebido, mLocale);
                double troco = valorTotal - valorRecebidoDouble;
                tvTroca.setText(UtilMoney.parseDoubleToMoney(troco, mLocale));
            }
        });


        TextView valorVenda = (TextView) dialog.findViewById(R.id.tv_total_venda);
        valorVenda.setText(mCarrinho.getPrecoTotalString());

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.

        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);

        LinearLayout declineButton = (LinearLayout) dialog.findViewById(R.id.lay_voltar);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        LinearLayout confirmarButton = (LinearLayout) dialog.findViewById(R.id.lay_confirmar);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Criar um objeto Date com a data desejada
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(date);

                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                String formattedFour = hourFormat.format(date);

                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString().replaceAll("-", "").toUpperCase();
                mPagamento.setTransacaoCodigo(uuidString);
                mPagamento.setTransacaoId(uuidString.substring(0, 5));

                mPagamento.setTransacaoData(formattedDate);
                mPagamento.setTransacaoHora(formattedFour);
                mPagamento.setTransacaoCardBrand("");
                mPagamento.setCriadoEm(date);
                mPagamento.setTransacaoTerminalSerialNumber("");
                mPagamento.setValorTotalFinal(mPagamento.getValorTotal());

                mPagamento.setTransacaoHostNsu("");
                mPagamento.setTransacaoNsu("");
                mPagamento.setTransacaoCodigo("");
                mPagamento.setNumeroParcelas(1);
                mPagamento.setStatus(StatusPagamento.PAGO);
                onCompraAprovada();
                dialog.dismiss();
            }
        });
    }

    public void onComprarCredito(View view) {
        mPagamento.setTipo(TipoPagamento.CARTAO_CREDITO);
        Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
        intent.putExtra(CREDIT_VALUE, mCarrinho.getPrecoTotalInt());
        intent.putExtra(CARD_METODO, 2);
        startActivityForResult(intent, 1);
    }

    public void onComprarDebito(View view) {
        mPagamento.setTipo(TipoPagamento.CARTAO_DEBITO);
        Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
        intent.putExtra(CREDIT_VALUE, mCarrinho.getPrecoTotalInt());
        intent.putExtra(CARD_METODO, 1);
        startActivityForResult(intent, 1);
    }


    //TODO
    public void onComprarPix(View view) {
        mPagamento.setTipo(TipoPagamento.PIX);
        Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
        intent.putExtra(CREDIT_VALUE, mCarrinho.getPrecoTotalInt());
        intent.putExtra(CARD_METODO, 0);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //mPagamento.setTransacaoCodigo(data.getStringExtra(IngressosConstants.PAYMENT_BIN));//INICIO DO CARTAO
            //mPagamento.setTransacaoCodigo(data.getStringExtra(IngressosConstants.PAYMENT_HOLDER));//4 ultimos
            //mPagamento.setTransacaoCodigo(data.getStringExtra(IngressosConstants.PAYMENT_LABEL));//CRETIDO/DEBIDO
            //mPagamento.setTransacaoCodigo(data.getStringExtra(IngressosConstants.PAYMENT_READER_MODEL));//MODELO A930
            //mPagamento.setValorTotal(data.getIntExtra(IngressosConstants.PAYMENT_ORIGINAL_AMOUNT, 1));
            //data.getStringExtra(IngressosConstants.PAYMENT_ERROR_CODE);

            String printReceipt = data.getStringExtra(IngressosConstants.PAYMENT_TYPE_TRANSACTION);
            if (printReceipt.toUpperCase().equals("ESTORNO")) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
                return;
            }

            pDialog.show();
            pDialog.setMessage("Processando...");
            mPagamento.setTransacaoCodigo(data.getStringExtra(IngressosConstants.PAYMENT_TRANSACTION_CODE));
            mPagamento.setTransacaoId(data.getStringExtra(IngressosConstants.PAYMENT_TRANSACTION_ID));
            mPagamento.setTransacaoData(data.getStringExtra(IngressosConstants.PAYMENT_DATE));
            mPagamento.setTransacaoHora(data.getStringExtra(IngressosConstants.PAYMENT_TIME));
            mPagamento.setTransacaoCardBrand(data.getStringExtra(IngressosConstants.PAYMENT_CARD_BRAND));

            mPagamento.setTransacaoTerminalSerialNumber(data.getStringExtra(IngressosConstants.PAYMENT_TERMINAL_SERIAL_NUMBER));

            String valorFinalStr = data.getStringExtra(IngressosConstants.PAYMENT_AMOUNT);
            mPagamento.setValorTotalFinal(Integer.parseInt(valorFinalStr) / 100);

            mPagamento.setTransacaoHostNsu(data.getStringExtra(IngressosConstants.PAYMENT_HOST_NSU));
            mPagamento.setTransacaoNsu(data.getStringExtra(IngressosConstants.PAYMENT_NSU));
            mPagamento.setNumeroParcelas(data.getIntExtra(IngressosConstants.PAYMENT_INSTALMENTS, 1));

            mPagamento.setStatus(StatusPagamento.PAGO);
            onCompraAprovada();
        }
    }


    public void onCompraAprovada() {

        PagamentoRequest pagamentoRequest = new PagamentoRequest();
        pagamentoRequest.setBandeira(mPagamento.getTransacaoCardBrand());
        pagamentoRequest.setCheckoutCodigo(mPagamento.getCheckoutCodigo());
        pagamentoRequest.setMetodoPagamento(mPagamento.getTipo().toString());
        pagamentoRequest.setNumeroParcelas(mPagamento.getNumeroParcelas());

        List<PagamentoIngressoPosseRequest> posseRequests = new ArrayList<>();
        for (PagamentoItem item : mPagamentoItems) {

            for (String codigoBilhete : item.getBilhetesCodigos()) {
                PagamentoIngressoPosseRequest posseRequest = new PagamentoIngressoPosseRequest();
                posseRequest.setBilheteCodigo(codigoBilhete);
                posseRequest.setUuidValorIngresso(item.getUuidValorLoteIngresso());
                posseRequest.setClienteCpf(item.getPosseCpf());
                posseRequest.setClienteTelefone(item.getPosseTelefone());
                posseRequests.add(posseRequest);
            }
        }

        pagamentoRequest.setClientePagadorCpf(mPagamento.getPagadorCpf());
        pagamentoRequest.setClientePagadorTelefone(mPagamento.getPagadorTelefone());
        pagamentoRequest.setPosses(posseRequests);
        pagamentoRequest.getRequest(pagamentoRequest, httpListenerPagamento);
        pDialog.show();
        pDialog.setMessage("Aguardando liberação...");
    }


    HttpRequestListener httpListenerPagamento = new HttpRequestListener<PagamentoResponse>() {
        @Override
        public void onSuccess(PagamentoResponse response) {

            ProcessarPagamento(response);
        }

        @Override
        public void onError(int code, String msg) {

            //REALIZAR A DEVOLUCAO
            pDialog.dismiss();

            builder.setTitle("Atenção");
            builder.setMessage("Ingresso não liberado,\nInciando Estorno.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Lógica a ser executada quando o botão "Sim" é clicado
                    Intent intent = new Intent(getContext(), PaymentsActivity.class);
                    intent.putExtra(CARD_METODO, 3);
                    startActivityForResult(intent, 1);
                }
            });

            builder.create();
            builder.show();

//            mDialogCustom.show();
//            mDialogCustom.setTitle(mTitleDialog);
//            mDialogCustom.setTitle(mTitleDialog);
//            mDialogCustom.setMessage(msg);
        }
    };

    public void ProcessarPagamento(PagamentoResponse response) {


        pDialog.setMessage("Ingressos liberados...");
        pDialog.show();

        mPagamento.setValorTotalFinal(response.getValorTotal());
        mPagamento.setUuid(UUID.randomUUID().toString());
        mPagamento.setCodigoDigital(response.getCodigo());
        PagamentoRepository pagamentoRepository = new PagamentoRepository(IngressosApplication.getAppDatabase());
        mPagamento.setId(pagamentoRepository.insert(mPagamento));

        mPagamentoItems.forEach(pagamentoItem -> pagamentoItem.setPagamentoId(mPagamento.getId()));

        PagamentoItemRepository pagamentoItemRepository = new PagamentoItemRepository(IngressosApplication.getAppDatabase());
        pagamentoItemRepository.insertAll(mPagamentoItems);

        pDialog.setMessage("Imprimindo ingressos...");
        pDialog.show();

        PrinterService printerService = new PrinterService(this.getContext());
        printerService.printCorteAqui();

        for (PagamentoItem item : mPagamentoItems) {
            int i = 1;
            for (String codigoBilhete : item.getBilhetesCodigos()) {
                PrinterBilhete bilhete = new PrinterBilhete();
                bilhete.setEventoLogo(mPagamento.getEvento().getImagemLogo());
                bilhete.eventoNome = mPagamento.getEvento().getNome();
                bilhete.eventoData = mPagamento.getEvento().getDataInicioDate();
                bilhete.eventoHora = mPagamento.getEvento().getDataInicioHora();
                bilhete.eventoLocal = mPagamento.getEvento().getLocal();
                bilhete.eventoCidade = mPagamento.getEvento().getmEndereco().getCidade();
                bilhete.eventoEstado = mPagamento.getEvento().getmEndereco().getEstado();
                bilhete.bilheteNome = item.getIngressoNome();
                bilhete.bilheteCombo = i + "/" + item.getBilhetesCodigos().size();
                bilhete.bilheteTipo = item.getIngressoTipoValido();
                bilhete.bilheteSexo = item.getIngressoSexo();
                bilhete.bilheteValor = item.getValorFinalMonetary();
                bilhete.bilheteCodigo = codigoBilhete;
                bilhete.bilheteCpf = item.getPosseCpfMascara();
                bilhete.vendaData = mPagamento.getTransacaoData();
                bilhete.vendaHora = mPagamento.getTransacaoHora();
                bilhete.pdvNome = mPagamento.getUsuarioLogado().getPdvNome();

                bilhete.vendaTransacao = mPagamento.getTransacaoCodigo();
                bilhete.vendaFormaPagamento = mPagamento.getTipo().getDescricao();
                bilhete.vendaCodigoPagamento = mPagamento.getCodigoDigital();

                printerService.printBilhete(bilhete);
                try {
                    Thread.sleep(1000); // Aguardar 2 segundos (2000 milissegundos)
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        }

        printerService.onDestroy();

        pDialog.setMessage("Impressão finalizada");
        pDialog.show();

        try {
            Thread.sleep(2000); // Aguardar 2 segundos (2000 milissegundos)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        pDialog.dismiss();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

        requireActivity().finish();
    }
}