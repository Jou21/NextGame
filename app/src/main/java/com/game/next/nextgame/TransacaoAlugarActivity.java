package com.game.next.nextgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Jogo;

public class TransacaoAlugarActivity extends AppCompatActivity {

    private TextView txtTransacaoValorAluguel, txtTransacaoValorCaucao;

    private Jogo model;

    private String precoAluguelJogo, precoJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao_alugar);

        txtTransacaoValorAluguel = (TextView) findViewById(R.id.txt_transacao_valor_aluguel);
        txtTransacaoValorCaucao = (TextView) findViewById(R.id.txt_transacao_valor_caucao);

        if (getIntent().hasExtra("JOGOUSER")) {
            model = (Jogo) getIntent().getSerializableExtra("JOGOUSER");
        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "JOGOUSER",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "JOGOUSER");
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

    }
}
