package com.digitalingressos.ingressos.recyclercomlivedata;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<User> userArrayList;

    public RecyclerViewAdapter() {
        this.userArrayList = new ArrayList<User> ();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        //return new RecyclerViewViewHolder(rootView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        User user = userArrayList.get(position);
        RecyclerViewViewHolder viewHolder= (RecyclerViewViewHolder) holder;

        viewHolder.txtView_title.setText(user.getTitle());
        viewHolder.txtView_description.setText(user.getDescription());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public void updateUserList(final List<User> userArrayList) {
        this.userArrayList.clear();
        this.userArrayList = userArrayList;
        notifyDataSetChanged();
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_icon;
        TextView txtView_title;
        TextView txtView_description;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            //imgView_icon = itemView.findViewById(R.id.imgView_icon);
            //txtView_title = itemView.findViewById(R.id.txtView_title);
            //txtView_description = itemView.findViewById(R.id.txtView_description);


        }
    }
}