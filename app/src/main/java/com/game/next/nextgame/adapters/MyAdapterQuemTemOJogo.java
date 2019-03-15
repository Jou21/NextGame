package com.game.next.nextgame.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    public MyAdapterQuemTemOJogo(ArrayList<UserGame> myDataset) {
        listaUserGames = myDataset;

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

        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Log.d("ENTROUX","ENTROUX");

                String nomeOtherUser = "";
                String imgOtherUserURL = "default";

                String precoAluguel = "";
                String precoVenda = "";

                final boolean[] entrou = {false};

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);

                    //Log.d("ENTROUY","ENTROUY");

                    //Log.d("ENTROUY","1 - " + user.getId() + " 2 - " + listaUserGames.get(position).getUserId());

                    if(user.getId().equals(listaUserGames.get(position).getUserId())){

                        //Log.d("ENTROUZ","ENTROUZ");

                        nomeOtherUser = user.getUsername();
                        imgOtherUserURL = user.getImageURL();

                        precoAluguel = listaUserGames.get(position).getPrecoAluga();
                        precoVenda = listaUserGames.get(position).getPrecoVenda();
                    }

                }

                holder.firstLine.setText(nomeOtherUser);
                holder.txtAluguel.setText("R$" + precoAluguel + ",00");
                holder.txtCompra.setText("R$" + precoVenda + ",00");

                if (!imgOtherUserURL.equals("default")){

                    Picasso.get().load(imgOtherUserURL).into(holder.imgUser, new Callback() {
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String latCurrentUser = "";
                String longCurrentUser = "";

                String latOtherUser = "";
                String longOtherUser = "";

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    LocationData locationUser = postSnapshot.getValue(LocationData.class);

                    if(locationUser.getUserId().equals(listaUserGames.get(position).getUserId())){
                        latOtherUser = locationUser.getEntregaLatitude();
                        longOtherUser = locationUser.getEntregaLongitude();
                    }

                    if(locationUser.getUserId().equals(user.getUid())){
                        latCurrentUser = locationUser.getLatitude();
                        longCurrentUser = locationUser.getLongitude();
                    }

                }

                if((latCurrentUser != "" && longCurrentUser != "") && (latOtherUser != "" && longOtherUser != "")){
                    LatLng posicaoInicial = new LatLng(Double.parseDouble(latCurrentUser),Double.parseDouble(longCurrentUser));
                    LatLng posicaoFinal = new LatLng(Double.parseDouble(latOtherUser),Double.parseDouble(longOtherUser));

                    double distance = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaoFinal);

                    holder.secondLine.setText("Está a " + formatNumber(distance) + " de você");
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

    public void removeAt(int position) {
        listaUserGames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaUserGames.size());
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
