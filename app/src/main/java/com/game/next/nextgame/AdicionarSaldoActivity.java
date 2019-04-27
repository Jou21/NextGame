package com.game.next.nextgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.game.next.nextgame.util.MoneyTextWatcher;

public class AdicionarSaldoActivity extends AppCompatActivity {

    private EditText edtAdicionarSaldo;

    private Button btnAdicionarSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_saldo);

        edtAdicionarSaldo = (EditText) findViewById(R.id.edt_adicionar_saldo);
        btnAdicionarSaldo = (Button) findViewById(R.id.btn_adicionar_saldo_verdadeiro);

        edtAdicionarSaldo.addTextChangedListener(new MoneyTextWatcher(edtAdicionarSaldo));

        btnAdicionarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saldoParaAddCarteira, valor, valor2, valor3, valor4;
                Double valorDouble;

                valor = edtAdicionarSaldo.getText().toString();
                valor2 = valor.replace("R$", "");
                valor3 = valor2.replace(".", "");
                valor4 = valor3.replace(",", ".");

                valorDouble = Double.parseDouble(valor4);

                if(valorDouble > 0.0){

                    saldoParaAddCarteira = String.valueOf(valorDouble);

                    Intent pagamentoIntent = new Intent(AdicionarSaldoActivity.this, PagamentoActivity.class);
                    pagamentoIntent.putExtra("ADICIONARSALDO", saldoParaAddCarteira);
                    startActivity(pagamentoIntent);
                    finish();

                } else {
                    Toast.makeText(AdicionarSaldoActivity.this, "Você precisa adicionar um valor maior que zero!", Toast.LENGTH_LONG).show();
                }


            }
        });





    }
}
