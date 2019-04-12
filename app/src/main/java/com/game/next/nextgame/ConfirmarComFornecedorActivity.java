package com.game.next.nextgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Jogo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmarComFornecedorActivity extends AppCompatActivity {

    private String precoAluguelJogo, precoJogo;
    private String fornecedorId;

    private DatabaseReference referenceTransacaoUser;
    private FirebaseUser user;

    private Jogo model;

    private Button btnConversarComFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_com_fornecedor);

        btnConversarComFornecedor = (Button) findViewById(R.id.btn_conversar_com_fornecedor);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());

        if (getIntent().hasExtra("USUARIOID")) {
            fornecedorId = getIntent().getStringExtra("USUARIOID");
        } else {
            Toast.makeText(ConfirmarComFornecedorActivity.this, "Activity cannot find  extras " + "USUARIOID", Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO", "Activity cannot find  extras " + "USUARIOID");
        }

        if (getIntent().hasExtra("JOGODOUSUARIO")) {
            model = (Jogo) getIntent().getSerializableExtra("JOGODOUSUARIO");
        } else {
            Toast.makeText(ConfirmarComFornecedorActivity.this,"Activity cannot find  extras " + "JOGODOUSUARIO",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "JOGODOUSUARIO");
        }

        if (getIntent().hasExtra("ALUGUELJOGODOUSUARIO")) {
            precoAluguelJogo = getIntent().getStringExtra("ALUGUELJOGODOUSUARIO");

        } else {
            Toast.makeText(ConfirmarComFornecedorActivity.this, "Activity cannot find  extras " + "ALUGUELJOGODOUSUARIO", Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO", "Activity cannot find  extras " + "ALUGUELJOGODOUSUARIO");
        }

        if (getIntent().hasExtra("PRECOJOGODOUSUARIO")) {
            precoJogo = getIntent().getStringExtra("PRECOJOGODOUSUARIO");

        } else {
            Toast.makeText(ConfirmarComFornecedorActivity.this, "Activity cannot find  extras " + "PRECOJOGODOUSUARIO", Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO", "Activity cannot find  extras " + "PRECOJOGODOUSUARIO");
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", user.getUid());
        hashMap.put("fornecedorId", fornecedorId);
        hashMap.put("valorAluguel", precoAluguelJogo);
        hashMap.put("valorCaucao", precoJogo);
        hashMap.put("jogo",model);

        referenceTransacaoUser.push().setValue(hashMap);

        btnConversarComFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
