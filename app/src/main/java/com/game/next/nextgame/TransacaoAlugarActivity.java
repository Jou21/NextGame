package com.game.next.nextgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Carteira;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.game.next.nextgame.entidades.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class TransacaoAlugarActivity extends AppCompatActivity {

    private TextView txtTransacaoValorAluguel, txtTransacaoValorCaucao, txtTransacaoValorNaCarteira;

    private Jogo model;

    private String precoAluguelJogo, precoJogo, fornecedorId, time, data;

    private Button btnTransacaoAlugar, btnAdicionarSaldo;

    private DatabaseReference reference, referenceTransacaoUser;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacao_alugar);

        txtTransacaoValorAluguel = (TextView) findViewById(R.id.txt_transacao_valor_aluguel);
        txtTransacaoValorCaucao = (TextView) findViewById(R.id.txt_transacao_valor_caucao);
        btnTransacaoAlugar = (Button) findViewById(R.id.btn_transacao_alugar);
        txtTransacaoValorNaCarteira = (TextView) findViewById(R.id.txt_transacao_valor_na_carteira);
        btnAdicionarSaldo = (Button) findViewById(R.id.btn_transacao_adicionar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("CarteiraUsers").child(user.getUid());
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                    if(userCarteira.getSaldo().equals("N") || userCarteira.getSaldo().equals("S")){
                        txtTransacaoValorNaCarteira.setText("N");
                    }else {

                        String valorInteiro, centavos;

                        String array[] = userCarteira.getSaldo().split("\\.");

                        if(array.length > 1) {
                            valorInteiro = array[0];
                            centavos = array[1];

                            if (array[1].length() == 1) {
                                centavos = centavos.concat("0");
                            }

                            txtTransacaoValorNaCarteira.setText("R$ " + valorInteiro + "," + centavos);
                        }else {
                            valorInteiro = array[0];
                            txtTransacaoValorNaCarteira.setText("R$ " + valorInteiro + ",00");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CARTEIRA","DEURUIM,");
            }
        });

        if (getIntent().hasExtra("TIME")) {
            time =  getIntent().getStringExtra("TIME");
            //Toast.makeText(TransacaoAlugarActivity.this,""+fornecedorId,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "TIME",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "TIME");
        }

        if (getIntent().hasExtra("USUARIOID")) {
            fornecedorId =  getIntent().getStringExtra("USUARIOID");
            //Toast.makeText(TransacaoAlugarActivity.this,""+fornecedorId,Toast.LENGTH_SHORT).show();
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

            String valorInteiro, centavos;

            String array[] = precoAluguelJogo.split("\\.");

            if(array.length > 1) {
                valorInteiro = array[0];
                centavos = array[1];

                if (array[1].length() == 1) {
                    centavos = centavos.concat("0");
                }

                txtTransacaoValorAluguel.setText("R$ " + valorInteiro + "," + centavos);
            }else {
                valorInteiro = array[0];
                txtTransacaoValorAluguel.setText("R$ " + valorInteiro + ",00");
            }


        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "ALUGUELJOGODOUSUARIO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "ALUGUELJOGODOUSUARIO");
        }

        if (getIntent().hasExtra("PRECOJOGODOUSUARIO")) {
            precoJogo = getIntent().getStringExtra("PRECOJOGODOUSUARIO");

            String valorInteiro, centavos;

            String array[] = precoJogo.split("\\.");

            if(array.length > 1) {
                valorInteiro = array[0];
                centavos = array[1];

                if(array[1].length() == 1){
                    centavos = centavos.concat("0");
                }

                txtTransacaoValorCaucao.setText("R$ " + valorInteiro + "," + centavos);
            }else {
                valorInteiro = array[0];
                txtTransacaoValorCaucao.setText("R$ " + valorInteiro + ",00");
            }

        } else {
            Toast.makeText(TransacaoAlugarActivity.this,"Activity cannot find  extras " + "PRECOJOGODOUSUARIO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "PRECOJOGODOUSUARIO");
        }

        btnTransacaoAlugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorNaCarteira = txtTransacaoValorNaCarteira.getText().toString();
                String valorNaCarteiraNew = "";
                String valorNaCarteiraNew2 = "";
                String valorNaCarteiraNew3 = "";
                valorNaCarteiraNew = valorNaCarteira.replace("R$ ", "");
                valorNaCarteiraNew2 = valorNaCarteiraNew.replace("R$", "");
                valorNaCarteiraNew3 = valorNaCarteiraNew2.replace(",", ".");
                Double valorNaCarteiraDouble = Double.parseDouble(valorNaCarteiraNew3);

                String valorCaucao = txtTransacaoValorCaucao.getText().toString();
                String valorCaucaoNew = "";
                String valorCaucaoNew2 = "";
                String valorCaucaoNew3 = "";
                valorCaucaoNew = valorCaucao.replace("R$ ", "");
                valorCaucaoNew2 = valorCaucaoNew.replace("R$", "");
                valorCaucaoNew3 = valorCaucaoNew2.replace(",", ".");
                Double valorCaucaoDouble = Double.parseDouble(valorCaucaoNew3);

                Calendar rightNow = Calendar.getInstance();
                TimeZone tz = TimeZone.getTimeZone("GMT-3:00");
                rightNow.setTimeZone(tz);
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                int minute = rightNow.get(Calendar.MINUTE);
                int second = rightNow.get(Calendar.SECOND);
                int dia = rightNow.get(Calendar.DAY_OF_MONTH);
                int mesZeroAteOnze = rightNow.get(Calendar.MONTH);
                int mesUmAteDoze = mesZeroAteOnze + 1;
                int ano = rightNow.get(Calendar.YEAR);

                data = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second) + "-" + String.valueOf(dia) + "/" + String.valueOf(mesUmAteDoze) + "/" + String.valueOf(ano);

                if(!valorNaCarteiraNew3.equals("N") && !valorCaucaoNew3.equals("N") && !valorNaCarteiraNew3.equals("S") && !valorCaucaoNew3.equals("S")){

                    if (valorNaCarteiraDouble >= valorCaucaoDouble) {

                        referenceTransacaoUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                boolean entrou = false;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    TransacaoUser transacaoUser = snapshot.getValue(TransacaoUser.class);
                                    String key = snapshot.getKey();

                                    if ((transacaoUser.getJogo().getCodigoDeBarra().equals(model.getCodigoDeBarra()) && transacaoUser.getFornecedorId().equals(fornecedorId)) && transacaoUser.getTime().equals(time)) {

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("userId", user.getUid());
                                        hashMap.put("fornecedorId", fornecedorId);
                                        hashMap.put("valorAluguel", precoAluguelJogo);
                                        hashMap.put("valorCaucao", precoJogo);
                                        hashMap.put("jogo", model);
                                        hashMap.put("time", data);
                                        hashMap.put("status", "INICIO");

                                        referenceTransacaoUser.child(key).setValue(hashMap);

                                        entrou = true;
                                        break;
                                    }
                                }

                                if (entrou == false) {

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("userId", user.getUid());
                                    hashMap.put("fornecedorId", fornecedorId);
                                    hashMap.put("valorAluguel", precoAluguelJogo);
                                    hashMap.put("valorCaucao", precoJogo);
                                    hashMap.put("jogo", model);
                                    hashMap.put("time", data);
                                    hashMap.put("status", "INICIO");

                                    referenceTransacaoUser.push().setValue(hashMap);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent intent = new Intent(TransacaoAlugarActivity.this, MessageActivity.class);
                        intent.putExtra("userid", fornecedorId);
                        intent.putExtra("MOSTRADIALOG", "ALUGAR");
                        intent.putExtra("JOGODOUSUARIO", model);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(TransacaoAlugarActivity.this, "Você não tem saldo suficiente! Adicione saldo para continuar.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(TransacaoAlugarActivity.this, "Não foi possível ler o valor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAdicionarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adicionarSaldoIntent = new Intent(TransacaoAlugarActivity.this, AdicionarSaldoActivity.class);
                startActivity(adicionarSaldoIntent);
            }
        });

    }
}
