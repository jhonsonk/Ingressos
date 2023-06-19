package com.digitalingressos.ingressos.ui.adpater;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.ResumoCompra;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;

import java.util.List;

public class ResumoCompraAdapter extends RecyclerView.Adapter<ResumoCompraAdapter.ResumoCompraHolder> {
    private CarrinhoViewModel mCarrinho;
    private List<ResumoCompra> mResumoCompra;
    public ResumoCompraAdapter(List<ResumoCompra> _resumoCompra, CarrinhoViewModel _carrinho) {

        mResumoCompra = _resumoCompra;
        mCarrinho = _carrinho;
    }

    @Override
    public ResumoCompraHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ResumoCompraHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_resumo_compra, parent, false));
    }

    @Override
    public void onBindViewHolder(ResumoCompraHolder holder, int position) {
        holder.tvIngressoNome.setText(mResumoCompra.get(position).getResumoIngresso());
        holder.tvIngressoQuantidade.setText(mResumoCompra.get(position).getQuantidade() + "X ");
        holder.tvIngressoValor.setText(mResumoCompra.get(position).getValorTotal());

        RecyclerView recyclerView = holder.rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(holder.rootView.getContext(), 1, RecyclerView.VERTICAL, false));
        ResumoCompradorAdapter adapter = new ResumoCompradorAdapter(mResumoCompra.get(position).getPosse());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {

        return mResumoCompra != null ? mResumoCompra.size() : 0;
    }

    public class ResumoCompraHolder extends RecyclerView.ViewHolder {

        public TextView tvIngressoValor;
        public TextView tvIngressoQuantidade;
        public TextView tvIngressoNome;
        public View rootView;

        public ResumoCompraHolder(View itemView) {
            super(itemView);

            tvIngressoValor = itemView.findViewById(R.id.tv_ingresso_valor);
            tvIngressoQuantidade = itemView.findViewById(R.id.tv_ingresso_quantidade);
            tvIngressoNome = itemView.findViewById(R.id.tv_ingresso_nome);

            rootView = itemView;
        }
    }
}


