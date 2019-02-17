package com.game.next.nextgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.next.nextgame.adapters.MyAdapter;
import com.game.next.nextgame.adapters.MyAdapterTitulos;
import com.game.next.nextgame.entidades.Jogo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityIdentificaJogo extends AppCompatActivity {

    private TextView txtCodIdentificado, txtCodigoDeBarras;
    private ImageView imgCodIdentificado;
    private Button btnAdicionarJogoManualmente, btnAdicionarJogoSucesso;
    private ArrayList<Jogo> jogosPS4 = new ArrayList<>();
    private ArrayList<Jogo> jogosXbox = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogo);

        txtCodIdentificado = (TextView) findViewById(R.id.txt_cod_identificado);
        imgCodIdentificado = (ImageView) findViewById(R.id.img_cod_identificado);
        btnAdicionarJogoManualmente = (Button) findViewById(R.id.btn_adicionar_jogo_manualmente);
        txtCodigoDeBarras = (TextView) findViewById(R.id.txt_codigo_de_barras_identifica_jogo);
        btnAdicionarJogoSucesso = (Button) findViewById(R.id.btn_adicionar_jogo_sucesso);

        Bundle b = getIntent().getExtras();
        String codigoDeBarras = "";
        if(b != null) {
            codigoDeBarras = b.getString("CODBAR");
            final String finalCodigoDeBarras = codigoDeBarras;

            final boolean[] jogoXboxEncontrado = {false};
            final boolean[] jogoPS4Encontrado = {false};

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRefPS4 = database.getReference("PS4");

            myRefPS4.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Jogo jogo = snapshot.getValue(Jogo.class);
                        jogosPS4.add(jogo);

                    }

                    for(Jogo j : jogosPS4){

                        if (j.getCodigoDeBarra().equals(finalCodigoDeBarras)) {
                            txtCodIdentificado.setText(j.getNome());
                            Picasso.get().load(j.getUrlImgJogo()).into(imgCodIdentificado, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            jogoPS4Encontrado[0] = true;
                            btnAdicionarJogoManualmente.setVisibility(View.GONE);
                            btnAdicionarJogoSucesso.setVisibility(View.VISIBLE);

                        }else if (j == jogosPS4.get(jogosPS4.size()-1) && jogoXboxEncontrado[0] == false && jogoPS4Encontrado[0] == false) {
                            txtCodIdentificado.setText("Nenhum jogo encontrado");
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            btnAdicionarJogoManualmente.setVisibility(View.VISIBLE);
                            btnAdicionarJogoSucesso.setVisibility(View.GONE);
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

                    }

                    for(Jogo j : jogosXbox){

                        if (j.getCodigoDeBarra().equals(finalCodigoDeBarras)) {
                            txtCodIdentificado.setText(j.getNome());
                            Picasso.get().load(j.getUrlImgJogo()).into(imgCodIdentificado, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            jogoXboxEncontrado[0] = true;
                            btnAdicionarJogoManualmente.setVisibility(View.GONE);
                            btnAdicionarJogoSucesso.setVisibility(View.VISIBLE);

                        }else if (j == jogosXbox.get(jogosXbox.size()-1) && jogoXboxEncontrado[0] == false && jogoPS4Encontrado[0] == false) {
                            txtCodIdentificado.setText("Nenhum jogo encontrado");
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            btnAdicionarJogoManualmente.setVisibility(View.VISIBLE);
                            btnAdicionarJogoSucesso.setVisibility(View.GONE);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERRO", String.valueOf(databaseError.getCode()));
                }


            });

        }
    }

    @Override
    public void onBackPressed() {
        btnAdicionarJogoManualmente.setVisibility(View.GONE);
        btnAdicionarJogoSucesso.setVisibility(View.GONE);
        txtCodigoDeBarras.setText("");
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        btnAdicionarJogoManualmente.setVisibility(View.GONE);
        btnAdicionarJogoSucesso.setVisibility(View.GONE);
        txtCodigoDeBarras.setText("");
        super.onDestroy();
    }
}
