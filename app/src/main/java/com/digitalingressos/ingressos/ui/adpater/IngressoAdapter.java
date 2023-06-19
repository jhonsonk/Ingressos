package com.digitalingressos.ingressos.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.database.ingresso.Ingresso;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

public class IngressoAdapter extends RecyclerView.Adapter<IngressoAdapter.IngressoHolder> {

    private CarrinhoViewModel mCarrinhoView;
    private final List<Ingresso> mIngresso;
    public LinearLayout linearLayoutCart;

    //POSSO RECEBER O CONTEXTO
    public IngressoAdapter(List<Ingresso> ingresso, CarrinhoViewModel _carrinhoViewModel, LinearLayout _linearLayoutCart) {
        mIngresso = ingresso;
        mCarrinhoView = _carrinhoViewModel;
        linearLayoutCart = _linearLayoutCart;
    }

    @Override
    public IngressoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new IngressoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_ingresso, parent, false), linearLayoutCart);
    }

    @Override
    public void onBindViewHolder(IngressoHolder holder, int position) {
        holder.mTvnomeIngresso.setText(mIngresso.get(position).getNome());
        if (mIngresso.get(position).getTipo().equals("Em_Aberto")) {
            holder.mTvTipoIngresso.setText("");
        } else {
            holder.mTvTipoIngresso.setText(mIngresso.get(position).getTipo());
        }
        holder.mTvSexoIngresso.setText(mIngresso.get(position).getSexo());
        holder.mTvValorIngresso.setText(mIngresso.get(position).getValorMonetary());

        long quantidade = mCarrinhoView.getQuantidade(mIngresso.get(position).getValorLoteIngressoUuid());
        if (quantidade > 0) {
            holder.mTvQuantidade.setText(String.valueOf(quantidade));
        } else {
            holder.mTvQuantidade.setText("");
        }

        holder.tvValorTotal.setText(mCarrinhoView.getPrecoTotalString());
        holder.mFabRemover.setOnClickListener(view -> onRemoverItem(mIngresso.get(position), position));
        holder.mFabAdcionar.setOnClickListener(view -> OnInsertItem(mIngresso.get(position), position));
        holder.mLayCardIngresso.setOnClickListener(view -> OnInsertItem(mIngresso.get(position), position));
    }

    @Override
    public int getItemCount() {

        return mIngresso != null ? mIngresso.size() : 0;
    }

    private void OnInsertItem(@NonNull Ingresso ingresso, int position) {
        //CarrinhoEntry carrinhoEntry = new CarrinhoEntry(ingresso, ingresso.getPrecoIngresso());
        mCarrinhoView.insertIngresso(ingresso);
        notifyItemChanged(position);
    }

    private void onRemoverItem(Ingresso ingresso, int position) {
       // CarrinhoEntry carrinhoEntry = new CarrinhoEntry(ingresso, ingresso.getPrecoIngresso());
        mCarrinhoView.removeIngresso(ingresso);
        notifyItemChanged(position);
    }


    /**
     * Holder Ingresso
     */
    public class IngressoHolder extends RecyclerView.ViewHolder {
        public TextView mTvnomeIngresso;
        public TextView mTvTipoIngresso;
        public TextView mTvSexoIngresso;
        public TextView mTvValorIngresso;
        public TextView mTvQuantidade;
        public FloatingActionButton mFabAdcionar;
        public FloatingActionButton mFabRemover;
        public LinearLayout mLayCardIngresso;
        public TextView tvValorTotal;

        public IngressoHolder(View itemView, View viewCarrinho) {
            super(itemView);
            mTvnomeIngresso = itemView.findViewById(R.id.nome_ingresso);
            mTvTipoIngresso = itemView.findViewById(R.id.tipo_ingresso);
            mTvSexoIngresso = itemView.findViewById(R.id.sexo_ingresso);
            mTvValorIngresso = itemView.findViewById(R.id.valor_ingresso);
            mFabAdcionar = itemView.findViewById(R.id.fab_adcionar);
            mFabRemover = itemView.findViewById(R.id.fab_remover);
            mTvQuantidade = itemView.findViewById(R.id.tv_quantidade);
            mLayCardIngresso = itemView.findViewById(R.id.ly_card_ingresso);
            tvValorTotal = viewCarrinho.findViewById(R.id.valor_total_carrinho);
        }
    }
}


