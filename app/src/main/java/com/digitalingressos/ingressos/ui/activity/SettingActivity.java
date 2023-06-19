package com.digitalingressos.ingressos.ui.activity;

import static com.digitalingressos.ingressos.util.IngressosConstants.CARD_METODO;
import static com.digitalingressos.ingressos.util.IngressosConstants.CREDIT_VALUE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.AppDatabase;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.data.database.caixa.CaixaRepository;
import com.digitalingressos.ingressos.data.database.enumeradores.StatusPagamento;
import com.digitalingressos.ingressos.data.database.enumeradores.TipoPagamento;
import com.digitalingressos.ingressos.data.database.pagamento.Pagamento;
import com.digitalingressos.ingressos.data.database.pagamento.PagamentoRepository;
import com.digitalingressos.ingressos.data.database.pagamento.PagamentoRequest;
import com.digitalingressos.ingressos.data.database.pagamento.PagamentoResponse;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.payment.ActionResult;
import com.digitalingressos.ingressos.payment.FileHelper;
import com.digitalingressos.ingressos.payment.PaymentsActivity;
import com.digitalingressos.ingressos.ui.fragment.EventoFragment;
import com.digitalingressos.ingressos.ui.fragment.FecharCaixaFragment;
import com.digitalingressos.ingressos.ui.fragment.SessaoFragment;
import com.digitalingressos.ingressos.util.IngressosConstants;
import com.digitalingressos.ingressos.util.NavigationHost;

public class SettingActivity extends AppCompatActivity implements NavigationHost {

    Pagamento _pagamento;
    AlertDialog.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setUpToolbar();
        mBuilder = new AlertDialog.Builder(this);
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void onClickFecharCaixa(View view) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, new FecharCaixaFragment()).commit();
    }

    public void onClickReimprimirComprovante(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Reimprimir comprovante");
        builder.setMessage("Qual via deseja imprimir?");
        builder.setPositiveButton("CLIENTE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica a ser executada quando o botão "Sim" é clicado
                Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
                intent.putExtra(CARD_METODO, 4);
                startActivityForResult(intent, 1);
            }
        });
        builder.setNegativeButton("ESTABELECIMENTO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
                intent.putExtra(CARD_METODO, 5);
                startActivityForResult(intent, 1);
            }
        });
        builder.create();
        builder.show();


    }

    public void onClickEstornarCartao(View view) {
        // Lógica do clique do botão "Fechar Caixa"
        Intent intent = new Intent(view.getContext(), PaymentsActivity.class);
        ActionResult actionResult = FileHelper.readFromFile(this);
        PagamentoRepository pagamentoRepository = new PagamentoRepository(IngressosApplication.getAppDatabase());
        _pagamento = pagamentoRepository.obterPorTransacaoCodigo(actionResult.getTransactionCode());

        //if (_pagamento != null) {
        //   if (_pagamento.getStatus().equals(StatusPagamento.PAGO)) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Atenção");
        builder.setMessage("Deseja estornar a transação " + actionResult.getTransactionId() + "?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica a ser executada quando o botão "Sim" é clicado
                intent.putExtra(CARD_METODO, 3);
                startActivityForResult(intent, 1);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica a ser executada quando o botão "Não" é clicado
            }
        });
        builder.create();
        builder.show();
//            } else {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.setTitle("Confirmação");
//                builder.setMessage("Pagamento se encontra como " + _pagamento.getStatus());
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Lógica a ser executada quando o botão "Sim" é clicado
//
//                    }
//                });
//                builder.create();
//                builder.show();
//            }
//        } else {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//            builder.setTitle("Confirmação");
//            builder.setMessage("Não possui pagamento para estornar");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // Lógica a ser executada quando o botão "Sim" é clicado
//
//                }
//            });
//            builder.create();
//            builder.show();
//
//        } } else {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//            builder.setTitle("Confirmação");
//            builder.setMessage("Não possui pagamento para estornar");
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // Lógica a ser executada quando o botão "Sim" é clicado
//
//                }
//            });
//            builder.create();
//            builder.show();
//
//        }
    }

    public void onClickLogout(View view) {

        IngressosApplication.setAuthHasExpired();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    ProgressDialog pDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            String printReceipt = data.getStringExtra(IngressosConstants.PAYMENT_PRINT_RECEIPT);
            if (printReceipt != null) {
                return;
            }

            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Aguarde...");
            pDialog.show();

            String codigoTransacao = data.getStringExtra(IngressosConstants.PAYMENT_TRANSACTION_CODE);
            if (_pagamento != null) {
                if (_pagamento.getTransacaoCodigo().equals(codigoTransacao)) {
                    PagamentoRepository pagamentoRepository = new PagamentoRepository(IngressosApplication.getAppDatabase());
                    pagamentoRepository.estornarPagamento(_pagamento);
                    PagamentoRequest pagamentoRequest = new PagamentoRequest();
                    pagamentoRequest.estornar(_pagamento.getCodigoDigital(), httpRequestListener);
                } else {
                    pDialog.setMessage("Ocorreu um erro.");
                    pDialog.dismiss();
                }
            }

            pDialog.dismiss();
        }
    }

    HttpRequestListener<String> httpRequestListener = new HttpRequestListener<String>() {
        @Override
        public void onSuccess(String response) {

            pDialog.dismiss();

            mBuilder.setTitle("Confirmação");
            mBuilder.setMessage("Estorno realizado com sucesso");
            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Lógica a ser executada quando o botão "Sim" é clicado

                }
            });
            mBuilder.create();
            mBuilder.show();
        }

        @Override
        public void onError(int statusCode, String mensagem) {
            pDialog.setMessage(mensagem);
            pDialog.show();
        }
    };


    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}