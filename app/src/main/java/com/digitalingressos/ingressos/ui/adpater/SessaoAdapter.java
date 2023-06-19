package com.digitalingressos.ingressos.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.database.sessao.Sessao;
import com.digitalingressos.ingressos.ui.fragment.IngressoFragment;
import com.digitalingressos.ingressos.util.NavigationHost;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;

import java.util.List;

public class SessaoAdapter extends RecyclerView.Adapter<SessaoAdapter.SessaoHolder> {

    private CarrinhoViewModel mCarrinho;
    private final List<Sessao> mSessao;
    private LinearLayout mLayoutCart;

    public SessaoAdapter(List<Sessao> sessao, CarrinhoViewModel carrinho, LinearLayout linearLayoutCart) {
        mSessao = sessao;
        mCarrinho = carrinho;
        mLayoutCart = linearLayoutCart;
    }

    @Override
    public SessaoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SessaoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_sessao, parent, false), mLayoutCart);
    }

    @Override
    public void onBindViewHolder(SessaoHolder holder, int position) {
        holder.mNomeSessao.setText(mSessao.get(position).getNome());
        holder.mNomeSessao.setOnClickListener(view -> onSelectItem(view, position));
        holder.mTvValorTotal.setText(mCarrinho.getPrecoTotalString());
    }

    @Override
    public int getItemCount() {
        return mSessao != null ? mSessao.size() : 0;
    }

    private void onSelectItem(View view, int position) {

        ((NavigationHost) view.getContext()).navigateTo(new IngressoFragment(mSessao.get(position), mCarrinho), false);
    }

    /**
     * Holder Sessao
     * */
    public class SessaoHolder extends RecyclerView.ViewHolder {

        public TextView mNomeSessao;
        public TextView mTvValorTotal;
        public SessaoHolder(View itemView, View viewCarrinho) {
            super(itemView);
            mNomeSessao = itemView.findViewById(R.id.nome_sessao);
            mTvValorTotal = viewCarrinho.findViewById(R.id.valor_total_carrinho);
        }
    }
}


