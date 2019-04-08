package com.game.next.nextgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Jogo;

public class TransacaoAlugarActivity extends AppCompatActivity {

    private TextView txtTransacaoValorAluguel, txtTransacaoValorCaucao;

    private Jogo model;

    private String precoAluguelJogo, precoJogo, userId;

    private Button btnTransacaoAlugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao_alugar);

        txtTransacaoValorAluguel = (TextView) findViewById(R.id.txt_transacao_valor_aluguel);
        txtTransacaoValorCaucao = (TextView) findViewById(R.id.txt_transacao_valor_caucao);
        btnTransacaoAlugar = (Button) findViewById(R.id.btn_transacao_alugar);

        if (getIntent().hasExtra("USUARIOID")) {
            userId =  getIntent().getStringExtra("USUARIOID");
        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "USUARIOID",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "USUARIOID");
        }

        if (getIntent().hasExtra("JOGODOUSUARIO")) {
            model = (Jogo) getIntent().getSerializableExtra("JOGODOUSUARIO");
        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "JOGODOUSUARIO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "JOGODOUSUARIO");
        }

        if (getIntent().hasExtra("ALUGUELJOGODOUSUARIO")) {
            precoAluguelJogo = getIntent().getStringExtra("ALUGUELJOGODOUSUARIO");

            txtTransacaoValorAluguel.setText("R$" + precoAluguelJogo + ",00");

        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "ALUGUELJOGODOUSUARIO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "ALUGUELJOGODOUSUARIO");
        }

        if (getIntent().hasExtra("PRECOJOGODOUSUARIO")) {
            precoJogo = getIntent().getStringExtra("PRECOJOGODOUSUARIO");

            txtTransacaoValorCaucao.setText("R$" + precoJogo + ",00");

        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "PRECOJOGODOUSUARIO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "PRECOJOGODOUSUARIO");
        }

        btnTransacaoAlugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pagamentoIntent = new Intent(TransacaoAlugarActivity.this,PagamentoActivity.class);
                pagamentoIntent.putExtra("USUARIOID",userId);
                pagamentoIntent.putExtra("JOGODOUSUARIO",model);
                pagamentoIntent.putExtra("ALUGUELJOGODOUSUARIO",precoAluguelJogo);
                pagamentoIntent.putExtra("PRECOJOGODOUSUARIO",precoJogo);
                startActivity(pagamentoIntent);
            }
        });

    }
}
