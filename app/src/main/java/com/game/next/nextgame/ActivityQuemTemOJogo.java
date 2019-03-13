package com.game.next.nextgame;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.UserGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityQuemTemOJogo extends AppCompatActivity {

    private Jogo model;
    private DatabaseReference reference;
    //private FirebaseUser user;

    private TextView txtQuemTemOJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quem_tem_o_jogo);

        txtQuemTemOJogo = (TextView) findViewById(R.id.txt_quem_tem_o_jogo);

        //user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

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
                            txtQuemTemOJogo.setText(userGame.getJogoId());
                            Toast.makeText(ActivityQuemTemOJogo.this, "Tem pelo menos 1 pessoa com esse jogo disponível!!!", Toast.LENGTH_LONG).show();
                        }



                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
