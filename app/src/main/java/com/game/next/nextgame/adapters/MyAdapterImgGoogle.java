package com.game.next.nextgame.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.game.next.nextgame.R;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterImgGoogle extends RecyclerView.Adapter<MyAdapterImgGoogle.ViewHolder> {
    private List<String> urls, urlsLimpa;
    //private boolean temUmItemSelecionado = false;
    //private int contador = 0;
    //private int ultimoItemSelecionado = 0;
    //private RecyclerView recyclerView;

    private ArrayList<Integer> listaDePosicoes = new ArrayList<>();


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;

        public ImageView imgMeuJogo;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            imgMeuJogo = (ImageView) v.findViewById(R.id.img_capa_google);
        }
    }

    public MyAdapterImgGoogle(List<String> myDataset, RecyclerView recyclerView) {
        urls = myDataset;
        //this.recyclerView = recyclerView;
    }

    public MyAdapterImgGoogle(List<String> myDataset) {
        urls = myDataset;
        //this.recyclerView = recyclerView;
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

                        holder.itemView.setBackgroundColor(Color.GREEN);


                        /*
                        if(temUmItemSelecionado == true){
                            recyclerView.findViewHolderForAdapterPosition(ultimoItemSelecionado).itemView.setBackgroundColor(Color.TRANSPARENT);
                            holder.itemView.setBackgroundColor(Color.GREEN);
                            notifyDataSetChanged();
                        }else{
                            holder.itemView.setBackgroundColor(Color.GREEN);
                            temUmItemSelecionado = true;
                            ultimoItemSelecionado = position;
                            notifyDataSetChanged();
                        }
                        */



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
