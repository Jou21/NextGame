package com.game.next.nextgame.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.game.next.nextgame.ActivityIdentificaJogo;
import com.game.next.nextgame.ActivityMeusJogos;
import com.game.next.nextgame.CaptureActivityPortrait;
import com.game.next.nextgame.CustomScannerActivity;
import com.game.next.nextgame.R;
import com.game.next.nextgame.VerificaSeAchouOJogoCorretoActivity;
import com.game.next.nextgame.adapters.MyAdapterMeusJogos;
import com.game.next.nextgame.entidades.UserGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class FragmentMeusJogos extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBar mProgressBar;

    private Button btn_meus_jogos_adicionar_jogo;

    private DatabaseReference reference;
    private FirebaseUser user;

    private ArrayList<UserGame> userGames = new ArrayList<>();

    private String contents = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meus_jogos, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarMeusJogos);

        btn_meus_jogos_adicionar_jogo = (Button) view.findViewById(R.id.btn_meus_jogos_adicionar_jogo);
        btn_meus_jogos_adicionar_jogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setPrompt("Mantenha um palmo de distancia do código de barras");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                //integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();

            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_meus_jogos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        exibirProgress(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserGame").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userGames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserGame userGame = snapshot.getValue(UserGame.class);
                    userGames.add(userGame);
                }

                //mAdapter = new MyAdapterMeusJogos(userGames, reference, mAdapter);
                mAdapter2 = new MyAdapterMeusJogos(userGames, reference);

                exibirProgress(false);

                recyclerView.setAdapter(mAdapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        //Log.d("CODBAR",""+intent.getStringExtra("SCAN_RESULT"));

        //Toast.makeText(getActivity(),"eae",Toast.LENGTH_LONG);

        if(scanningResult != null){
            if(scanningResult.getContents() != null) {
                if (intent.hasExtra("SCAN_RESULT")) {

                    contents = intent.getStringExtra("SCAN_RESULT");
                    //format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                    Log.d("CODBAR", "" + contents);

                    Intent verificaSeAchouOJogoCorreto = new Intent(getActivity(), VerificaSeAchouOJogoCorretoActivity.class);
                    verificaSeAchouOJogoCorreto.putExtra("CODBAR", contents);
                    startActivity(verificaSeAchouOJogoCorreto);
                    //Intent telaIdentificaJogo = new Intent(getActivity(), ActivityIdentificaJogo.class);
                    //telaIdentificaJogo.putExtra("CODBAR", contents);
                    //startActivity(telaIdentificaJogo);
                    //getActivity().finish()

                } else {
                    Toast.makeText(getActivity(), "Activity cannot find  extras " + "SCAN_RESULT", Toast.LENGTH_SHORT).show();
                    Log.d("EXTRASJOGO", "Activity cannot find  extras " + "SCAN_RESULT");
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }



    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

}
