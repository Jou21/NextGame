package com.game.next.nextgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.game.next.nextgame.adapters.MyAdapter;
import com.game.next.nextgame.adapters.MyAdapterTitulos;
import com.game.next.nextgame.entidades.Jogo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityIdentificaJogo extends AppCompatActivity {

    private TextView txtCodIdentificado;
    private ArrayList<Jogo> jogosPS4 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifica_jogo);

        txtCodIdentificado = (TextView) findViewById(R.id.txt_cod_identificado);

        Bundle b = getIntent().getExtras();
        String codigoDeBarras = "";
        if(b != null) {
            codigoDeBarras = b.getString("CODBAR");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("PS4");

            final String finalCodigoDeBarras = codigoDeBarras;
            myRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Jogo jogo = snapshot.getValue(Jogo.class);
                        jogosPS4.add(jogo);

                        if (jogo.getCodigoDeBarra().equals(finalCodigoDeBarras)) {
                            txtCodIdentificado.setText(jogo.getNome());
                        }
                         //if (jogo.getCodigoDeBarra1().equals(finalCodigoDeBarras)) {
                         //   txtCodIdentificado.setText(jogo.getNome());
                        //}
                         //if (jogo.getCodigoDeBarra2().equals(finalCodigoDeBarras)) {
                         //   txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //if (jogo.getCodigoDeBarra3().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getCodigoDeBarra4());
                        //}
                        //else if (jogo.getCodigoDeBarra4().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //else if (jogo.getCodigoDeBarra5().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //else if (jogo.getCodigoDeBarra6().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //else if (jogo.getCodigoDeBarra7().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //else if (jogo.getCodigoDeBarra8().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //else if (jogo.getCodigoDeBarra9().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}
                        //else if (jogo.getCodigoDeBarra10().equals(finalCodigoDeBarras)) {
                        //    txtCodIdentificado.setText(jogo.getNome());
                        //}

                    }


                    //for(Jogo j : jogosPS4){


                    //}

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERRO", String.valueOf(databaseError.getCode()));
                }


            });



        }
    }
}
