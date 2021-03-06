package com.game.next.nextgame.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.next.nextgame.InfoTransacaoActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.game.next.nextgame.entidades.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapterTransacoes extends RecyclerView.Adapter<MyAdapterTransacoes.ViewHolder> {

    private ArrayList<TransacaoUser> transacoesUsers;
    private DatabaseReference referenceUsers;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtFirstLine;
        public TextView txtSecondLine;
        public View layout;
        public CircleImageView imgUser;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtFirstLine = (TextView) v.findViewById(R.id.first_line_transacao);
            txtSecondLine = (TextView) v.findViewById(R.id.second_line_transacao);
            imgUser = (CircleImageView) v.findViewById(R.id.img_row_transacao);
        }
    }

    public MyAdapterTransacoes(ArrayList<TransacaoUser> myDataset, DatabaseReference referenceUsers) {
        transacoesUsers = myDataset;
        this.referenceUsers = referenceUsers;
    }

    @Override
    public MyAdapterTransacoes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.row_transacao, parent, false);

        MyAdapterTransacoes.ViewHolder vh = new MyAdapterTransacoes.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtFirstLine.setText(transacoesUsers.get(position).getJogo().getNome());

        if(transacoesUsers.get(position).getStatus().equals("ENTREGADO")) {
            holder.txtSecondLine.setText(transacoesUsers.get(position).getStatus());
            holder.txtSecondLine.setBackground(ContextCompat.getDrawable(holder.layout.getContext(), R.drawable.shape_btn_arredondado_amarelo));
        }else if(transacoesUsers.get(position).getStatus().equals("CONCLUIDO")){
            holder.txtSecondLine.setText(transacoesUsers.get(position).getStatus());
            holder.txtSecondLine.setBackground(ContextCompat.getDrawable(holder.layout.getContext(), R.drawable.shape_btn_arredondado_verde));
        } else {
            holder.txtSecondLine.setText(transacoesUsers.get(position).getStatus());
            holder.txtSecondLine.setBackground(ContextCompat.getDrawable(holder.layout.getContext(), R.drawable.shape_btn_arredondado_azul));
        }

        referenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    User user = postSnapshot.getValue(User.class);

                    if (user.getId().equals(transacoesUsers.get(position).getFornecedorId())) {

                        Picasso.get().load(user.getImageURL()).into(holder.imgUser, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInfoTransacao = new Intent(holder.layout.getContext(), InfoTransacaoActivity.class);
                intentInfoTransacao.putExtra("TRANSACAO", transacoesUsers.get(position));
                holder.layout.getContext().startActivity(intentInfoTransacao);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transacoesUsers.size();
    }
}
