package com.digitalingressos.ingressos.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;


import com.digitalingressos.ingressos.data.database.pagamentoItem.PagamentoItem;


import java.util.List;

public class ResumoCompradorAdapter extends RecyclerView.Adapter<ResumoCompradorAdapter.ResumoCompradorHolder> {

    private final List<PagamentoItem> mUsers;

    public ResumoCompradorAdapter(List<PagamentoItem> users) {
        mUsers = users;
    }

    @Override
    public ResumoCompradorHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ResumoCompradorHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_resumo_comprador, parent, false));
    }

    @Override
    public void onBindViewHolder(ResumoCompradorHolder holder, int position) {
        holder.tvIngressoPosse.setText(mUsers.get(position).getStringResumoPosse());
    }

    @Override
    public int getItemCount() {

        return mUsers != null ? mUsers.size() : 0;
    }

    public class ResumoCompradorHolder extends RecyclerView.ViewHolder {
        public TextView tvIngressoPosse;

        public ResumoCompradorHolder(View itemView) {
            super(itemView);
            tvIngressoPosse = itemView.findViewById(R.id.tv_ingresso_posse);
        }
    }
}

