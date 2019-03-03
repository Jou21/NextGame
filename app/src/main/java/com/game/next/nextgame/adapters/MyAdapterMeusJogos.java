package com.game.next.nextgame.adapters;

import java.util.List;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.ActivityMeusJogos;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.UserGame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MyAdapterMeusJogos extends RecyclerView.Adapter<MyAdapterMeusJogos.ViewHolder> {
    private List<UserGame> userGames;
    private DatabaseReference reference;
    private RecyclerView.Adapter adapter;
    private ActivityMeusJogos activityMeusJogos;

    private int entrou = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtPrecoRowJogo;
        public TextView txtAluguelRowJogo;
        public View layout;
        public Button btnDelete;
        public ImageView imgMeuJogo;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine_meus_jogos);
            txtPrecoRowJogo = (TextView) v.findViewById(R.id.txt_preco_row_jogo);
            txtAluguelRowJogo = (TextView) v.findViewById(R.id.txt_aluguel_row_jogo);
            imgMeuJogo = (ImageView) v.findViewById(R.id.icon_meus_jogos);
            btnDelete = (Button) v.findViewById(R.id.btn_delete);
        }
    }

    public MyAdapterMeusJogos(List<UserGame> myDataset, DatabaseReference reference, RecyclerView.Adapter mAdapter) {
        userGames = myDataset;
        this.reference = reference;
        this.adapter = mAdapter;
    }

    public MyAdapterMeusJogos(List<UserGame> myDataset, DatabaseReference reference, ActivityMeusJogos activityMeusJogos) {
        userGames = myDataset;
        this.reference = reference;
        this.activityMeusJogos = activityMeusJogos;
    }

    @Override
    public MyAdapterMeusJogos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.row_meus_jogos, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String name = userGames.get(position).getNomeJogo();
        holder.txtHeader.setText(name);
        if(userGames.get(position).getPrecoVenda().equals("N")){
            holder.txtPrecoRowJogo.setText("Indisponível");
        }else{
            holder.txtPrecoRowJogo.setText("R$" + userGames.get(position).getPrecoVenda() + ",00");
        }

        if(userGames.get(position).getPrecoAluga().equals("N")){
            holder.txtAluguelRowJogo.setText("Indisponível");
        }else{
            holder.txtAluguelRowJogo.setText("R$" + userGames.get(position).getPrecoAluga() + ",00");
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //final View view = v;
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            UserGame userGame = snapshot.getValue(UserGame.class);

                            //notifyItemRemoved(position);
                            //activityMeusJogos.notificaLista();
                            if(userGames.get(position).getTime() == userGame.getTime() && position != entrou){

                                //userGames.remove(position);
                                //notifyItemRemoved(position);
                                snapshot.getRef().removeValue();
                                remove(position);

                                //activityMeusJogos.notificaLista();
                                Toast.makeText(holder.layout.getContext(),"Você excluiu um registro!",Toast.LENGTH_SHORT).show();
                                entrou = position;

                            }

                            //notifyItemRemoved(position);
                            //activityMeusJogos.notificaLista();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(holder.layout.getContext(),"Aconteceu um erro ao excluir!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

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

    public void remove(int position) {
        userGames.remove(position);
        notifyItemRemoved(position);
        activityMeusJogos.notificaLista();
    }
}