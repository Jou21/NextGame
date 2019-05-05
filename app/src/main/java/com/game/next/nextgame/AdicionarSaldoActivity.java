package com.game.next.nextgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setSystemBarTheme(AdicionarSaldoActivity.this,true,R.color.branco);
        }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static final void setSystemBarTheme(final Activity pActivity, final boolean textIsDark, int corStatusBar) {

        Window window = pActivity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(pActivity,corStatusBar));

        // Fetch the current flags.
        final int lFlags = window.getDecorView().getSystemUiVisibility();
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        pActivity.getWindow().getDecorView().setSystemUiVisibility(textIsDark ? (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }
}
