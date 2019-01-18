package com.game.next.nextgame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityCadastro extends AppCompatActivity {

    private EditText edtNome,edtEmail,edtSenha,edtConfirmeSuaSenha;
    private Button btnCadastrar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = (EditText) findViewById(R.id.edt_cadastro_nome);
        edtEmail = (EditText) findViewById(R.id.edt_cadastro_email);
        edtSenha = (EditText) findViewById(R.id.edt_cadastro_senha);
        edtConfirmeSuaSenha = (EditText) findViewById(R.id.edt_cadastro_confirma_senha);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastro_cadastrar);

        mAuth = FirebaseAuth.getInstance();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtSenha.getText().toString())
                        .addOnCompleteListener(ActivityCadastro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent it = new Intent(ActivityCadastro.this, ActivityLogin.class);
                                    startActivity(it);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(ActivityCadastro.this, "Erro: " + task.getException(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }
        });


    }
}
