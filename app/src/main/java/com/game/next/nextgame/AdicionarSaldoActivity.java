package com.game.next.nextgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdicionarSaldoActivity extends AppCompatActivity {

    private EditText edtAdicionarSaldo;

    private Button btnAdicionarSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_saldo);

        edtAdicionarSaldo = (EditText) findViewById(R.id.edt_adicionar_saldo);

        btnAdicionarSaldo = (Button) findViewById(R.id.btn_adicionar_saldo_verdadeiro);

        btnAdicionarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pagamentoIntent = new Intent(AdicionarSaldoActivity.this, PagamentoActivity.class);
                pagamentoIntent.putExtra("ADICIONARSALDO",edtAdicionarSaldo.getText().toString());
                startActivity(pagamentoIntent);
                finish();
            }
        });





    }
}
