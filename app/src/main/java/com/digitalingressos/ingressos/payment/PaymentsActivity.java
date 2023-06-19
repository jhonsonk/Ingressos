package com.digitalingressos.ingressos.payment;

import static com.digitalingressos.ingressos.util.IngressosConstants.CARD_METODO;
import static com.digitalingressos.ingressos.util.IngressosConstants.CREDIT_VALUE;
import static com.digitalingressos.ingressos.util.IngressosConstants.INSTALLMENT_TYPE_PARC_COMPRADOR;
import static com.digitalingressos.ingressos.util.IngressosConstants.INSTALLMENT_TYPE_PARC_VENDEDOR;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutRequest;
import com.digitalingressos.ingressos.databinding.ActivityPaymentsBinding;
import com.digitalingressos.ingressos.util.IngressosConstants;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import javax.inject.Inject;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInstallment;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;
import io.reactivex.annotations.NonNull;

public class PaymentsActivity extends MvpActivity<PaymentsContract, PaymentsPresenter> implements PaymentsContract {

    private static final String TAG = PaymentsActivity.class.getSimpleName();
    //Caixa de dialogo - Remover
    //CustomDialog dialog;

    //valor da transacao
    private int value;

    //numero de parcelas
    private int installmentNumber;

    //Indica que está na tela de parcelas
    private boolean isLayoutInstallment;

    //Tipo do cartao - 1 Debito - 2 Credito
    private int cardMetodo;

    //Injecao dos components
    @Inject
    PaymentsComponent mInjector;

    //Injecao de tela
    private ActivityPaymentsBinding binding;

    private boolean shouldShowDialog;

    //Indica que a transação começou, (senha nao foi digitada)
    private boolean transactionInit = false;

    //Indica que começou o processo de pagamento, (senha ja digitada)
    private boolean transactionInitProcessPayment = false;

    //transacao aprovada
    private boolean transactionAproved = false;

    //transacao reprovada
    private boolean transactionRejected = false;

    //Tipo da parcela, quem paga os juros, Comprador ou vendedor
    private Integer mParcType;
    private boolean mCanClick = true;
    private PlugPagTransactionResult transactionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isLayoutInstallment = false;
        installmentNumber = 1;
        mInjector = DaggerPaymentsComponent.builder().useCaseModule(new UseCaseModule()).wrapperModule(new WrapperModule(getApplicationContext())).build();
        mInjector.inject(this);

