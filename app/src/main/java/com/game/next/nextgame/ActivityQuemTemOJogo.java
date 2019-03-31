package com.game.next.nextgame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.adapters.MyAdapterQuemTemOJogo;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.UserGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuemTemOJogo extends AppCompatActivity {

    private Jogo model;
    private DatabaseReference reference;
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    private TextView txtQuemTemOJogo;

    private ArrayList<UserGame> listaUserGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quem_tem_o_jogo);

        txtQuemTemOJogo = (TextView) findViewById(R.id.txt_quem_tem_o_jogo);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_quem_tem_o_jogo);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        if (getIntent().hasExtra("JOGO")) {
            model = (Jogo) getIntent().getSerializableExtra("JOGO");

            reference.child("UserGame").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //String jogoId = reference.child("UserGame").child(userId).child(key).child("jogoId");

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()){
                            UserGame userGame = postSnapshot2.getValue(UserGame.class);
                            //userGames.add(userGame);

                            if(userGame.getJogoId().equals(model.getCodigoDeBarra())){
                                //txtQuemTemOJogo.setText(userGame.getJogoId());
                                //Toast.makeText(ActivityQuemTemOJogo.this, "Tem pelo menos 1 pessoa com esse jogo disponível!!!", Toast.LENGTH_LONG).show();
                                if(!userGame.getUserId().equals(user.getUid())){
                                    listaUserGames.add(userGame);
                                }

                            }

                        }

                    }
                    mAdapter = new MyAdapterQuemTemOJogo(listaUserGames, model);
                    recyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(ActivityQuemTemOJogo.this,"Activity cannot find  extras " + "JOGO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "JOGO");
        }

    }

    @Override
    public void onBackPressed() {
        //Intent mainActivity = new Intent(ActivityQuemTemOJogo.this, MainActivity.class);
        //startActivity(mainActivity);
        finish();
        super.onBackPressed();
    }
}
