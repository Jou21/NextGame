package com.game.next.nextgame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.game.next.nextgame.adapters.MyAdapterMeusJogos;

import java.util.ArrayList;
import java.util.List;

public class ActivityMeusJogos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Button btn_meus_jogos_adicionar_jogo;

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

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_meus_jogos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }
        mAdapter = new MyAdapterMeusJogos(input);
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
