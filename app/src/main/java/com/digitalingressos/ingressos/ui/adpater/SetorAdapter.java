package com.digitalingressos.ingressos.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.database.setor.Setor;
import com.digitalingressos.ingressos.viewModel.CarrinhoViewModel;

import java.util.List;

public class SetorAdapter extends RecyclerView.Adapter<SetorAdapter.SetorHolder> {
    private CarrinhoViewModel mCarrinho;
    private final List<Setor> mSetor;
    private LinearLayout mLayoutCart;

    public SetorAdapter(List<Setor> setor, CarrinhoViewModel carrinho, LinearLayout layoutCart) {
        mSetor = setor;
        mLayoutCart = layoutCart;
        mCarrinho = carrinho;
    }

    @Override
    public SetorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SetorHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_setor, parent, false));
    }

    @Override
    public void onBindViewHolder(SetorHolder holder, int position) {
        holder.mSetorNome.setText(mSetor.get(position).getNome());
        RecyclerView recyclerView = holder.mRootView.findViewById(R.id.recycler_view_ingressos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(holder.mRootView.getContext(), 2, RecyclerView.VERTICAL, false));

        Setor _setor = mSetor.get(position);
        mSetor.get(position).getIngressos().forEach(a-> a.setSetor(_setor));

        IngressoAdapter adapter = new IngressoAdapter(mSetor.get(position).getIngressos(), mCarrinho, mLayoutCart);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mSetor != null ? mSetor.size() : 0;
    }


    /**
     * Holder Setor
     * */
    public class SetorHolder extends RecyclerView.ViewHolder {
        public TextView mSetorNome;
        public View mRootView;
        public SetorHolder(View itemView) {
            super(itemView);
            mSetorNome = itemView.findViewById(R.id.nome_setor);
            mRootView = itemView;
        }
    }
}


