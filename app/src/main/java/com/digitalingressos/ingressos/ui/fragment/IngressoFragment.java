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
import com.digitalingressos.ingressos.data.database.sessao.Sessao;
import com.digitalingressos.ingressos.data.database.setor.Setor;
import com.digitalingressos.ingressos.data.database.setor.SetorRequest;
import com.digitalingressos.ingressos.ui.adpater.SetorAdapter;
import com.digitalingressos.ingressos.util.CustomDialog;
import com.digitalingressos.ingressos.util.NavigationHost;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;

import java.util.List;

public class IngressoFragment extends Fragment {

    private CarrinhoViewModel mCarrinhoView;
    Sessao mSessao;
    ProgressDialog pDialog;
    private CustomDialog mDialogCustom;
    private String mTitleDialog;

    View mViewPrincipal;
    RecyclerView mRecyclerView;
    LinearLayout mLayCart;
    LinearLayout mLayBtVoltar;
    LinearLayout mLaybtComprar;

    TextView tvTituloEvento;
    TextView tvValorTotal;


    public IngressoFragment(Sessao sessao, CarrinhoViewModel carrinhoView) {
        mCarrinhoView = carrinhoView;
        mSessao = sessao;
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

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment with the ProductGrid theme
        mViewPrincipal = inflater.inflate(R.layout.fragment_ingresso, container, false);

        mLayCart = mViewPrincipal.findViewById(R.id.layout_carrinho_id);

        //Voltar
        mLayBtVoltar = mLayCart.findViewById(R.id.lay_voltar);
        mLayBtVoltar.setOnClickListener(viewa -> onVoltar(mViewPrincipal)); //SET ACAO
        requireActivity().getOnBackPressedDispatcher().addCallback(callbackPressed);


        //comprar
        mLaybtComprar = mLayCart.findViewById(R.id.lay_comprar);
        mLaybtComprar.setOnClickListener(viewa -> onComprar(mViewPrincipal)); //SET ACAO


        //Titulo
        tvTituloEvento = mViewPrincipal.findViewById(R.id.tv_titulo_evento);
        tvTituloEvento.setText(mSessao.getEvento().getNome());

        //Valor do carrinho
        tvValorTotal = mLayCart.findViewById(R.id.valor_total_carrinho);
        tvValorTotal.setText(mCarrinhoView.getPrecoTotalString());


        // Set up the tool bar
        setUpToolbar(mViewPrincipal);

        initDialogs();
        pDialog.show();

        //recyclerView
        mRecyclerView = mViewPrincipal.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));

        //adpter
        SetorRequest request = new SetorRequest();
        request.getRequest(mSessao.getCodigo(), httpListenerSetor);

        return mViewPrincipal;
    }

    HttpRequestListener httpListenerSetor = new HttpRequestListener<List<Setor>>() {
        @Override
        public void onSuccess(List<Setor> response) {

            response.forEach(a -> a.mSessao = mSessao);
            SetorAdapter adapter = new SetorAdapter(response, mCarrinhoView, mLayCart);
            mRecyclerView.setAdapter(adapter);
            pDialog.dismiss();

        }

        @Override
        public void onError(int code, String msg) {

            pDialog.dismiss();
            mDialogCustom.show();
            mDialogCustom.setMessage(msg);

        }
    };

    public void onVoltar(View view) {
        //ESSE VOLTAR PRECISA
        ((NavigationHost) view.getContext()).navigateTo(new SessaoFragment(mSessao.getEvento(), mCarrinhoView), false);
    }

    public void onComprar(View view) {
        if (mCarrinhoView.getSizeCarrinho() > 0) {
            ((NavigationHost) view.getContext()).navigateTo(new IngressoPosseFragment(mSessao, mCarrinhoView), false);
        } else {
            mDialogCustom.show();
            mDialogCustom.setTitle(mTitleDialog);
            mDialogCustom.setMessage("Carrinho Vazio");
        }
    }

    // This callback will only be called when MyFragment is at least Started.
    OnBackPressedCallback callbackPressed = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            onVoltar(mViewPrincipal);
        }
    };
}