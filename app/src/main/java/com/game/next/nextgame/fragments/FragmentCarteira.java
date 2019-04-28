package com.game.next.nextgame.fragments;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.AdicionarSaldoActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Carteira;
import com.game.next.nextgame.util.MoneyTextWatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FragmentCarteira extends Fragment {

    private Button btnAdicionar;
    private View view;

    private TextView txtCarteiraSaldo;

    private DatabaseReference reference;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_carteira, container, false);
        btnAdicionar = view.findViewById(R.id.btn_carteira_adicionar);
        txtCarteiraSaldo = view.findViewById(R.id.txt_carteira_saldo);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("CarteiraUsers").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                    String valorInteiro, centavos;

                    String array[] = userCarteira.getSaldo().split("\\.");

                    Log.d("CARTEIRA", "Carteira tem " + userCarteira.getSaldo());

                    if(array.length > 1) {
                        valorInteiro = array[0];
                        centavos = array[1];

                        if (array[1].length() == 1) {
                            centavos = centavos.concat("0");
                        }

                        txtCarteiraSaldo.setText("R$ " + valorInteiro + "," + centavos);
                    }else {
                        valorInteiro = array[0];
                        txtCarteiraSaldo.setText("R$ " + valorInteiro + ",00");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CARTEIRA","DEURUIM,");
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adicionarSaldoIntent = new Intent(getActivity(), AdicionarSaldoActivity.class);
                startActivity(adicionarSaldoIntent);
            }
        });

        return view;
    }
}
