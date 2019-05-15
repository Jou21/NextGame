package com.game.next.nextgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.game.next.nextgame.adapters.MyAdapterListaScan;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ListaScanUsuariosProximosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseUser user;
    private DatabaseReference referenceUsers, referenceCarteiraUser, referenceTransacaoUser;


    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_scan_usuarios_proximos);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarListaScan);

        exibirProgress(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsers = FirebaseDatabase.getInstance().getReference("Users");
        referenceCarteiraUser = FirebaseDatabase.getInstance().getReference("CarteiraUsers");
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            ArrayList<TransacaoUser> transacoesUsers = (ArrayList<TransacaoUser>) extras.getSerializable("LISTATRANSACOES");
            ArrayList<TransacaoUser> transacaoUsers = (ArrayList<TransacaoUser>) extras.getSerializable("LISTATRANSACAOUSERS");
            HashMap<String,String> hashMap = (HashMap<String,String>) extras.getSerializable("HASHMAPLISTATRASANCAO");


            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_lista_scan);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new MyAdapterListaScan(transacoesUsers, referenceUsers, referenceCarteiraUser, referenceTransacaoUser, transacaoUsers, hashMap);

            exibirProgress(false);

            recyclerView.setAdapter(mAdapter);
        }
    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
