package com.game.next.nextgame.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.game.next.nextgame.ActivityQuemTemOJogo;
import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.fragments.FragmentB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MyAdapterOfRecyclersB extends RecyclerView.Adapter<MyAdapterOfRecyclersB.ViewHolder> {

    static final String PACKAGE_ID = "com.game.next.nextgame:id/";

    private FragmentB fragmentB;
    private LinearLayoutManager layoutManager;
    private MyAdapterTitulos[] myAdapterTitulos = new MyAdapterTitulos[8];
    private MyAdapter[] myAdapter = new MyAdapter[8];

    private MyAdapterListJogos adapter = null;
    private AutoCompleteTextView autoCompletePesquisar;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public RecyclerView recyclerView;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        }
    }

    public MyAdapterOfRecyclersB(final FragmentB fragmentB, AutoCompleteTextView autoCompletePesquisar) {
        this.fragmentB = fragmentB;
        this.autoCompletePesquisar = autoCompletePesquisar;

        final ArrayList<Jogo> jogosPS4 = new ArrayList<>();

        final ArrayList<Jogo> jogosPS4AcaoEAventura = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4Luta = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4CriancaEFamilia = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4CorridaEVoo = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4RPG = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4Atirador = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4Esportes = new ArrayList<>();
        final ArrayList<Jogo> jogosPS4Estretegia = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PS4");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Jogo jogo = snapshot.getValue(Jogo.class);
                    jogosPS4.add(jogo);
                }

                acionaDrop(fragmentB, jogosPS4);

                for(Jogo j : jogosPS4){
                    if(j.getCategoria().equals("Ação e Aventura")){
                        jogosPS4AcaoEAventura.add(j);
                    }
                    if(j.getCategoria().equals("Luta")){
                        jogosPS4Luta.add(j);
                    }
                    if(j.getCategoria().equals("Crianças e Família")){
                        jogosPS4CriancaEFamilia.add(j);
                    }
                    if(j.getCategoria().equals("Corrida e Voo")){
                        jogosPS4CorridaEVoo.add(j);
                    }
                    if(j.getCategoria().equals("RPG")){
                        jogosPS4RPG.add(j);
                    }
                    if(j.getCategoria().equals("Atirador")){
                        jogosPS4Atirador.add(j);
                    }
                    if(j.getCategoria().equals("Esportes")){
                        jogosPS4Esportes.add(j);
                    }
                    if(j.getCategoria().equals("Estratégia")){
                        jogosPS4Estretegia.add(j);
                    }
                }
                myAdapterTitulos[0] = new MyAdapterTitulos("Ação e Aventura","#ffffff");
                myAdapter[0] = new MyAdapter(jogosPS4AcaoEAventura,"#ffffff");
                myAdapterTitulos[1] = new MyAdapterTitulos("Luta","#ffffff");
                myAdapter[1] = new MyAdapter(jogosPS4Luta,"#ffffff");
                myAdapterTitulos[2] = new MyAdapterTitulos("Crianças e Família","#ffffff");
                myAdapter[2] = new MyAdapter(jogosPS4CriancaEFamilia,"#ffffff");
                myAdapterTitulos[3] = new MyAdapterTitulos("Corrida e Voo","#ffffff");
                myAdapter[3] = new MyAdapter(jogosPS4CorridaEVoo,"#ffffff");
                myAdapterTitulos[4] = new MyAdapterTitulos("RPG","#ffffff");
                myAdapter[4] = new MyAdapter(jogosPS4RPG,"#ffffff");
                myAdapterTitulos[5] = new MyAdapterTitulos("Atirador","#ffffff");
                myAdapter[5] = new MyAdapter(jogosPS4Atirador,"#ffffff");
                myAdapterTitulos[6] = new MyAdapterTitulos("Esportes","#ffffff");
                myAdapter[6] = new MyAdapter(jogosPS4Esportes,"#ffffff");
                myAdapterTitulos[7] = new MyAdapterTitulos("Estratégia","#ffffff");
                myAdapter[7] = new MyAdapter(jogosPS4Estretegia,"#ffffff");

                fragmentB.getMAdapterOfRecyclers().notifyDataSetChanged();
                fragmentB.exibirProgress(false);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERRO", String.valueOf(databaseError.getCode()));
            }


        });
    }

    @Override
    public MyAdapterOfRecyclersB.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_recycler, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position == 0){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[0]);
        }
        if(position == 1){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[0]);
        }
        if(position == 2){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[1]);
        }
        if(position == 3){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[1]);
        }
        if(position == 4){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[2]);
        }
        if(position == 5){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[2]);
        }
        if(position == 6){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[3]);
        }
        if(position == 7){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[3]);
        }
        if(position == 8){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[4]);
        }
        if(position == 9){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[4]);
        }
        if(position == 10){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[5]);
        }
        if(position == 11){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[5]);
        }
        if(position == 12){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[6]);
        }
        if(position == 13){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[6]);
        }
        if(position == 14){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[7]);
        }
        if(position == 15){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentB.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[7]);
        }
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    private void acionaDrop(final FragmentB fragmentB, ArrayList<Jogo> listTodosJogosPS4){
        //====================Auto Complete Pesquisar========================================================
        if(listTodosJogosPS4.isEmpty() == false) {

            if(fragmentB != null && autoCompletePesquisar != null){

                    adapter = new MyAdapterListJogos(fragmentB.getContext(), listTodosJogosPS4);
                    autoCompletePesquisar.setAdapter(adapter);

                    autoCompletePesquisar.setThreshold(2);//Começa a procurar do segundo caractere

                    autoCompletePesquisar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            final Jogo jogoSelecionado = (Jogo) parent.getItemAtPosition(position);

                            Intent quemTemOJogo = new Intent(fragmentB.getContext(), ActivityQuemTemOJogo.class);
                            quemTemOJogo.putExtra("JOGO", jogoSelecionado);
                            fragmentB.getContext().startActivity(quemTemOJogo);
                            //fragmentB.getActivity().finish();

                        }
                    });
                //}

            }



        }
        //======================================================================================================
    }

}