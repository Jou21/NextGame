package com.game.next.nextgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.game.next.nextgame.entidades.Jogo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VerificaSeAchouOJogoCorretoActivity extends AppCompatActivity {

    private ImageView imgVerificaSeAchouOJogoCorreto;

    private TextView txtNomeDoJogo, txtAchouOJogoCorreto;

    private Button btnSim, btnNao;

    private String codigoDeBarras = "";

    private ProgressBar mProgressBar;

    private ArrayList<Jogo> jogosPS4 = new ArrayList<>();
    private ArrayList<Jogo> jogosXbox = new ArrayList<>();

    private ArrayList<Jogo> listTodosJogos = new ArrayList<>();
    private List<String> listNomeTodosJogos = new ArrayList<>();

    private Jogo jogoIdentificado = new Jogo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifica_se_achou_o_jogo_correto);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarVerificaSeAchouOJogoCorreto);

        imgVerificaSeAchouOJogoCorreto = (ImageView) findViewById(R.id.img_verifica_se_achou_o_jogo_correto);
        txtNomeDoJogo = (TextView) findViewById(R.id.txt_nome_do_jogo_verifica_se_achou_o_jogo_correto);
        btnSim = (Button) findViewById(R.id.btn_sim_verifica_se_achou_o_jogo_correto);
        btnNao = (Button) findViewById(R.id.btn_nao_verifica_se_achou_o_jogo_correto);
        txtAchouOJogoCorreto = (TextView) findViewById(R.id.txt_seria_esse_o_jogo_verifica_se_achou_o_jogo_correto);

        imgVerificaSeAchouOJogoCorreto.setVisibility(View.GONE);
        txtNomeDoJogo.setVisibility(View.GONE);
        btnSim.setVisibility(View.GONE);
        btnNao.setVisibility(View.GONE);

        exibirProgress(true);

        Bundle b = getIntent().getExtras();

        if(b != null) {
            codigoDeBarras = b.getString("CODBAR");
            final String finalCodigoDeBarras = codigoDeBarras;

            final boolean[] jogoXboxEncontrado = {false};
            final boolean[] jogoPS4Encontrado = {false};
            final boolean[] pesquisouJogosXbox = {false};
            final boolean[] pesquisouJogosPS4 = {false};

            exibirProgress(true);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRefPS4 = database.getReference("PS4");

            myRefPS4.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Jogo jogo = snapshot.getValue(Jogo.class);
                        jogosPS4.add(jogo);
                        listTodosJogos.add(jogo);
                        listNomeTodosJogos.add(jogo.getNome());
                    }

                    for(Jogo j : jogosPS4){

                        if (j == jogosPS4.get(jogosPS4.size()-1) && jogoPS4Encontrado[0] == false){
                            pesquisouJogosPS4[0] = true;
                        }

                        if (j.getCodigoDeBarra().equals(finalCodigoDeBarras)) {
                            txtNomeDoJogo.setText(j.getNome());
                            Picasso.get().load(j.getUrlImgJogo()).into(imgVerificaSeAchouOJogoCorreto, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                            jogoIdentificado = j;

                            jogoPS4Encontrado[0] = true;

                            txtAchouOJogoCorreto.setText("Seria esse o seu jogo?");
                            imgVerificaSeAchouOJogoCorreto.setVisibility(View.VISIBLE);
                            txtNomeDoJogo.setVisibility(View.VISIBLE);
                            btnSim.setVisibility(View.VISIBLE);
                            btnNao.setVisibility(View.VISIBLE);

                            exibirProgress(false);


                        }else if (j == jogosPS4.get(jogosPS4.size()-1) && jogoXboxEncontrado[0] == false && jogoPS4Encontrado[0] == false && pesquisouJogosXbox[0] == true && pesquisouJogosPS4[0] == true) {

                            Intent telaIdentificaJogo = new Intent(VerificaSeAchouOJogoCorretoActivity.this, ActivityIdentificaJogo.class);
                            telaIdentificaJogo.putExtra("CODBAR", codigoDeBarras);
                            telaIdentificaJogo.putExtra("CORRETOCODBAR", "NAO");
                            startActivity(telaIdentificaJogo);
                            finish();

                            exibirProgress(false);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERRO", String.valueOf(databaseError.getCode()));
                }


            });

            DatabaseReference myRefXbox = database.getReference("Xbox");

            myRefXbox.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Jogo jogo = snapshot.getValue(Jogo.class);
                        jogosXbox.add(jogo);
                        listTodosJogos.add(jogo);
                        listNomeTodosJogos.add(jogo.getNome());
                    }

                    for(Jogo j : jogosXbox){

                        if (j == jogosXbox.get(jogosXbox.size()-1) && jogoXboxEncontrado[0] == false){
                            pesquisouJogosXbox[0] = true;
                        }

                        if (j.getCodigoDeBarra().equals(finalCodigoDeBarras)) {
                            txtNomeDoJogo.setText(j.getNome());
                            Picasso.get().load(j.getUrlImgJogo()).into(imgVerificaSeAchouOJogoCorreto, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                            jogoIdentificado = j;

                            jogoXboxEncontrado[0] = true;

                            txtAchouOJogoCorreto.setText("Seria esse o seu jogo?");
                            imgVerificaSeAchouOJogoCorreto.setVisibility(View.VISIBLE);
                            txtNomeDoJogo.setVisibility(View.VISIBLE);
                            btnSim.setVisibility(View.VISIBLE);
                            btnNao.setVisibility(View.VISIBLE);

                            exibirProgress(false);

                        }else if (j == jogosXbox.get(jogosXbox.size()-1) && jogoXboxEncontrado[0] == false && jogoPS4Encontrado[0] == false && pesquisouJogosXbox[0] == true && pesquisouJogosPS4[0] == true) {

                            Intent telaIdentificaJogo = new Intent(VerificaSeAchouOJogoCorretoActivity.this, ActivityIdentificaJogo.class);
                            telaIdentificaJogo.putExtra("CODBAR", codigoDeBarras);
                            telaIdentificaJogo.putExtra("CORRETOCODBAR", "NAO");
                            startActivity(telaIdentificaJogo);
                            finish();

                            exibirProgress(false);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERRO", String.valueOf(databaseError.getCode()));
                }


            });

            btnSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent telaIdentificaJogo = new Intent(VerificaSeAchouOJogoCorretoActivity.this, ActivityIdentificaJogo.class);
                    telaIdentificaJogo.putExtra("CODBAR", codigoDeBarras);
                    telaIdentificaJogo.putExtra("CORRETOCODBAR", "SIM");
                    startActivity(telaIdentificaJogo);
                    finish();
                }
            });


            btnNao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent telaIdentificaJogo = new Intent(VerificaSeAchouOJogoCorretoActivity.this, ActivityIdentificaJogo.class);
                    telaIdentificaJogo.putExtra("CODBAR", codigoDeBarras);
                    telaIdentificaJogo.putExtra("CORRETOCODBAR", "NAO");
                    startActivity(telaIdentificaJogo);
                    finish();
                }
            });




        }


    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
