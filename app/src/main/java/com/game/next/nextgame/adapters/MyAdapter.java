package com.game.next.nextgame.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.next.nextgame.ActivityQuemTemOJogo;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Jogo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Jogo> jogos;
    private String corTexto;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;
        public ImageView imgCapa;
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            imgCapa = (ImageView) v.findViewById(R.id.imgCapa);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.row_layout_id);

            if(corTexto != null && !corTexto.equals("")){
                txtHeader.setTextColor(Color.parseColor(corTexto));
                txtFooter.setTextColor(Color.parseColor(corTexto));
            }


        }
    }

    public MyAdapter(List<Jogo> myDataset) {
        jogos = myDataset;
    }

    public MyAdapter(List<Jogo> myDataset, String corTexto) {
        jogos = myDataset;
        this.corTexto = corTexto;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        if(corTexto != null && !corTexto.equals("")){
            v = inflater.inflate(R.layout.row_layout_ps4, parent, false);
        }else{
            v = inflater.inflate(R.layout.row_layout, parent, false);
        }


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txtHeader.setText(jogos.get(position).getNome()); //Está GONE na view
        holder.txtFooter.setText(jogos.get(position).getDataLancamento()); //Está GONE na view
        Picasso.get().load(jogos.get(position).getUrlImgJogo()).into(holder.imgCapa, new Callback() {
            @Override
            public void onSuccess() {
                holder.imgCapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent quemTemOJogo = new Intent(holder.layout.getContext(), ActivityQuemTemOJogo.class);
                        quemTemOJogo.putExtra("JOGO",jogos.get(position));
                        holder.layout.getContext().startActivity(quemTemOJogo);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                removeAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jogos.size();
    }

    public void removeAt(int position) {
        jogos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, jogos.size());
    }
}