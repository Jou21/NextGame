package com.game.next.nextgame.adapters;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.UserGame;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MyAdapterMeusJogos extends RecyclerView.Adapter<MyAdapterMeusJogos.ViewHolder> {
    private List<UserGame> userGames;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ImageView imgMeuJogo;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine_meus_jogos);
            txtFooter = (TextView) v.findViewById(R.id.secondLine_meus_jogos);
            imgMeuJogo = (ImageView) v.findViewById(R.id.icon_meus_jogos);
        }
    }

    public MyAdapterMeusJogos(List<UserGame> myDataset) {
        userGames = myDataset;
    }

    @Override
    public MyAdapterMeusJogos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.row_meus_jogos, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String name = userGames.get(position).getNomeJogo();
        holder.txtHeader.setText(name);
        holder.txtFooter.setText("Footer: " + name);
        Picasso.get().load(userGames.get(position).getImgJogo()).into(holder.imgMeuJogo, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userGames.size();
    }

}