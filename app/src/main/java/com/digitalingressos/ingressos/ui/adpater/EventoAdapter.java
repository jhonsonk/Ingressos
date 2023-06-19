package com.digitalingressos.ingressos.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.digitalingressos.ingressos.R;
import com.digitalingressos.ingressos.data.database.evento.Evento;

import com.digitalingressos.ingressos.network.ImageRequester;
import com.digitalingressos.ingressos.ui.fragment.SessaoFragment;
import com.digitalingressos.ingressos.util.NavigationHost;

import java.util.ArrayList;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoHolder> {

    private List<Evento> mEventos;
    private ImageRequester imageRequester;

    public EventoAdapter(List<Evento> eventos) {
        mEventos = eventos;
        imageRequester = ImageRequester.getInstance();

    }

    @Override
    public EventoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_evento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventoHolder holder, int position) {
        imageRequester.setImageFromUrl(holder.mEventoImage, mEventos.get(position).getImagemBannerInternoLink());
        holder.mEventoNome.setText(mEventos.get(position).getNome());
        holder.mCard.setOnClickListener(view -> onSelectItem(view, position));
    }

    @Override
    public int getItemCount() {
        return mEventos != null ? mEventos.size() : 0;
    }

    private void onSelectItem(View view, int position) {
        ((NavigationHost) view.getContext()).navigateTo(new SessaoFragment(mEventos.get(position)), false); // Navigate to the next Fragment

    }

    public void setFilteredList(List<Evento> filteredList) {
        this.mEventos = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Holder View do Evento
     */
    public class EventoHolder extends RecyclerView.ViewHolder {
        public NetworkImageView mEventoImage;
        public TextView mEventoNome;
        public LinearLayout mCard;

        public EventoHolder(View itemView) {
            super(itemView);
            mEventoImage = itemView.findViewById(R.id.evento_image);
            mEventoNome = itemView.findViewById(R.id.nome_evento);
            mCard = itemView.findViewById(R.id.lay_card_evento);
        }
    }
}






