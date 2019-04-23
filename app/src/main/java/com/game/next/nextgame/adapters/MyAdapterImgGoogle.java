package com.game.next.nextgame.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.game.next.nextgame.ActivityIdentificaJogo;
import com.game.next.nextgame.PerguntaXboxOuPS4Activity;
import com.game.next.nextgame.R;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterImgGoogle extends RecyclerView.Adapter<MyAdapterImgGoogle.ViewHolder> {
    private List<String> urls;
    private ActivityIdentificaJogo activityIdentificaJogo;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;

        public ImageView imgMeuJogo;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            imgMeuJogo = (ImageView) v.findViewById(R.id.img_capa_google);
        }
    }

    public MyAdapterImgGoogle(List<String> myDataset, ActivityIdentificaJogo context) {
        urls = myDataset;
        activityIdentificaJogo = context;
    }

    public MyAdapterImgGoogle(List<String> myDataset) {
        urls = myDataset;
    }

    @Override
    public MyAdapterImgGoogle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.row_grid_layout, parent, false);

        MyAdapterImgGoogle.ViewHolder vh = new MyAdapterImgGoogle.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyAdapterImgGoogle.ViewHolder holder, final int position) {

        Picasso.get().load(urls.get(position)).into(holder.imgMeuJogo, new Callback() {
            @Override
            public void onSuccess() {
                holder.imgMeuJogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityIdentificaJogo.capturaCapa(position);
                        holder.itemView.setBackgroundColor(Color.GREEN);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                removeAt(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();

    }

    public void removeAt(int position) {
        urls.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, urls.size());
    }

}
