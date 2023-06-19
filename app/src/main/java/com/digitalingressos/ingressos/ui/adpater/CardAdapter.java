package com.digitalingressos.ingressos.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

import com.digitalingressos.ingressos.R;

import com.digitalingressos.ingressos.network.ImageRequester;
import com.digitalingressos.ingressos.ui.fragment.SessaoFragment;
import com.digitalingressos.ingressos.ui.adpater.viewholder.CardHolder;
import com.digitalingressos.ingressos.util.NavigationHost;

import java.util.List;


//public class CardAdapter extends RecyclerView.Adapter<CardHolder> {
//
//    private ItemViewModel viewModel;
//    private final List<EventoEntry> mUsers;
//    private ImageRequester imageRequester;
//
//    //POSSO RECEBER O CONTEXTO
//    public CardAdapter(List<EventoEntry> users, ItemViewModel _viewModel) {
//        mUsers = users;
//        imageRequester = ImageRequester.getInstance();
//        viewModel = _viewModel;
//    }
//
//    @Override
//    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new CardHolder(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_evento, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(CardHolder holder, int position) {
//        //holder.title.setText(String.format(Locale.getDefault(), "%s, %d - %s",mUsers.get(position).getName(),mUsers.get(position).getAge(), mUsers.get(position).getCity()));
//
//        imageRequester.setImageFromUrl(holder.productImage, mUsers.get(position).imagem_banner_interno_link);
//        holder.productTitle.setText(mUsers.get(position).getNome());
//        holder.card.setOnClickListener(view -> updateItem(view, position)); //SET ACAO
//        //holder.moreButton.setOnClickListener(view -> updateItem(position)); SET ACAO
//        //holder.deleteButton.setOnClickListener(view -> removerItem(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mUsers != null ? mUsers.size() : 0;
//    }
//
//    private void insertItem(EventoEntry user) {
//        mUsers.add(user);
//        notifyItemInserted(getItemCount());
//    }
//
//    private void updateItem(View view, int position) {
//        mUsers.get(position).incrementAge();//FUNCAO
//        viewModel.selectItem(mUsers.get(position));
//
//        //((NavigationHost) view.getContext()).navigateTo(new SessaoFragment(viewModel), false); // Navigate to the next Fragment
//        notifyItemChanged(position);
//    }
//
//    private void removerItem(int position) {
//        mUsers.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, mUsers.size());
//    }
//
//    public void updateList(EventoEntry user) {
//        insertItem(user);
//    }
//}


