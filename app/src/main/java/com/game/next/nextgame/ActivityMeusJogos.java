package com.game.next.nextgame;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.game.next.nextgame.adapters.MyAdapterMeusJogos;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.UserGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ActivityMeusJogos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button btn_meus_jogos_adicionar_jogo;

    private DatabaseReference reference;
    private FirebaseUser user;

    private ArrayList<UserGame> userGames = new ArrayList<>();

    private String contents;
    //private String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_jogos);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFA900")));
        bar.setTitle("Meus Jogos");

        getWindow().setStatusBarColor(Color.parseColor("#FFA900"));

        bar.setDisplayHomeAsUpEnabled(true);


        btn_meus_jogos_adicionar_jogo = (Button) findViewById(R.id.btn_meus_jogos_adicionar_jogo);
        btn_meus_jogos_adicionar_jogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ActivityMeusJogos.this);
                integrator.setPrompt("Mantenha um palmo de distancia do código de barras");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_meus_jogos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //List<String> input = new ArrayList<>();
        //for (int i = 0; i < 100; i++) {
        //    input.add("Test" + i);
        //}

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserGame").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserGame userGame = snapshot.getValue(UserGame.class);
                    userGames.add(userGame);
                }

                mAdapter = new MyAdapterMeusJogos(userGames);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        if(scanningResult != null){
            contents = intent.getStringExtra("SCAN_RESULT");
            //format = intent.getStringExtra("SCAN_RESULT_FORMAT");

            //Log.d("CODBAR",""+contents);

            Intent telaIdentificaJogo = new Intent(ActivityMeusJogos.this, ActivityIdentificaJogo.class);
            telaIdentificaJogo.putExtra("CODBAR",contents);
            startActivity(telaIdentificaJogo);

        }

        super.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
