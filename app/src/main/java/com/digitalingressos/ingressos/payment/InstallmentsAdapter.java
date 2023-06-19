package com.digitalingressos.ingressos.payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.util.Utils;

import java.util.List;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInstallment;

public class InstallmentsAdapter extends RecyclerView.Adapter<InstallmentsAdapter.ViewHolder> {

    private final List<PlugPagInstallment> installments;
    private final OnItemClickListener listener;

    public InstallmentsAdapter(List<PlugPagInstallment> installments, OnItemClickListener listener) {
        this.installments = installments;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_installment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(installments.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return installments.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int installNumber);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemInstallment;
        public TextView itemInstallmentValor;
        public TextView itemInstallmentTotal;


        public ViewHolder(View itemView) {
            super(itemView);
            itemInstallment = itemView.findViewById(R.id.tvInstallment);
            itemInstallmentValor = itemView.findViewById(R.id.tvInstallmentValor);
            itemInstallmentTotal = itemView.findViewById(R.id.tvInstallmentTotal);
        }

        public void bind(PlugPagInstallment installment, final int item, final OnItemClickListener listener) {
            if (installment != null) {
                itemInstallment.setText(installment.getQuantity() + " X");
                itemInstallmentValor.setText(Utils.getFormattedValue((double) installment.getAmount()));
                itemInstallmentTotal.setText("= "+Utils.getFormattedValue((double) installment.getTotal()));
                itemView.setOnClickListener(v -> listener.onItemClick(installment.getQuantity()));
            }
        }
    }
}
