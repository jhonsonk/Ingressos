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
import com.digitalingressos.ingressos.network.HttpRequestListener;
import com.digitalingressos.ingressos.data.database.evento.Evento;
import com.digitalingressos.ingressos.data.database.sessao.Sessao;
import com.digitalingressos.ingressos.data.database.sessao.SessaoRequest;
import com.digitalingressos.ingressos.ui.adpater.SessaoAdapter;
import com.digitalingressos.ingressos.util.CustomDialog;
import com.digitalingressos.ingressos.util.NavigationHost;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;

import java.util.List;

public class SessaoFragment extends Fragment {
    private CarrinhoViewModel mCarrinhoView;
    private Evento mEvento;

    View mViewPrincipal;
    RecyclerView mRecyclerView;
    LinearLayout mLayCart;
    LinearLayout mLayBtVoltar;
    LinearLayout mLayBtComprar;


    ProgressDialog pDialog;
    private CustomDialog mDialogCustom;
    private String mTitleDialog;

    TextView tvTituloEvento;
    TextView tvValorTotal;

    public SessaoFragment(Evento evento) {
        mCarrinhoView = new CarrinhoViewModel();
        mEvento = evento;
        mCarrinhoView.setEvento(evento);
    }

    public SessaoFragment(Evento evento, CarrinhoViewModel carrinhoViewModel) {
        mEvento = evento;
        mCarrinhoView = carrinhoViewModel;
        mCarrinhoView.setEvento(mEvento);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public void initDialogs() {

        pDialog = new ProgressDialog(this.getContext());
        pDialog.setMessage("Carregando...");
        mTitleDialog = "Atenção";
        mDialogCustom = new CustomDialog(this.getContext());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewPrincipal = inflater.inflate(R.layout.fragment_sessao, container, false);

        //Voltar
        mLayCart = mViewPrincipal.findViewById(R.id.layout_carrinho_id);
        mLayBtVoltar = mLayCart.findViewById(R.id.lay_voltar);
        mLayBtComprar = mLayCart.findViewById(R.id.lay_comprar);

        mLayBtComprar.setVisibility(View.INVISIBLE);

        mLayBtVoltar.setOnClickListener(viewa -> onVoltar(mViewPrincipal));
        requireActivity().getOnBackPressedDispatcher().addCallback(callbackPressed);

        //Nome do evento
        tvTituloEvento = mViewPrincipal.findViewById(R.id.tv_titulo_evento);
        tvTituloEvento.setText(mEvento.getNome());

        //Valor do carrinho
        tvValorTotal = mLayCart.findViewById(R.id.valor_total_carrinho);
        tvValorTotal.setText(mCarrinhoView.getPrecoTotalString());

        initDialogs();
        pDialog.show();

        // Set up the tool bar
        setUpToolbar(mViewPrincipal);

        mRecyclerView = mViewPrincipal.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));

        SessaoRequest request = new SessaoRequest();
        request.getRequest(mEvento.getCodigo(), httpListenerSessao);

        return mViewPrincipal;
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    public void onVoltar(View view) {

        ((NavigationHost) view.getContext()).navigateTo(new EventoFragment(), false);
    }

    public void onComprar(View view) {
        if (mCarrinhoView.getSizeCarrinho() > 0) {
            //((NavigationHost) view.getContext()).navigateTo(new IngressoPosseFragment(mSessao, mCarrinhoView), true);
        } else {
            mDialogCustom.show();
            mDialogCustom.setTitle(mTitleDialog);
            mDialogCustom.setMessage("Carrinho Vazio");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    //Botao fisico de voltar
    OnBackPressedCallback callbackPressed = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            onVoltar(mViewPrincipal);
        }
    };

    HttpRequestListener httpListenerSessao = new HttpRequestListener<List<Sessao>>() {
        @Override
        public void onSuccess(List<Sessao> response) {

            //colocar o evento aqui
            response.forEach(a -> a.mEvento = mEvento);
            SessaoAdapter adapter = new SessaoAdapter(response, mCarrinhoView, mLayCart);
            mRecyclerView.setAdapter(adapter);
            pDialog.dismiss();

        }

        @Override
        public void onError(int code, String msg) {

            pDialog.dismiss();
            mDialogCustom.show();
            mDialogCustom.setMessage(msg);
            mDialogCustom.setTitle(mTitleDialog);
        }
    };
}