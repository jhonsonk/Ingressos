package com.digitalingressos.ingressos.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.IngressoPosse;
import com.digitalingressos.ingressos.data.database.checkout.Checkout;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutItem;
import com.digitalingressos.ingressos.data.database.checkout.CheckoutRequest;
import com.digitalingressos.ingressos.data.database.sessao.Sessao;
import com.digitalingressos.ingressos.data.database.setor.Setor;
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.ui.adpater.IngressoPosseAdapter;
import com.digitalingressos.ingressos.ui.adpater.SetorAdapter;
import com.digitalingressos.ingressos.util.CustomDialog;
import com.digitalingressos.ingressos.util.NavigationHost;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class IngressoPosseFragment extends Fragment {
    private CarrinhoViewModel mCarrinho;
    //usado para voltar
    Sessao mSessao;
    ProgressDialog pDialog;
    private CustomDialog mDialogCustom;
    private String mTitleDialog;

    View mViewPrincipal;
    RecyclerView mRecyclerView;
    LinearLayout mLayCart;
    LinearLayout mLayBtVoltar;
    LinearLayout mLaybtComprar;

    TextView mTvTituloEvento;
    TextView mTvValorTotal;
    IngressoPosseAdapter mAdapter;


    public IngressoPosseFragment(Sessao sessaoEntry, CarrinhoViewModel carrinho) {
        mCarrinho = carrinho;
        mSessao = sessaoEntry;
    }

    public void initDialogs() {

        pDialog = new ProgressDialog(this.getContext());
        pDialog.setMessage("Carregando...");
        mTitleDialog = "Atenção";
        mDialogCustom = new CustomDialog(this.getContext());
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mViewPrincipal = inflater.inflate(R.layout.fragment_ingresso_posse, container, false);

        initDialogs();
        pDialog.show();

        mLayCart = mViewPrincipal.findViewById(R.id.layout_carrinho_id);
        mLayBtVoltar = mLayCart.findViewById(R.id.lay_voltar);
        mLayBtVoltar.setOnClickListener(viewa -> onVoltar(mViewPrincipal));
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);


        //comprar
        mLaybtComprar = mLayCart.findViewById(R.id.lay_comprar);
        mLaybtComprar.setOnClickListener(viewa -> onComprar(mViewPrincipal)); //SET ACAO


        //Titulo
        mTvTituloEvento = mViewPrincipal.findViewById(R.id.tv_titulo_evento);
        mTvTituloEvento.setText(mCarrinho.getEvento().getNome());

        //Valor do carrinho
        mTvValorTotal = mLayCart.findViewById(R.id.valor_total_carrinho);
        mTvValorTotal.setText(mCarrinho.getPrecoTotalMonetary());

        setUpToolbar(mViewPrincipal);

        mRecyclerView = mViewPrincipal.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));


        mAdapter = new IngressoPosseAdapter(mCarrinho, mLayCart);
        mRecyclerView.setAdapter(mAdapter);

        pDialog.dismiss();

        return mViewPrincipal;
    }

    public void onVoltar(View view) {
        ((NavigationHost) view.getContext()).navigateTo(new IngressoFragment(mSessao, mCarrinho), false);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            onVoltar(mViewPrincipal);
        }
    };

    public void onComprar(View view) {

        if (mCarrinho.isTodosIngressosPreenchidos()) {
            pDialog.show();

            try {
                IngressoPosse ingressosDoComprador = mCarrinho.getSelectedItemList().stream().filter(IngressoPosse::isComprador).collect(Collectors.toList()).get(0);
                mCarrinho.setPagadorPadrao(ingressosDoComprador);
            } catch (Exception e) {
                mCarrinho.setPagadorPadrao(mCarrinho.getSelectedItemList().get(0));
            }

            CheckoutRequest.checkout(mCarrinho.getCheckoutRequest(), httpListenerCheckout);
        } else {
            pDialog.hide();
            mDialogCustom.show();
            mDialogCustom.setTitle(mTitleDialog);
            mDialogCustom.setMessage("O carrinho possui ingressos sem posse ou inválidos");
        }
    }


    HttpRequestListener httpListenerCheckout = new HttpRequestListener<Checkout>() {
        @Override
        public void onSuccess(Checkout response) {

            Gson gson = new GsonBuilder().create();
            List<CheckoutItem> checkoutItem = gson.fromJson(response.getItens(), CheckoutItem.getListType());
            response.setCheckoutItem(checkoutItem);
            mCarrinho.setCheckout(response);

            ((NavigationHost) getContext()).navigateTo(new ResumoCompraFragment(mSessao, mCarrinho), false);
            pDialog.dismiss();
        }

        @Override
        public void onError(int code, String msg) {

            pDialog.dismiss();
            mDialogCustom.show();
            mDialogCustom.setMessage(msg);
        }
    };
}