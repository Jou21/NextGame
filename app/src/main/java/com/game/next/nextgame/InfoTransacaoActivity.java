package com.game.next.nextgame;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.game.next.nextgame.entidades.TransacaoUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InfoTransacaoActivity extends AppCompatActivity {

    private TextView txtParabensInfoTransacao, txtPegouHoraInfoTransacao, txtPegouDataInfoTransacao;
    private TextView txtDevolverDataInfoTransacao;

    private ConstraintLayout layInfoTransacaoAluga;

    private TransacaoUser transacaoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_transacao);

        txtParabensInfoTransacao = (TextView) findViewById(R.id.txt_parabens_info_transacao);
        txtPegouDataInfoTransacao = (TextView) findViewById(R.id.txt_pegou_data_info_transacao);
        txtPegouHoraInfoTransacao = (TextView) findViewById(R.id.txt_pegou_hora_info_transacao);
        txtDevolverDataInfoTransacao = (TextView) findViewById(R.id.txt_devolver_data_info_transacao);
        layInfoTransacaoAluga = (ConstraintLayout) findViewById(R.id.lay_info_transacao_aluga);

        if(getIntent().hasExtra("TRANSACAO")){
            transacaoUser = (TransacaoUser) getIntent().getExtras().getSerializable("TRANSACAO");

            if(transacaoUser.getStatus().equals("CONCLUIDO")){
                layInfoTransacaoAluga.setVisibility(View.GONE);
                txtParabensInfoTransacao.setVisibility(View.VISIBLE);
                txtParabensInfoTransacao.setText("Você pegou o jogo com sucesso!");
            }else if(transacaoUser.getStatus().equals("ENTREGADO")){
                layInfoTransacaoAluga.setVisibility(View.VISIBLE);
                txtParabensInfoTransacao.setVisibility(View.GONE);

                String array[] = transacaoUser.getTime().split("-");

                String horario = array[0];
                String data = array[1];

                txtPegouHoraInfoTransacao.setText(horario);
                txtPegouDataInfoTransacao.setText(data);

                try {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    df.setLenient (false);
                    Date dt = df.parse(data);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dt);
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    dt = cal.getTime();

                    txtDevolverDataInfoTransacao.setText(df.format (dt));

                } catch (ParseException e) {
                    txtDevolverDataInfoTransacao.setText("7 dias depois");
                }


            }else {
                layInfoTransacaoAluga.setVisibility(View.GONE);
                txtParabensInfoTransacao.setVisibility(View.VISIBLE);
                txtParabensInfoTransacao.setText("Você ainda não pegou o jogo!");
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
