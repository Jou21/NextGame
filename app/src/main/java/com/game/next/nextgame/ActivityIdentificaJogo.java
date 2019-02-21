package com.game.next.nextgame;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.adapters.MyAdapter;
import com.game.next.nextgame.adapters.MyAdapterTitulos;
import com.game.next.nextgame.entidades.Jogo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityIdentificaJogo extends AppCompatActivity {

    private TextView txtCodIdentificado, txtCodigoDeBarras, txtPrecoVendaJogo, txtPrecoAluguelJogo;
    private ImageView imgCodIdentificado;
    private Button btnAdicionarJogoManualmente, btnAdicionarJogoSucesso;
    private ArrayList<Jogo> jogosPS4 = new ArrayList<>();
    private ArrayList<Jogo> jogosXbox = new ArrayList<>();

    private Button btnDomingo, btnSegunda, btnTerca, btnQuarta, btnQuinta, btnSexta, btnSabado;

    private boolean domingoSelecionado = false;
    private boolean segundaSelecionado = false;
    private boolean tercaSelecionado = false;
    private boolean quartaSelecionado = false;
    private boolean quintaSelecionado = false;
    private boolean sextaSelecionado = false;
    private boolean sabadoSelecionado = false;


    private Jogo jogoIdentificado = new Jogo();

    private DatabaseReference reference;
    private FirebaseUser user;

    private SeekBar seekBarPreco, seekBarAluguel;

    private int progressChangedValuePreco = 0;
    private int progressChangedValueAluguel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogo);

        txtPrecoAluguelJogo = (TextView) findViewById(R.id.txt_preco_aluguel_jogo);
        txtPrecoVendaJogo = (TextView) findViewById(R.id.txt_preco_venda_jogo);
        txtCodIdentificado = (TextView) findViewById(R.id.txt_cod_identificado);
        imgCodIdentificado = (ImageView) findViewById(R.id.img_cod_identificado);
        btnAdicionarJogoManualmente = (Button) findViewById(R.id.btn_adicionar_jogo_manualmente);
        txtCodigoDeBarras = (TextView) findViewById(R.id.txt_codigo_de_barras_identifica_jogo);
        btnAdicionarJogoSucesso = (Button) findViewById(R.id.btn_adicionar_jogo_sucesso);
        seekBarPreco = (SeekBar)findViewById(R.id.seek_bar_preco);
        seekBarAluguel = (SeekBar)findViewById(R.id.seek_bar_aluguel);
        btnDomingo = (Button) findViewById(R.id.btn_domingo);
        btnSegunda = (Button) findViewById(R.id.btn_segunda);
        btnTerca = (Button) findViewById(R.id.btn_terca);
        btnQuarta = (Button) findViewById(R.id.btn_quarta);
        btnQuinta = (Button) findViewById(R.id.btn_quinta);
        btnSexta = (Button) findViewById(R.id.btn_sexta);
        btnSabado = (Button) findViewById(R.id.btn_sabado);


        btnDomingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            // TODO Auto-generated method stub
                if(domingoSelecionado == false) {
                    btnDomingo.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    domingoSelecionado = true;
                }else{
                    btnDomingo.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    domingoSelecionado = false;
                }
            }
        });

        btnSegunda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(segundaSelecionado == false) {
                    btnSegunda.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    segundaSelecionado = true;
                }else{
                    btnSegunda.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    segundaSelecionado = false;
                }
            }
        });

        btnTerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(tercaSelecionado == false) {
                    btnTerca.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    tercaSelecionado = true;
                }else{
                    btnTerca.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    tercaSelecionado = false;
                }
            }
        });

        btnQuarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(quartaSelecionado == false) {
                    btnQuarta.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    quartaSelecionado = true;
                }else{
                    btnQuarta.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    quartaSelecionado = false;
                }
            }
        });

        btnQuinta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(quintaSelecionado == false) {
                    btnQuinta.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    quintaSelecionado = true;
                }else{
                    btnQuinta.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    quintaSelecionado = false;
                }
            }
        });

        btnSexta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(sextaSelecionado == false) {
                    btnSexta.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    sextaSelecionado = true;
                }else{
                    btnSexta.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    sextaSelecionado = false;
                }
            }
        });

        btnSabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(sabadoSelecionado == false) {
                    btnSabado.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    sabadoSelecionado = true;
                }else{
                    btnSabado.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    sabadoSelecionado = false;
                }
            }
        });

        seekBarPreco.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValuePreco = progress;
                txtPrecoVendaJogo.setText("R$ " + String.valueOf(progressChangedValuePreco) + ",00");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(ActivityIdentificaJogo.this, "Seek bar progress is :" + progressChangedValue,
                //        Toast.LENGTH_SHORT).show();
            }
        });

        seekBarAluguel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValueAluguel = progress;
                txtPrecoAluguelJogo.setText("R$ " + String.valueOf(progressChangedValueAluguel) + ",00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnAdicionarJogoSucesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference();
                user = FirebaseAuth.getInstance().getCurrentUser();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", user.getUid());
                hashMap.put("jogoId", jogoIdentificado.getCodigoDeBarra());
                hashMap.put("imgJogo", jogoIdentificado.getUrlImgJogo());
                hashMap.put("nomeJogo", jogoIdentificado.getNome());

                reference.child("UserGame").child(user.getUid()).push().setValue(hashMap);
                btnAdicionarJogoSucesso.setVisibility(View.GONE);
                Intent telaMeusJogos = new Intent(ActivityIdentificaJogo.this, ActivityMeusJogos.class);
                startActivity(telaMeusJogos);
                finish();
            }
        });

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
                            jogoIdentificado = j;
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
                            jogoIdentificado = j;
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
