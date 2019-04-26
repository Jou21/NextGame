package com.game.next.nextgame.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.game.next.nextgame.ActivityQuemTemOJogo;
import com.game.next.nextgame.JogoDoUsuarioActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.LocationData;
import com.game.next.nextgame.entidades.User;
import com.game.next.nextgame.entidades.UserGame;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapterQuemTemOJogo extends RecyclerView.Adapter<MyAdapterQuemTemOJogo.ViewHolder> {

    private ArrayList<UserGame> listaUserGames;
    private DatabaseReference reference;
    private FirebaseUser user;
    private Jogo jogo;

    private String userId = "";
    private String nomeOtherUser = "";
    private String imgOtherUserURL = "default";

    private String precoAluguel = "";
    private String precoVenda = "";

    private String latCurrentUser = "";
    private String longCurrentUser = "";

    private String latOtherUser = "";
    private String longOtherUser = "";


    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imgUser;
        public TextView firstLine, secondLine, txtCompra, txtAluguel;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            imgUser = (CircleImageView) v.findViewById(R.id.icon_quem_tem_o_jogo);
            firstLine = (TextView) v.findViewById(R.id.firstLine_quem_tem_o_jogo);
            secondLine = (TextView) v.findViewById(R.id.secondLine_quem_tem_o_jogo);
            txtCompra = (TextView) v.findViewById(R.id.txt_preco_quem_tem_o_jogo);
            txtAluguel = (TextView) v.findViewById(R.id.txt_aluguel_quem_tem_o_jogo);
        }
    }

    public MyAdapterQuemTemOJogo(ArrayList<UserGame> myDataset, Jogo jogo) {
        listaUserGames = myDataset;
        this.jogo = jogo;
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
    }



    @Override
    public MyAdapterQuemTemOJogo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_quem_tem_o_jogo, parent, false);

        MyAdapterQuemTemOJogo.ViewHolder vh = new MyAdapterQuemTemOJogo.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final User user = postSnapshot.getValue(User.class);

                    if(user.getId().equals(listaUserGames.get(position).getUserId())){

                        holder.firstLine.setText(user.getUsername());
                        if(listaUserGames.get(position).getAluga().equals("N")){
                            holder.txtAluguel.setTextSize(14);
                            holder.txtAluguel.setText("Indisponível");
                        }else {
                            holder.txtAluguel.setTextSize(17);
                            holder.txtAluguel.setText("R$" + listaUserGames.get(position).getPrecoAluga() + ",00");
                        }
                        if(listaUserGames.get(position).getVende().equals("N")) {
                            holder.txtCompra.setTextSize(14);
                            holder.txtCompra.setText("Indisponível");

                        }else {
                            holder.txtCompra.setTextSize(17);
                            holder.txtCompra.setText("R$" + listaUserGames.get(position).getPrecoVenda() + ",00");
                        }


                        if (!user.getImageURL().equals("default")){

                            Picasso.get().load(user.getImageURL()).into(holder.imgUser, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                        } else {
                            holder.imgUser.setImageResource(R.mipmap.ic_launcher);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            //Log.d("USERIDS",listaUserGames.get(position).getPrecoAluga());

                            Intent jogoDoUsuarioIntent = new Intent(holder.layout.getContext(), JogoDoUsuarioActivity.class);
                            jogoDoUsuarioIntent.putExtra("USERID",listaUserGames.get(position).getUserId());
                            jogoDoUsuarioIntent.putExtra("JOGOUSER",jogo);
                            jogoDoUsuarioIntent.putExtra("IMAGEMUSER",user.getImageURL());
                            jogoDoUsuarioIntent.putExtra("NOMEUSER",user.getUsername());
                            jogoDoUsuarioIntent.putExtra("ALUGA",listaUserGames.get(position).getAluga());
                            jogoDoUsuarioIntent.putExtra("VENDE",listaUserGames.get(position).getVende());
                            jogoDoUsuarioIntent.putExtra("PRECOALUGUEL",listaUserGames.get(position).getPrecoAluga());
                            jogoDoUsuarioIntent.putExtra("PRECOVENDA",listaUserGames.get(position).getPrecoVenda());
                            jogoDoUsuarioIntent.putExtra("TIME",listaUserGames.get(position).getTime());
                            holder.layout.getContext().startActivity(jogoDoUsuarioIntent);
                            ((ActivityQuemTemOJogo)holder.layout.getContext()).finish();
                            }
                        });
                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    LocationData locationUser = postSnapshot.getValue(LocationData.class);

                    if((locationUser.getUserId() != null && listaUserGames.get(position).getUserId() != null) && user != null){
                        if(locationUser.getUserId().equals(listaUserGames.get(position).getUserId())){
                            //latOtherUser = locationUser.getEntregaLatitude();
                            //longOtherUser = locationUser.getEntregaLongitude();
                            latOtherUser = locationUser.getLatitude();
                            longOtherUser = locationUser.getLongitude();
                        }

                        if(locationUser.getUserId().equals(user.getUid())){
                            latCurrentUser = locationUser.getLatitude();
                            longCurrentUser = locationUser.getLongitude();
                        }
                    }

                }

                if((latCurrentUser != null && longCurrentUser != null) && (latOtherUser != null && longOtherUser != null)){
                    if((!latCurrentUser.equals("") && !longCurrentUser.equals("")) && (!latOtherUser.equals("") && !longOtherUser.equals(""))){
                        LatLng posicaoInicial = new LatLng(Double.parseDouble(latCurrentUser),Double.parseDouble(longCurrentUser));
                        LatLng posicaoFinal = new LatLng(Double.parseDouble(latOtherUser),Double.parseDouble(longOtherUser));

                        if( posicaoInicial != null && posicaoFinal != null) {
                            double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaoFinal);

                            holder.secondLine.setText("Está a " + formatNumber(distance) + " de você");
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return listaUserGames.size();
    }


    private String formatNumber(double distance) {
        String unit = " m";
        if (distance > 1000) {
            distance /= 1000;
            unit = " km";
        }

        return String.format("%4.1f%s", distance, unit);
    }
}
