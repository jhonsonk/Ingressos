package com.digitalingressos.ingressos.ui.adpater.viewholder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.digitalingressos.ingressos.R;

public class CardHolder extends RecyclerView.ViewHolder {

    public NetworkImageView productImage;
    public TextView productTitle;
    public LinearLayout card;

    public CardHolder(View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.evento_image);
        productTitle = itemView.findViewById(R.id.nome_evento);
        card = itemView.findViewById(R.id.lay_card_evento);
    }
}
