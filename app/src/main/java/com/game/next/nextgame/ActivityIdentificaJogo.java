package com.game.next.nextgame;

import android.content.Context;
import android.content.Intent;

import android.graphics.PorterDuff;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.game.next.nextgame.adapters.MyAdapterImgGoogle;
import com.game.next.nextgame.adapters.MyAdapterListJogos;

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityIdentificaJogo extends AppCompatActivity {

    private TextView txtCodIdentificado, txtCodigoDeBarras, txtPrecoVendaJogo, txtPrecoAluguelJogo;
    private TextView txtSelecioneUmaCapaIdentificaJogo, txtObsSelecioneUmaCapaIdentificaJogo;
    private ImageView imgCodIdentificado;
    private Button btnAdicionarJogoManualmente, btnAdicionarJogoSucesso, btnAdicionarHorarioIdentificaJogo;
    private ArrayList<Jogo> jogosPS4 = new ArrayList<>();
    private ArrayList<Jogo> jogosXbox = new ArrayList<>();

    private ArrayList<Jogo> listTodosJogos = new ArrayList<>();
    private List<String> listNomeTodosJogos = new ArrayList<>();
    private AutoCompleteTextView autoCompletePesquisar;

    private LinearLayout laySeekAlugar, laySeekPreco, layAchouJogo, layNaoAchouJogo;
    private LinearLayout layHorarios1, layHorarios2, layHorarios3;
    private CheckBox checkHora1, checkHora2, checkHora3;
    private TextView txtHora1, txtHora2, txtHora3, txtExcluirHora1, txtExcluirHora2, txtExcluirHora3;

    private Button btnDomingo, btnSegunda, btnTerca, btnQuarta, btnQuinta, btnSexta, btnSabado;
    private Button btnAlugar, btnAlugarEVender, btnVender, btnNaoAcheiOJogo;

    private ScrollView scrollIdentificaJogo;

    private EditText edtDigiteONomeDeSeuJogoAqui;

    private String domingoSelecionado = "N";
    private String segundaSelecionado = "N";
    private String tercaSelecionado = "N";
    private String quartaSelecionado = "N";
    private String quintaSelecionado = "N";
    private String sextaSelecionado = "N";
    private String sabadoSelecionado = "N";

    private String codigoDeBarras = "";

    private String aluga = "S";
    private String vende = "S";

    private TimePicker horarioIdentificaJogo;
    private TimePicker horarioIdentificaJogo2;

    private Jogo jogoIdentificado = new Jogo();

    private boolean achouJogoPeloCodBar = false;

    private DatabaseReference reference;
    private FirebaseUser user;

    private MyAdapterListJogos adapter = null;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private SeekBar seekBarPreco, seekBarAluguel;

    private int progressChangedValuePreco = 60;
    private int progressChangedValueAluguel = 20;

    private int hora = 9;
    private int minuto = 30;

    private int hora2 = 10;
    private int minuto2 = 30;

    private int urlPosition = 0;

    private int quantidadeMaxDeHorarios = 1;

    private ProgressBar mProgressBar;

    private List<String> resultUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogo);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarIdentificaJogo);

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
        horarioIdentificaJogo = (TimePicker)findViewById(R.id.horario_identifica_jogo);
        horarioIdentificaJogo2 = (TimePicker)findViewById(R.id.horario_2_identifica_jogo);

        scrollIdentificaJogo = (ScrollView) findViewById(R.id.scroll_identifica_jogo);
        btnNaoAcheiOJogo = (Button) findViewById(R.id.btn_nao_achei_o_jogo);
        layNaoAchouJogo = (LinearLayout) findViewById(R.id.lay_nao_achou_jogo);
        layAchouJogo = (LinearLayout) findViewById(R.id.lay_achou_jogo);
        laySeekAlugar = (LinearLayout) findViewById(R.id.lay_seek_aluguel);
        laySeekPreco = (LinearLayout) findViewById(R.id.lay_seek_preco);
        btnAlugar = (Button) findViewById(R.id.btn_alugar_identifica_jogo);
        btnAlugarEVender = (Button) findViewById(R.id.btn_alugar_e_vender_identifica_jogo);
        btnVender = (Button) findViewById(R.id.btn_vender_identifica_jogo);

        autoCompletePesquisar = (AutoCompleteTextView) findViewById(R.id.pesquisarAutoComplete);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_identifica_jogo);
        txtSelecioneUmaCapaIdentificaJogo = (TextView) findViewById(R.id.txt_selecione_uma_capa_identifica_jogo);
        txtObsSelecioneUmaCapaIdentificaJogo = (TextView) findViewById(R.id.txt_obs_selecione_uma_capa_identifica_jogo);

        btnAdicionarHorarioIdentificaJogo = (Button) findViewById(R.id.btn_adicionar_horario_identifica_jogo);
        layHorarios1 = (LinearLayout) findViewById(R.id.lay_horarios_1);
        layHorarios2 = (LinearLayout) findViewById(R.id.lay_horarios_2);
        layHorarios3 = (LinearLayout) findViewById(R.id.lay_horarios_3);
        checkHora1 = (CheckBox) findViewById(R.id.check_hora1_identifica_jogo);
        checkHora2 = (CheckBox) findViewById(R.id.check_hora2_identifica_jogo);
        checkHora3 = (CheckBox) findViewById(R.id.check_hora3_identifica_jogo);
        txtHora1 = (TextView) findViewById(R.id.txt_hora1_identifica_jogo);
        txtHora2 = (TextView) findViewById(R.id.txt_hora2_identifica_jogo);
        txtHora3 = (TextView) findViewById(R.id.txt_hora3_identifica_jogo);
        txtExcluirHora1 = (TextView) findViewById(R.id.txt_excluir_hora1_identifica_jogo);
        txtExcluirHora2 = (TextView) findViewById(R.id.txt_excluir_hora2_identifica_jogo);
        txtExcluirHora3 = (TextView) findViewById(R.id.txt_excluir_hora3_identifica_jogo);

        edtDigiteONomeDeSeuJogoAqui = (EditText) findViewById(R.id.edt_digite_o_nome_de_seu_jogo_aqui);

        btnAlugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laySeekAlugar.setVisibility(View.VISIBLE);
                laySeekPreco.setVisibility(View.GONE);

                btnAlugar.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corRoxo), PorterDuff.Mode.MULTIPLY);
                btnAlugarEVender.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corAzul), PorterDuff.Mode.MULTIPLY);
                btnVender.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corAzul), PorterDuff.Mode.MULTIPLY);

                aluga = "S";
                vende = "N";

            }
        });

        btnAlugarEVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laySeekAlugar.setVisibility(View.VISIBLE);
                laySeekPreco.setVisibility(View.VISIBLE);

                btnAlugar.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corAzul), PorterDuff.Mode.MULTIPLY);
                btnAlugarEVender.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corRoxo), PorterDuff.Mode.MULTIPLY);
                btnVender.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corAzul), PorterDuff.Mode.MULTIPLY);

                aluga = "S";
                vende = "S";
            }
        });

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laySeekAlugar.setVisibility(View.GONE);
                laySeekPreco.setVisibility(View.VISIBLE);

                btnAlugar.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corAzul), PorterDuff.Mode.MULTIPLY);
                btnAlugarEVender.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corAzul), PorterDuff.Mode.MULTIPLY);
                btnVender.getBackground().setColorFilter(ContextCompat.getColor(ActivityIdentificaJogo.this, R.color.corRoxo), PorterDuff.Mode.MULTIPLY);

                aluga = "N";
                vende = "S";
            }
        });



        horarioIdentificaJogo.setIs24HourView(true);

        horarioIdentificaJogo.setCurrentHour(hora);
        horarioIdentificaJogo.setCurrentMinute(minuto);

        horarioIdentificaJogo.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hora = hourOfDay;
                minuto = minute;
            }
        });

        horarioIdentificaJogo2.setIs24HourView(true);

        horarioIdentificaJogo2.setCurrentHour(hora2);
        horarioIdentificaJogo2.setCurrentMinute(minuto2);

        horarioIdentificaJogo2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hora2 = hourOfDay;
                minuto2 = minute;
            }
        });

        btnAdicionarHorarioIdentificaJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantidadeMaxDeHorarios == 3){
                    Toast.makeText(ActivityIdentificaJogo.this, "A quantidade máxima de horários são 3. Exclua 1 horário se quiser adicionar outro!",Toast.LENGTH_LONG).show();
                }else{
                    if(layHorarios1.getVisibility() == View.GONE){
                        layHorarios1.setVisibility(View.VISIBLE);
                        txtHora1.setText(hora + ":" + minuto + " - " + hora2 + ":" + minuto2);
                        checkHora1.setChecked(true);
                        quantidadeMaxDeHorarios++;

                    }else if((layHorarios2.getVisibility() == View.GONE) && (layHorarios1.getVisibility() == View.VISIBLE)){
                        layHorarios2.setVisibility(View.VISIBLE);
                        txtHora2.setText(hora + ":" + minuto + " - " + hora2 + ":" + minuto2);
                        checkHora2.setChecked(true);
                        quantidadeMaxDeHorarios++;

                    }else if((layHorarios3.getVisibility() == View.GONE) && (layHorarios2.getVisibility() == View.VISIBLE) && (layHorarios1.getVisibility() == View.VISIBLE)){
                        layHorarios3.setVisibility(View.VISIBLE);
                        txtHora3.setText(hora + ":" + minuto + " - " + hora2 + ":" + minuto2);
                        checkHora3.setChecked(true);
                        quantidadeMaxDeHorarios++;

                    }
                }
            }
        });

        txtExcluirHora1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHora1.setChecked(false);
                layHorarios1.setVisibility(View.GONE);
                quantidadeMaxDeHorarios--;
            }
        });

        txtExcluirHora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHora2.setChecked(false);
                layHorarios2.setVisibility(View.GONE);
                quantidadeMaxDeHorarios--;
            }
        });

        txtExcluirHora3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHora3.setChecked(false);
                layHorarios3.setVisibility(View.GONE);
                quantidadeMaxDeHorarios--;
            }
        });

        //Jogar no Firebase só os horários que estiverem visiveis na tela e checados! E só adicionar se tiver pelo menos 1 checado!

        btnDomingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
            // TODO Auto-generated method stub
                if(domingoSelecionado.equals("N")) {
                    btnDomingo.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Domingo Adicionado!",Toast.LENGTH_SHORT).show();
                    domingoSelecionado = "S";
                }else{
                    btnDomingo.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    domingoSelecionado = "N";
                }
            }
        });

        btnSegunda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(segundaSelecionado.equals("N")) {
                    btnSegunda.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Segunda-feira Adicionado!",Toast.LENGTH_SHORT).show();
                    segundaSelecionado = "S";
                }else{
                    btnSegunda.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    segundaSelecionado = "N";
                }
            }
        });

        btnTerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(tercaSelecionado.equals("N")) {
                    btnTerca.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Terça-feira Adicionado!",Toast.LENGTH_SHORT).show();
                    tercaSelecionado = "S";
                }else{
                    btnTerca.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    tercaSelecionado = "N";
                }
            }
        });

        btnQuarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(quartaSelecionado.equals("N")) {
                    btnQuarta.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Quarta-feira Adicionado!",Toast.LENGTH_SHORT).show();
                    quartaSelecionado = "S";
                }else{
                    btnQuarta.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    quartaSelecionado = "N";
                }
            }
        });

        btnQuinta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(quintaSelecionado.equals("N")) {
                    btnQuinta.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Quinta-feira Adicionado!",Toast.LENGTH_SHORT).show();
                    quintaSelecionado = "S";
                }else{
                    btnQuinta.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    quintaSelecionado = "N";
                }
            }
        });

        btnSexta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(sextaSelecionado.equals("N")) {
                    btnSexta.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Sexta-feira Adicionado!",Toast.LENGTH_SHORT).show();
                    sextaSelecionado = "S";
                }else{
                    btnSexta.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    sextaSelecionado = "N";
                }
            }
        });

        btnSabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(sabadoSelecionado.equals("N")) {
                    btnSabado.setBackground(getResources().getDrawable(R.drawable.round_button_roxo));
                    Toast.makeText(ActivityIdentificaJogo.this,"Sábado Adicionado!",Toast.LENGTH_SHORT).show();
                    sabadoSelecionado = "S";
                }else{
                    btnSabado.setBackground(getResources().getDrawable(R.drawable.round_button_azul));
                    sabadoSelecionado = "N";
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
                if(((checkHora1.isChecked() == true) || (checkHora2.isChecked() == true) || (checkHora3.isChecked() == true)) &&
                        ((domingoSelecionado == "S") || (segundaSelecionado == "S") || (tercaSelecionado == "S") ||
                                (quartaSelecionado == "S") || (quintaSelecionado == "S") || (sextaSelecionado == "S") || (sabadoSelecionado == "S"))){

                    reference = FirebaseDatabase.getInstance().getReference();
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("userId", user.getUid());
                    hashMap.put("jogoId", codigoDeBarras);

                    if (achouJogoPeloCodBar == true) {
                        hashMap.put("imgJogo", jogoIdentificado.getUrlImgJogo());
                        hashMap.put("nomeJogo", jogoIdentificado.getNome());

                        hashMap.put("addManual", "N");
                        hashMap.put("aluga", aluga);
                        hashMap.put("vende", vende);

                        if(aluga == "S"){
                            hashMap.put("precoAluga", String.valueOf(progressChangedValueAluguel));
                        }else{
                            hashMap.put("precoAluga", "N");
                        }

                        if(vende == "S"){
                            hashMap.put("precoVenda", String.valueOf(progressChangedValuePreco));
                        }else{
                            hashMap.put("precoVenda", "N");
                        }

                        hashMap.put("domingo", domingoSelecionado);
                        hashMap.put("segunda", segundaSelecionado);
                        hashMap.put("terca", tercaSelecionado);
                        hashMap.put("quarta", quartaSelecionado);
                        hashMap.put("quinta", quintaSelecionado);
                        hashMap.put("sexta", sextaSelecionado);
                        hashMap.put("sabado", sabadoSelecionado);

                        if (checkHora1.isChecked() == true) {
                            hashMap.put("hora1", txtHora1.getText().toString());
                        } else {
                            hashMap.put("hora1", "N");
                        }

                        if (checkHora2.isChecked() == true) {
                            hashMap.put("hora2", txtHora2.getText().toString());
                        } else {
                            hashMap.put("hora2", "N");
                        }

                        if (checkHora3.isChecked() == true) {
                            hashMap.put("hora3", txtHora3.getText().toString());
                        } else {
                            hashMap.put("hora3", "N");
                        }

                        reference.child("UserGame").child(user.getUid()).push().setValue(hashMap);
                        btnAdicionarJogoSucesso.setVisibility(View.GONE);
                        Intent telaMeusJogos = new Intent(ActivityIdentificaJogo.this, ActivityMeusJogos.class);
                        startActivity(telaMeusJogos);
                        finish();

                    }else if(edtDigiteONomeDeSeuJogoAqui.getVisibility() == View.VISIBLE && achouJogoPeloCodBar == false){

                        if(edtDigiteONomeDeSeuJogoAqui.getText().length() != 0){
                            hashMap.put("imgJogo", resultUrls.get(urlPosition));
                            hashMap.put("nomeJogo", edtDigiteONomeDeSeuJogoAqui.getText().toString());

                            hashMap.put("addManual", "S");
                            hashMap.put("aluga", aluga);
                            hashMap.put("vende", vende);

                            if(aluga == "S"){
                                hashMap.put("precoAluga", String.valueOf(progressChangedValueAluguel));
                            }else{
                                hashMap.put("precoAluga", "N");
                            }

                            if(vende == "S"){
                                hashMap.put("precoVenda", String.valueOf(progressChangedValuePreco));
                            }else{
                                hashMap.put("precoVenda", "N");
                            }

                            hashMap.put("domingo", domingoSelecionado);
                            hashMap.put("segunda", segundaSelecionado);
                            hashMap.put("terca", tercaSelecionado);
                            hashMap.put("quarta", quartaSelecionado);
                            hashMap.put("quinta", quintaSelecionado);
                            hashMap.put("sexta", sextaSelecionado);
                            hashMap.put("sabado", sabadoSelecionado);

                            if (checkHora1.isChecked() == true) {
                                hashMap.put("hora1", txtHora1.getText().toString());
                            } else {
                                hashMap.put("hora1", "N");
                            }

                            if (checkHora2.isChecked() == true) {
                                hashMap.put("hora2", txtHora2.getText().toString());
                            } else {
                                hashMap.put("hora2", "N");
                            }

                            if (checkHora3.isChecked() == true) {
                                hashMap.put("hora3", txtHora3.getText().toString());
                            } else {
                                hashMap.put("hora3", "N");
                            }

                            reference.child("UserGame").child(user.getUid()).push().setValue(hashMap);
                            btnAdicionarJogoSucesso.setVisibility(View.GONE);
                            Intent telaMeusJogos = new Intent(ActivityIdentificaJogo.this, ActivityMeusJogos.class);
                            startActivity(telaMeusJogos);
                            finish();

                        }else{
                            Toast.makeText(ActivityIdentificaJogo.this,
                                    "É necessário digitar o nome do jogo!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        hashMap.put("imgJogo", jogoIdentificado.getUrlImgJogo());
                        hashMap.put("nomeJogo", jogoIdentificado.getNome());

                        hashMap.put("addManual", "N");
                        hashMap.put("aluga", aluga);
                        hashMap.put("vende", vende);

                        if(aluga == "S"){
                            hashMap.put("precoAluga", String.valueOf(progressChangedValueAluguel));
                        }else{
                            hashMap.put("precoAluga", "N");
                        }

                        if(vende == "S"){
                            hashMap.put("precoVenda", String.valueOf(progressChangedValuePreco));
                        }else{
                            hashMap.put("precoVenda", "N");
                        }

                        hashMap.put("domingo", domingoSelecionado);
                        hashMap.put("segunda", segundaSelecionado);
                        hashMap.put("terca", tercaSelecionado);
                        hashMap.put("quarta", quartaSelecionado);
                        hashMap.put("quinta", quintaSelecionado);
                        hashMap.put("sexta", sextaSelecionado);
                        hashMap.put("sabado", sabadoSelecionado);

                        if (checkHora1.isChecked() == true) {
                            hashMap.put("hora1", txtHora1.getText().toString());
                        } else {
                            hashMap.put("hora1", "N");
                        }

                        if (checkHora2.isChecked() == true) {
                            hashMap.put("hora2", txtHora2.getText().toString());
                        } else {
                            hashMap.put("hora2", "N");
                        }

                        if (checkHora3.isChecked() == true) {
                            hashMap.put("hora3", txtHora3.getText().toString());
                        } else {
                            hashMap.put("hora3", "N");
                        }

                        reference.child("UserGame").child(user.getUid()).push().setValue(hashMap);
                        btnAdicionarJogoSucesso.setVisibility(View.GONE);
                        Intent telaMeusJogos = new Intent(ActivityIdentificaJogo.this, ActivityMeusJogos.class);
                        startActivity(telaMeusJogos);
                        finish();

                    }


                }else {
                    Toast.makeText(ActivityIdentificaJogo.this,
                            "É necessário que ao menos 1 dia da semana esteja selecionado e pelo menos 1 horário esteja checado",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                    acionaDrop();

                    for(Jogo j : jogosPS4){

                        if (j == jogosPS4.get(jogosPS4.size()-1) && jogoPS4Encontrado[0] == false){
                            pesquisouJogosPS4[0] = true;
                        }

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
                            achouJogoPeloCodBar = true;
                            jogoIdentificado = j;
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            jogoPS4Encontrado[0] = true;
                            //btnAdicionarJogoManualmente.setVisibility(View.GONE);
                            btnAdicionarJogoSucesso.setVisibility(View.VISIBLE);
                            imgCodIdentificado.setVisibility(View.VISIBLE);
                            layAchouJogo.setVisibility(View.VISIBLE);
                            layNaoAchouJogo.setVisibility(View.GONE);

                            exibirProgress(false);


                        }else if (j == jogosPS4.get(jogosPS4.size()-1) && jogoXboxEncontrado[0] == false && jogoPS4Encontrado[0] == false && pesquisouJogosXbox[0] == true && pesquisouJogosPS4[0] == true) {
                            txtCodIdentificado.setText("Nenhum jogo encontrado");
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            //btnAdicionarJogoManualmente.setVisibility(View.VISIBLE);
                            layNaoAchouJogo.setVisibility(View.VISIBLE);
                            btnAdicionarJogoSucesso.setVisibility(View.GONE);
                            layAchouJogo.setVisibility(View.GONE);

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

                    acionaDrop();

                    for(Jogo j : jogosXbox){

                        if (j == jogosXbox.get(jogosXbox.size()-1) && jogoXboxEncontrado[0] == false){
                            pesquisouJogosXbox[0] = true;
                        }

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
                            achouJogoPeloCodBar = true;
                            jogoIdentificado = j;
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            jogoXboxEncontrado[0] = true;
                            //btnAdicionarJogoManualmente.setVisibility(View.GONE);
                            btnAdicionarJogoSucesso.setVisibility(View.VISIBLE);
                            imgCodIdentificado.setVisibility(View.VISIBLE);
                            layAchouJogo.setVisibility(View.VISIBLE);
                            layNaoAchouJogo.setVisibility(View.GONE);

                            exibirProgress(false);

                        }else if (j == jogosXbox.get(jogosXbox.size()-1) && jogoXboxEncontrado[0] == false && jogoPS4Encontrado[0] == false && pesquisouJogosXbox[0] == true && pesquisouJogosPS4[0] == true) {
                            txtCodIdentificado.setText("Nenhum jogo encontrado");
                            txtCodigoDeBarras.setText(finalCodigoDeBarras);
                            //btnAdicionarJogoManualmente.setVisibility(View.VISIBLE);
                            btnAdicionarJogoSucesso.setVisibility(View.GONE);
                            layAchouJogo.setVisibility(View.GONE);
                            layNaoAchouJogo.setVisibility(View.VISIBLE);

                            exibirProgress(false);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERRO", String.valueOf(databaseError.getCode()));
                }


            });


            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        btnNaoAcheiOJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollIdentificaJogo.setVisibility(View.GONE);
                txtSelecioneUmaCapaIdentificaJogo.setVisibility(View.VISIBLE);
                txtObsSelecioneUmaCapaIdentificaJogo.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

                exibirProgress(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            Document doc = Jsoup.connect("https://www.google.com.br/search?tbm=isch&sa=1&ei=xTIUW5nGJYSPwwSZspqACg&q=" + finalCodigoDeBarras + "&oq=" + finalCodigoDeBarras + "&gs_l=img.3...4944.4944.0.5656.1.1.0.0.0.0.116.116.0j1.1.0....0...1c.1.64.img..0.0.0....0.AuKFroOBgdI#imgrc=_").get();

                            Log.d("URL","https://www.google.com.br/search?tbm=isch&sa=1&ei=xTIUW5nGJYSPwwSZspqACg&q=" + finalCodigoDeBarras + "&oq=" + finalCodigoDeBarras + "&gs_l=img.3...4944.4944.0.5656.1.1.0.0.0.0.116.116.0j1.1.0....0...1c.1.64.img..0.0.0....0.AuKFroOBgdI#imgrc=_");
                            //Document doc1 = Jsoup.connect("https://cosmos.bluesoft.com.br/produtos/" + finalCodigoDeBarras).get();

                            //nomeProduto = doc1.getElementById("container-principal").getElementsByTag("h1").text();

                            Elements elements = doc.select("div.rg_meta");

                            JSONObject jsonObject;
                            for (Element element : elements) {
                                if (element.childNodeSize() > 0) {
                                    jsonObject = (JSONObject) new JSONParser().parse(element.childNode(0).toString());
                                    resultUrls.add((String) jsonObject.get("ou"));
                                }
                            }

                        /*
                        System.out.println("number of results: " + resultUrls.size());

                        for (String imageUrl : resultUrls) {
                            System.out.println(imageUrl);
                        }
                        */

                        //    url = resultUrls.get(0);


                        } catch (IOException e) {

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //mAdapter = new MyAdapterImgGoogle(resultUrls, recyclerView);
                                mAdapter = new MyAdapterImgGoogle(resultUrls, ActivityIdentificaJogo.this);
                                recyclerView.setAdapter(mAdapter);

                                exibirProgress(false);

                                //Picasso.get().load(url).into(imageView);
                                //txtCodBar.setText(nomeProduto);
                                //exibirProgress(false);
                            }
                        });
                    }
                }).start();

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

    private void acionaDrop(){
        //====================Auto Complete Pesquisar========================================================
        if(listTodosJogos.isEmpty() == false) {


            adapter = new MyAdapterListJogos(ActivityIdentificaJogo.this, listTodosJogos);
            autoCompletePesquisar.setAdapter(adapter);

            autoCompletePesquisar.setThreshold(1);//Começa a procurar do primeiro caractere

            autoCompletePesquisar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final Jogo selection = (Jogo) parent.getItemAtPosition(position);

                    jogoIdentificado = selection;

                    Picasso.get().load(selection.getUrlImgJogo()).into(imgCodIdentificado, new Callback() {
                        @Override
                        public void onSuccess() {
                            txtCodIdentificado.setVisibility(View.VISIBLE);
                            txtCodIdentificado.setText(selection.getNome());
                            scrollIdentificaJogo.setVisibility(View.VISIBLE);
                            //btnAdicionarJogoManualmente.setVisibility(View.GONE);
                            btnAdicionarJogoSucesso.setVisibility(View.VISIBLE);
                            imgCodIdentificado.setVisibility(View.VISIBLE);
                            layAchouJogo.setVisibility(View.VISIBLE);
                            layNaoAchouJogo.setVisibility(View.GONE);
                            hideKeyboard(ActivityIdentificaJogo.this,autoCompletePesquisar);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });



                }
            });

        }
        //======================================================================================================
    }

    public void capturaCapa(int posicao){

        urlPosition = posicao;

        Picasso.get().load(resultUrls.get(posicao)).into(imgCodIdentificado, new Callback() {
            @Override
            public void onSuccess() {
                scrollIdentificaJogo.setVisibility(View.VISIBLE);
                txtCodIdentificado.setVisibility(View.GONE);
                edtDigiteONomeDeSeuJogoAqui.setVisibility(View.VISIBLE);
                txtSelecioneUmaCapaIdentificaJogo.setVisibility(View.GONE);
                txtObsSelecioneUmaCapaIdentificaJogo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                btnAdicionarJogoSucesso.setVisibility(View.VISIBLE);
                imgCodIdentificado.setVisibility(View.VISIBLE);
                layAchouJogo.setVisibility(View.VISIBLE);
                layNaoAchouJogo.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
