package com.game.next.nextgame.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.game.next.nextgame.R;
import com.game.next.nextgame.adapters.MyAdapterTransacoes;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentTransacoes extends Fragment {

    private View view;

    private DatabaseReference referenceUsers, referenceTransacaoUser;
    private FirebaseUser user;
    private ArrayList<TransacaoUser> transacoesUsers = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transacoes, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarTransacoes);

        exibirProgress(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsers = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_transacoes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        referenceTransacaoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                transacoesUsers = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TransacaoUser transacaoUser = snapshot.getValue(TransacaoUser.class);
                    //String key = snapshot.getKey();

                    transacoesUsers.add(transacaoUser);
                }

                mAdapter = new MyAdapterTransacoes(transacoesUsers, referenceUsers);

                exibirProgress(false);

                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}