        super.onCreate(savedInstanceState);
        viewsInitializer();
        binding = ActivityPaymentsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        initPropertiesAndListeners();
        setUpToolbar();
    }

    private void initPropertiesAndListeners() {

        binding.btCancelarPagamento.setOnClickListener(view -> cancelTransaction());
        binding.btCreditoAvisa.setOnClickListener(view -> doCreditPayment());
        binding.btCreditoComprador.setOnClickListener(view -> doCreditPaymentCustomer());
        binding.btCreditoVendedor.setOnClickListener(view -> doCreditPaymentSeller());

        binding.tvValor.setText(getValueString());

        mCanClick = false;
        shouldShowDialog = true;
        switch (cardMetodo) {
            case 0://PIX
                binding.layAppBar.setVisibility(View.GONE);
                binding.layCredito.setVisibility(View.GONE);
                binding.layCentre.setVisibility(View.VISIBLE);
                binding.layParcelas.setVisibility(View.GONE);
                binding.tvMetodo.setText("PIX");
                getPresenter().doPixPayment(getValue());
                break;

            case 1://DEBITO
                binding.layAppBar.setVisibility(View.GONE);
                binding.layCredito.setVisibility(View.GONE);
                binding.layCentre.setVisibility(View.VISIBLE);
                binding.layParcelas.setVisibility(View.GONE);
                binding.tvMetodo.setText("DEBITO");
                getPresenter().doDebitPayment(getValue());
                break;

            case 2://CREDIT CARD
                binding.layCredito.setVisibility(View.VISIBLE);
                binding.layCentre.setVisibility(View.GONE);
                binding.layParcelas.setVisibility(View.GONE);
                break;
            case 3://REFUND
                binding.layAppBar.setVisibility(View.GONE);
                binding.layCredito.setVisibility(View.GONE);
                binding.layCentre.setVisibility(View.VISIBLE);
                binding.layParcelas.setVisibility(View.GONE);
                binding.tvMetodo.setText("Estorno");
                binding.tvMetodo.setVisibility(View.VISIBLE);
                ActionResult actionResult = FileHelper.readFromFile(this);
                doRefund(actionResult);
                break;
            case 4://PRINT CustomerReceipt
                binding.layAppBar.setVisibility(View.GONE);
                binding.layCredito.setVisibility(View.GONE);
                binding.layCentre.setVisibility(View.GONE);
                binding.layParcelas.setVisibility(View.GONE);
                binding.btCancelarPagamento.setVisibility(View.GONE);
                shouldShowDialog = false;
                mCanClick = false;
                getPresenter().printCustomerReceipt();
                break;

            case 5://PRINT StablishmentReceipt
                binding.layAppBar.setVisibility(View.GONE);
                binding.layCredito.setVisibility(View.GONE);
                binding.layCentre.setVisibility(View.GONE);
                binding.layParcelas.setVisibility(View.GONE);
                binding.btCancelarPagamento.setVisibility(View.GONE);
                shouldShowDialog = false;
                mCanClick = false;
                getPresenter().printStablishmentReceipt();
                break;
        }
    }

    private void viewsInitializer() {
        //REMOVER
        //dialog = new CustomDialog(this);
        //dialog.setOnCancelListener(cancelListener);

        //PUT OS DADOS DA COMPRA
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        value = extras.getInt(CREDIT_VALUE);
        cardMetodo = extras.getInt(CARD_METODO);
    }

    public void setTransactionInit(boolean value) {

        transactionInit = value;
    }

    private void startCalculateInstallments() {
        isLayoutInstallment = true;
        getPresenter().calculateInstallments(getValue(), mParcType);
    }

    @NonNull
    @Override
    public PaymentsPresenter createPresenter() {
        return mInjector.presenter();
    }

    @Override
    public void setUpAdapter(List<PlugPagInstallment> installments) {

        installments.add(0, new PlugPagInstallment(1, getValue(), getValue()));
        RecyclerView recyclerView = binding.listInstallments;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
        InstallmentsAdapter adapter = new InstallmentsAdapter(installments, itemClick -> {
            installmentNumber = itemClick;
            doCreditPaymentInstallments();
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMessage(String msg) {
//         if (shouldShowDialog && !dialog.isShowing()) {
//             dialog.show();
//          }
//        dialog.setMessage(msg);
    }

    @Override
    public void printReceiptSuccess() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(IngressosConstants.PAYMENT_PRINT_RECEIPT, "OK");
        setResult(Activity.RESULT_OK, returnIntent);
        showLoading(false);
        finish();
    }

    @Override
    public void showLoading(Boolean show) {
        if (show) {
            UIFeedback.showProgress(this);
        } else {
            UIFeedback.dismissProgress();
        }
    }

    @Override
    public void showLoading(Boolean show, String msg) {
        if (show) {
            UIFeedback.showProgress(this,msg);
        } else {
            UIFeedback.dismissProgress();
        }
    }

    public void disposeDialog() {
        mCanClick = true;
        shouldShowDialog = false;
        UIFeedback.dismissDialog();
    }

    @Override
    public void disposePayment() {
        getPresenter().abortTransaction();
        disposeDialog();
    }

    public void setTransactionResult(PlugPagTransactionResult result) {

        transactionResult = result;
    }

    public void setTransactionAproved(boolean value) {

        transactionAproved = value;
    }

    public void setTransactionRejected(boolean value) {

        transactionRejected = value;
    }

    public void setTransactionInitProcessPayment(boolean value) {

        transactionInitProcessPayment = value;
    }


    /*Meios de pagamento*/
    public void doCreditPayment() {
        binding.layAppBar.setVisibility(View.GONE);
        binding.layCredito.setVisibility(View.GONE);
        binding.layCentre.setVisibility(View.VISIBLE);
        binding.layParcelas.setVisibility(View.GONE);
        binding.tvMetodo.setText("CREDITO À VISTA");
        getPresenter().doCreditPaymentInCash(getValue());
    }

    public void doCreditPaymentInstallments() {
        binding.layAppBar.setVisibility(View.GONE);
        binding.layCredito.setVisibility(View.GONE);
        binding.layCentre.setVisibility(View.VISIBLE);
        binding.layParcelas.setVisibility(View.GONE);

        binding.tvMetodo.setText("CREDITO PARC. " + installmentNumber + " Vezes");

        if (INSTALLMENT_TYPE_PARC_VENDEDOR == mParcType) {
            getPresenter().doCreditPaymentSellerInstallments(getValue(), installmentNumber);

        } else {
            getPresenter().doCreditPaymentBuyerInstallments(getValue(), installmentNumber);
        }
    }


    public void doCreditPaymentCustomer() {
        binding.layAppBar.setVisibility(View.VISIBLE);
        binding.layCredito.setVisibility(View.GONE);
        binding.layCentre.setVisibility(View.GONE);
        binding.layParcelas.setVisibility(View.VISIBLE);
        mParcType = INSTALLMENT_TYPE_PARC_COMPRADOR;
        startCalculateInstallments();

    }

    public void doCreditPaymentSeller() {
        binding.layAppBar.setVisibility(View.VISIBLE);
        binding.layCredito.setVisibility(View.GONE);
        binding.layCentre.setVisibility(View.GONE);
        binding.layParcelas.setVisibility(View.VISIBLE);
        mParcType = INSTALLMENT_TYPE_PARC_VENDEDOR;
        startCalculateInstallments();
    }

    public void doRefund(ActionResult actionResult) {
        getPresenter().doRefund(actionResult);
    }


    private int getValue() {
        return value;
    }

    private String getValueString() {
        return String.format("VALOR: R$%.2f", (getValue() / 100.0));
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(binding.txtTotalValue, InputMethodManager.SHOW_FORCED);
    }


    @Override
    public void writeToFile(String transactionCode, String transactionId) {
        FileHelper.writeToFile(transactionCode, transactionId, this);
    }

    @Override
    public void setPaymentValue(String value) {
        binding.tvValor.setText(value);
    }

    //toolbar
    private void setUpToolbar() {
        Toolbar toolbar = binding.appBar;
        this.setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                if (isLayoutInstallment) {
                    isLayoutInstallment = false;
                    binding.layAppBar.setVisibility(View.VISIBLE);
                    binding.layCredito.setVisibility(View.VISIBLE);
                    binding.layCentre.setVisibility(View.GONE);
                    binding.layParcelas.setVisibility(View.GONE);

                } else {
                    onBackPressed();
                }
            }
        });
    }

    //SERA REMOVIDA
    DialogInterface.OnCancelListener cancelListener = dialogInterface -> {
        dialogInterface.dismiss();
        if (shouldShowDialog) {
            getPresenter().abortTransaction();
        }
    };

    //BOTAO DE CANCELAR
    public void cancelTransaction() {

        if (transactionInit) {
            disposePayment();
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        UIFeedback.releaseVariables();
        super.onDestroy();
    }


    @Override
    public void showTransactionSuccess() {
        mCanClick = true;
        binding.tvMessage1.setVisibility(View.INVISIBLE);
        binding.tvMessage2.setVisibility(View.VISIBLE);
        binding.tvSenha.setVisibility(View.INVISIBLE);
        binding.tvValor.setVisibility(View.INVISIBLE);
        binding.tvMetodo.setVisibility(View.INVISIBLE);

        binding.progressesIndicator.setVisibility(View.INVISIBLE);
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);

        binding.layPagamento.setBackgroundResource(R.color.green_700);
        binding.imgPagamento.setImageResource(R.drawable.payment_authorized_48);
        binding.tvMessage2.setText(R.string.transactions_successful);

        //transactionResult
        Intent returnIntent = new Intent();

        Log.d(TAG, "showTransactionSuccess: " + transactionResult.toString());
        returnIntent.putExtra(IngressosConstants.PAYMENT_ERROR_CODE, transactionResult.getErrorCode());
        returnIntent.putExtra(IngressosConstants.PAYMENT_TRANSACTION_CODE, transactionResult.getTransactionCode());
        returnIntent.putExtra(IngressosConstants.PAYMENT_TRANSACTION_ID, transactionResult.getTransactionId());
        returnIntent.putExtra(IngressosConstants.PAYMENT_DATE, transactionResult.getDate());
        returnIntent.putExtra(IngressosConstants.PAYMENT_TIME, transactionResult.getTime());
        returnIntent.putExtra(IngressosConstants.PAYMENT_CARD_BRAND, transactionResult.getCardBrand());
        returnIntent.putExtra(IngressosConstants.PAYMENT_BIN, transactionResult.getBin());
        returnIntent.putExtra(IngressosConstants.PAYMENT_HOLDER, transactionResult.getHolder());
        returnIntent.putExtra(IngressosConstants.PAYMENT_TERMINAL_SERIAL_NUMBER, transactionResult.getTerminalSerialNumber());
        returnIntent.putExtra(IngressosConstants.PAYMENT_AMOUNT, transactionResult.getAmount());
        returnIntent.putExtra(IngressosConstants.PAYMENT_LABEL, transactionResult.getLabel());
        returnIntent.putExtra(IngressosConstants.PAYMENT_READER_MODEL, transactionResult.getReaderModel());
        returnIntent.putExtra(IngressosConstants.PAYMENT_HOST_NSU, transactionResult.getHostNsu());
        returnIntent.putExtra(IngressosConstants.PAYMENT_NSU, transactionResult.getNsu());
        returnIntent.putExtra(IngressosConstants.PAYMENT_AUTO_CODE, transactionResult.getAutoCode());
        returnIntent.putExtra(IngressosConstants.PAYMENT_INSTALMENTS, installmentNumber);
        returnIntent.putExtra(IngressosConstants.PAYMENT_ORIGINAL_AMOUNT, transactionResult.getOriginalAmount());
        returnIntent.putExtra(IngressosConstants.PAYMENT_TYPE_TRANSACTION, transactionResult.getTypeTransaction());


        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void showTransactionDialog(String message) {

        binding.tvMessage1.setVisibility(View.VISIBLE);
        binding.tvMessage2.setVisibility(View.GONE);

        binding.tvValor.setVisibility(View.GONE);
        binding.tvMetodo.setVisibility(View.GONE);
        binding.tvSenha.setVisibility(View.GONE);

        binding.progressesIndicator.setVisibility(View.VISIBLE);

        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.layPagamento.setBackgroundResource(R.color.purple_900);
        binding.imgPagamento.setImageResource(R.drawable.payment_init_48);

        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);

        binding.tvMessage1.setText(message);
    }

    //PARTE 1
    public void showInitDialog() {
        binding.tvSenha.setVisibility(View.INVISIBLE);
        binding.tvValor.setVisibility(View.VISIBLE);
        binding.tvMetodo.setVisibility(View.VISIBLE);
        binding.tvMessage1.setVisibility(View.VISIBLE);
        binding.tvMessage2.setVisibility(View.INVISIBLE);

        binding.progressesIndicator.setVisibility(View.VISIBLE);
        binding.btCancelarPagamento.setVisibility(View.VISIBLE);

        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.layPagamento.setBackgroundResource(R.color.purple_900);
        binding.imgPagamento.setImageResource(R.drawable.payment_init_48);
        binding.tvMessage2.setText(R.string.transactions_initiate);

    }

    @Override
    public void showWaitDialog(String message) {

        if (transactionInit) {
            //TEXTO
            binding.tvMessage1.setVisibility(View.VISIBLE);
            binding.tvMessage2.setVisibility(View.GONE);
            binding.tvSenha.setVisibility(View.GONE);
            binding.tvValor.setVisibility(View.GONE);
            binding.tvMetodo.setVisibility(View.GONE);
            //Botao
            binding.btCancelarPagamento.setVisibility(View.INVISIBLE);
            if (transactionInitProcessPayment) {
                binding.imgPagamento.setImageResource(R.drawable.payment_processing_48);
            } else {
                binding.imgPagamento.setImageResource(R.drawable.payment_init_48);
            }
        } else {
            binding.tvMessage1.setVisibility(View.VISIBLE);
            binding.tvMessage2.setVisibility(View.GONE);
            binding.tvSenha.setVisibility(View.GONE);
            binding.tvValor.setVisibility(View.GONE);
            binding.tvMetodo.setVisibility(View.GONE);
            //Botao
            binding.btCancelarPagamento.setVisibility(View.VISIBLE);
            binding.imgPagamento.setImageResource(R.drawable.payment_init_48);
        }

        //Imagem central e load
        binding.progressesIndicator.setVisibility(View.VISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);

        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.layPagamento.setBackgroundResource(R.color.purple_900);

        //MENSAGEM
        binding.tvMessage1.setText(message);
    }

    //PARTE 2 -- INSIRA O CARTAO
    @Override
    public void showInsertCardDialog() {

        transactionInit = true;
        //TEXTO
        binding.tvMessage1.setVisibility(View.VISIBLE);
        binding.tvMessage2.setVisibility(View.GONE);
        binding.tvSenha.setVisibility(View.GONE);
        binding.tvValor.setVisibility(View.VISIBLE);
        binding.tvMetodo.setVisibility(View.VISIBLE);

        //imagem
        binding.progressesIndicator.setVisibility(View.INVISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_insert_card_48);

        //botao
        binding.btCancelarPagamento.setVisibility(View.VISIBLE);

        //cor de fundo
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.layPagamento.setBackgroundResource(R.color.purple_900);

        //TEXTO
        binding.tvMessage1.setText(R.string.transactions_insert_card);

    }

    //PARTE 3 -- DIGITE A SENHA
    @Override
    public void showPasswordDialog(String message) {

        //TEXTO
        binding.tvMessage1.setVisibility(View.GONE);
        binding.tvMessage2.setVisibility(View.VISIBLE);
        binding.tvSenha.setVisibility(View.VISIBLE);
        binding.tvValor.setVisibility(View.VISIBLE);
        binding.tvMetodo.setVisibility(View.VISIBLE);

        //IMAGEM
        binding.progressesIndicator.setVisibility(View.GONE);
        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_password_48);

        //BOTAO
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);

        //PADDING
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin_small), 0, 0);

        //COR DE FUNDO
        binding.layPagamento.setBackgroundResource(R.color.purple_900);

        //MENSAGEM
        binding.tvSenha.setText(message);
    }

    //PARTE 4 - PROCESSANDO PAGAMENTO
    @Override
    public void showProcessPaymentDialog() {

        binding.tvMessage1.setVisibility(View.VISIBLE);
        binding.tvMessage2.setVisibility(View.GONE);
        binding.tvSenha.setVisibility(View.GONE);
        binding.tvValor.setVisibility(View.GONE);
        binding.tvMetodo.setVisibility(View.GONE);

        //imag
        binding.progressesIndicator.setVisibility(View.VISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_processing_48);

        //botao
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);

        //COR DE FUNDO PADDGIN
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.layPagamento.setBackgroundResource(R.color.purple_900);

        binding.tvMessage1.setText("Processando pagamento");
    }

    //PARTE 5 - TRANSACAO APROVADA
    @Override
    public void showAuthorizedDialog() {

        binding.tvMessage1.setVisibility(View.INVISIBLE);
        binding.tvMessage2.setVisibility(View.VISIBLE);
        binding.tvSenha.setVisibility(View.INVISIBLE);
        binding.tvValor.setVisibility(View.INVISIBLE);
        binding.tvMetodo.setVisibility(View.INVISIBLE);

        binding.progressesIndicator.setVisibility(View.INVISIBLE);
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_authorized_48);

        binding.layPagamento.setBackgroundResource(R.color.green_700);
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.tvMessage2.setText(R.string.transactions_authorized);
    }

    //PARTE 6 -- Nao autorizado
    @Override
    public void showUnauthorizedDialog() {

        binding.tvMessage1.setVisibility(View.INVISIBLE);
        binding.tvMessage2.setVisibility(View.VISIBLE);
        binding.tvSenha.setVisibility(View.INVISIBLE);
        binding.tvValor.setVisibility(View.INVISIBLE);
        binding.tvMetodo.setVisibility(View.INVISIBLE);

        binding.progressesIndicator.setVisibility(View.GONE);
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_unauthorized_48);

        binding.layPagamento.setBackgroundResource(R.color.red_700);
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.tvMessage2.setText(R.string.transactions_unauthorized);
    }

    //PARTE 7 - Execepation
    @Override
    public void showUnauthorizedExceptionDialog(String message) {

        binding.tvMessage1.setVisibility(View.INVISIBLE);
        binding.tvMessage2.setVisibility(View.VISIBLE);
        binding.tvSenha.setVisibility(View.INVISIBLE);
        binding.tvValor.setVisibility(View.INVISIBLE);
        binding.tvMetodo.setVisibility(View.INVISIBLE);

        binding.progressesIndicator.setVisibility(View.GONE);
        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);
        binding.imgPagamento.setVisibility(View.VISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_unauthorized_48);

        binding.layPagamento.setBackgroundResource(R.color.red_700);
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);
        binding.tvMessage2.setText(message);
    }

    //PARTE 8 - REMOVA O CARTAO
    @Override
    public void showRemoveCardDialog() {
        binding.tvMessage1.setVisibility(View.GONE);
        binding.tvMessage2.setVisibility(View.VISIBLE);

        binding.tvValor.setVisibility(View.GONE);
        binding.tvMetodo.setVisibility(View.GONE);

        binding.tvSenha.setVisibility(View.GONE);
        binding.tvMessage2.setText("Retire o cartão");

        binding.progressesIndicator.setVisibility(View.INVISIBLE);
        binding.layPagamento.setBackgroundResource(R.color.purple_900);
        binding.layCentre.setPaddingRelative(0, (int) getResources().getDimension(R.dimen.payment_top_margin), 0, 0);

        binding.btCancelarPagamento.setVisibility(View.INVISIBLE);
        binding.imgPagamento.setImageResource(R.drawable.payment_insert_card_48);
    }
}