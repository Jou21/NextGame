package com.game.next.nextgame.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.fragments.FragmentA;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAdapterOfRecyclers extends RecyclerView.Adapter<MyAdapterOfRecyclers.ViewHolder> {

    private FragmentA fragmentA;
    private LinearLayoutManager layoutManager;
    private MyAdapterTitulos[] myAdapterTitulos = new MyAdapterTitulos[8];
    private MyAdapter[] myAdapter = new MyAdapter[8];

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public RecyclerView recyclerView;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        }
    }

    public MyAdapterOfRecyclers(final FragmentA fragmentA) {
        this.fragmentA = fragmentA;

        final ArrayList<Jogo> jogosXbox = new ArrayList<>();

        final ArrayList<Jogo> jogosXboxAcaoEAventura = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxLuta = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxCriancaEFamilia = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxCorridaEVoo = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxRPG = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxAtirador = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxEsportes = new ArrayList<>();
        final ArrayList<Jogo> jogosXboxEstretegia = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Xbox");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Jogo jogo = snapshot.getValue(Jogo.class);
                    jogosXbox.add(jogo);
                }

                for(Jogo j : jogosXbox){
                    if (j.getCategoria().equals("Ação e Aventura")) {
                        jogosXboxAcaoEAventura.add(j);
                    }
                    if (j.getCategoria().equals("Luta")) {
                        jogosXboxLuta.add(j);
                    }
                    if (j.getCategoria().equals("Crianças e Família")) {
                        jogosXboxCriancaEFamilia.add(j);
                    }
                    if (j.getCategoria().equals("Corrida e Voo")) {
                        jogosXboxCorridaEVoo.add(j);
                    }
                    if (j.getCategoria().equals("RPG")) {
                        jogosXboxRPG.add(j);
                    }
                    if (j.getCategoria().equals("Atirador")) {
                        jogosXboxAtirador.add(j);
                    }
                    if (j.getCategoria().equals("Esportes")) {
                        jogosXboxEsportes.add(j);
                    }
                    if (j.getCategoria().equals("Estratégia")) {
                        jogosXboxEstretegia.add(j);
                    }
                }

                myAdapterTitulos[0] = new MyAdapterTitulos("Ação e Aventura");
                myAdapter[0] = new MyAdapter(jogosXboxAcaoEAventura);
                myAdapterTitulos[1] = new MyAdapterTitulos("Luta");
                myAdapter[1] = new MyAdapter(jogosXboxLuta);
                myAdapterTitulos[2] = new MyAdapterTitulos("Crianças e Família");
                myAdapter[2] = new MyAdapter(jogosXboxCriancaEFamilia);
                myAdapterTitulos[3] = new MyAdapterTitulos("Corrida e Voo");
                myAdapter[3] = new MyAdapter(jogosXboxCorridaEVoo);
                myAdapterTitulos[4] = new MyAdapterTitulos("RPG");
                myAdapter[4] = new MyAdapter(jogosXboxRPG);
                myAdapterTitulos[5] = new MyAdapterTitulos("Atirador");
                myAdapter[5] = new MyAdapter(jogosXboxAtirador);
                myAdapterTitulos[6] = new MyAdapterTitulos("Esportes");
                myAdapter[6] = new MyAdapter(jogosXboxEsportes);
                myAdapterTitulos[7] = new MyAdapterTitulos("Estratégia");
                myAdapter[7] = new MyAdapter(jogosXboxEstretegia);

                fragmentA.getMAdapterOfRecyclers().notifyDataSetChanged();
                fragmentA.exibirProgress(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERRO", String.valueOf(databaseError.getCode()));
            }


        });
    }

    @Override
    public MyAdapterOfRecyclers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_recycler, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position == 0){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[0]);
        }

        if(position == 1){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[0]);
        }

        if(position == 2){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[1]);
        }
        if(position == 3){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[1]);
        }
        if(position == 4){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[2]);
        }
        if(position == 5){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[2]);
        }
        if(position == 6){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[3]);
        }
        if(position == 7){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[3]);
        }
        if(position == 8){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[4]);
        }
        if(position == 9){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[4]);
        }
        if(position == 10){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[5]);
        }
        if(position == 11){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[5]);
        }
        if(position == 12){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[6]);
        }
        if(position == 13){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[6]);
        }
        if(position == 14){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapterTitulos[7]);
        }
        if(position == 15){
            holder.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(fragmentA.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            holder.recyclerView.setAdapter(myAdapter[7]);
        }

    }

    @Override
    public int getItemCount() {
        return 16;
    }

}