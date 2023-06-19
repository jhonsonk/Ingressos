package com.digitalingressos.ingressos.ui.fragment;

import android.app.ProgressDialog;
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

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.application.IngressosApplication;
import com.digitalingressos.ingressos.data.database.AppDatabase;
import com.digitalingressos.ingressos.data.database.auth.Auth;
import com.digitalingressos.ingressos.data.database.caixa.Caixa;
import com.digitalingressos.ingressos.data.database.caixa.CaixaRepository;
import com.digitalingressos.ingressos.data.database.checkout.Checkout;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutItem;
import com.digitalingressos.ingressos.data.database.evento.EventoRequest;
import com.digitalingressos.ingressos.data.database.pagamento.PagamentoRepository;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.printer.PrinterService;
import com.digitalingressos.ingressos.ui.activity.MainActivity;
import com.digitalingressos.ingressos.ui.activity.SettingActivity;
import com.digitalingressos.ingressos.ui.adpater.EventoAdapter;
import com.digitalingressos.ingressos.util.CustomDialog;
import com.digitalingressos.ingressos.util.NavigationHost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class FecharCaixaFragment extends Fragment {


    private TextView tvDinheiroQtd;
    private TextView tvDinheiroValor;

    private TextView tvDebitoQtd;
    private TextView tvDebitoValor;

    private TextView tvCreditoQtd;
    private TextView tvCreditoValor;

    private TextView tvPixQtd;
    private TextView tvPixValor;

    private TextView tvQtdTotal;
    private TextView tvValorTotal;

    private Button btnFecharCaixa;
    private Button btnCancelar;

    Caixa mCaixa;

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Iniciar a nova Activity aqui
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public FecharCaixaFragment() {

    }

    public void initFechamentoCaixa() {

        AppDatabase db = IngressosApplication.getAppDatabase();
        Auth auth = IngressosApplication.getAppAuth();

        auth.getUsuarioUuid();

        CaixaRepository caixaRepository = new CaixaRepository(db);
        Caixa caixa = caixaRepository.mCaixaDao.obterAberto(auth.getUsuarioUuid());

        if (caixa != null) {
            caixa.setAuth(auth);
            PagamentoRepository pagamentoRepository = new PagamentoRepository(db);
            caixa.realizarCalculoFechamento(pagamentoRepository.obterPagamentosCaixa(caixa.getId()));
        }

        this.mCaixa = caixa;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_fechar_caixa, container, false);

        initFechamentoCaixa();

        tvDinheiroQtd = view.findViewById(R.id.tv_dinheiro_qtd);
        tvDinheiroQtd.setText(Integer.toString(mCaixa.getQuantidadeVendasDinheiro()));

        tvDinheiroValor = view.findViewById(R.id.tv_dinheiro_valor);
        tvDinheiroValor.setText(mCaixa.getTotalVendasDinheiroMonetary());

        tvDebitoQtd = view.findViewById(R.id.tv_debito_qtd);
        tvDebitoQtd.setText((Integer.toString(mCaixa.getQuantidadeVendasDebito())));

        tvDebitoValor = view.findViewById(R.id.tv_debito_valor);
        tvDebitoValor.setText(mCaixa.getTotalVendasDebitoMonetary());

        tvCreditoQtd = view.findViewById(R.id.tv_credito_qtd);
        tvCreditoQtd.setText((Integer.toString(mCaixa.getQuantidadeVendasCredito())));

        tvCreditoValor = view.findViewById(R.id.tv_credito_valor);
        tvCreditoValor.setText(mCaixa.getTotalVendasCreditoMonetary());

        tvPixQtd = view.findViewById(R.id.tv_pix_qtd);
        tvPixQtd.setText((Integer.toString(mCaixa.getQuantidadeVendasPix())));

        tvPixValor = view.findViewById(R.id.tv_pix_valor);
        tvPixValor.setText(mCaixa.getTotalVendasPixMonetary());

        tvQtdTotal = view.findViewById(R.id.tv_qtd_total);
        tvQtdTotal.setText((Integer.toString(mCaixa.getQuantidadeTotalVendas())));

        tvValorTotal = view.findViewById(R.id.tv_valor_total);
        tvValorTotal.setText(mCaixa.getTotalVendasMonetary());

        btnFecharCaixa = view.findViewById(R.id.bt_fechar_caixar);
        btnFecharCaixa.setOnClickListener(onClickFecharCaixa);

        btnCancelar = view.findViewById(R.id.bt_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar);

        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

        setUpToolbar(view);
        //pDialog.dismiss();
        return view;
    }

    View.OnClickListener onClickFecharCaixa = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ProgressDialog pDialog = new ProgressDialog(v.getContext());
            pDialog.setMessage("Aguarde...");
            pDialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Realize as ações que você deseja executar após 2 segundos
                    mCaixa.fecharCaixa();
                    CaixaRepository caixaRepository = new CaixaRepository(IngressosApplication.getAppDatabase());
                    caixaRepository.mCaixaDao.insert(mCaixa);
                    PrinterService printerService = new PrinterService(IngressosApplication.getAppContext());
                    printerService.printFechamentoCaixa(mCaixa);
                    IngressosApplication.setAuthHasExpired();
                    pDialog.dismiss();
                    CustomDialog mCustom = new CustomDialog(v.getContext());
                    mCustom.show();
                    mCustom.setTitle("Caixa Fechado");
                    mCustom.setMessage("Você será deslogado do sistema.");
                    mCustom.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // Lógica a ser executada quando o diálogo for cancelado
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
            }, 2000);
        }
    };

    View.OnClickListener onClickCancelar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
            getActivity().finish();

        }
    };
}