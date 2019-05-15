package com.game.next.nextgame.adapters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.game.next.nextgame.R;
import com.game.next.nextgame.entidades.Carteira;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.game.next.nextgame.entidades.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MyAdapterListaScan extends RecyclerView.Adapter<MyAdapterListaScan.ViewHolder> {
    private ArrayList<TransacaoUser> transacoesUsers, transacaoUsers;
    private DatabaseReference referenceUsers, referenceCarteiraUser, referenceTransacaoUser;
    private HashMap<String,String> hashMap;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;
        public ImageView imgUser;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.first_line_lista_scan);
            txtFooter = (TextView) v.findViewById(R.id.second_line_lista_scan);
            imgUser = (ImageView) v.findViewById(R.id.img_row_lista_scan);
        }
    }

    public MyAdapterListaScan(ArrayList<TransacaoUser> myDataset, DatabaseReference referenceUsers, DatabaseReference referenceCarteiraUser, DatabaseReference referenceTransacaoUser, ArrayList<TransacaoUser> transacaoUsers, HashMap<String,String> hashMap) {
        this.transacoesUsers = myDataset;
        this.referenceUsers = referenceUsers;
        this.referenceCarteiraUser = referenceCarteiraUser;
        this.referenceTransacaoUser = referenceTransacaoUser;
        this.transacaoUsers = transacaoUsers;
        this.hashMap = hashMap;
    }

    @Override
    public MyAdapterListaScan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext());
        View v = inflater.inflate(R.layout.row_lista_scan, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String nomeJogo = transacoesUsers.get(position).getJogo().getNome();
        holder.txtHeader.setText("Jogo: " +nomeJogo);

        referenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    User user = postSnapshot.getValue(User.class);

                    if (user.getId().equals(transacoesUsers.get(position).getFornecedorId())) {

                        holder.txtFooter.setText("Fornecedor: " + user.getUsername());

                        Picasso.get().load(user.getImageURL()).into(holder.imgUser, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {

                            referenceCarteiraUser.child(transacoesUsers.get(position).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                    final Double quantoUsuarioTemNaCarteira = Double.parseDouble(userCarteira.getSaldo());

                                    Log.d("ENTROUPROXIMAS1", "CurrentUser tem " + quantoUsuarioTemNaCarteira);

                                    Double somatorio = 0.0;
                                    boolean podeRealizarATransacao = false;

                                    for(TransacaoUser userTransacoes : transacaoUsers){
                                        if(userTransacoes.getStatus().equals("ENTREGADO")){
                                            somatorio += Double.parseDouble(userTransacoes.getValorCaucao());
                                        }
                                    }

                                    somatorio += Double.parseDouble(transacoesUsers.get(position).getValorCaucao());

                                    if(quantoUsuarioTemNaCarteira >= somatorio){
                                        podeRealizarATransacao = true;
                                    }

                                    if (transacoesUsers.get(position).getStatus().equals("INICIO") && podeRealizarATransacao) {
                                        //AlertDialog.Builder builder = new AlertDialog.Builder(holder.layout.getContext());

                                        //builder.setMessage("Você acaba de receber o jogo " + transacoesUsers.get(position).getJogo().getNome() + ". Boa diversão!!!").setTitle("PARABÉNS!!!");
                                        //builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        //    public void onClick(DialogInterface dialog, int id) {

                                        //    }
                                        //});

                                        String key = (String) hashMap.get(transacoesUsers.get(position).getUserId());

                                        //AlertDialog dialog = builder.create();
                                        if (transacoesUsers.get(position).getValorAluguel().equals("N")) {

                                            final String key2 = (String) hashMap.get(transacoesUsers.get(position).getUserId());

                                            //entrou = false;


                                            String saldoParaAddCarteiraCaucao = transacoesUsers.get(position).getValorCaucao();

                                            String array[] = transacoesUsers.get(position).getTime().split("-");

                                            String horario = array[0];
                                            String data = array[1];

                                            final Double saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraCaucao);


                                            referenceCarteiraUser.child(transacoesUsers.get(position).getFornecedorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                        String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) + saldoTotalParaAddCarteira);

                                                        Log.d("ENTROUPROXIMAS2", "Fornecedor vai ter " + saldoTotal);

                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacoesUsers.get(position).getFornecedorId());
                                                        hashMap.put("saldo", saldoTotal);

                                                        referenceCarteiraUser.child(transacoesUsers.get(position).getFornecedorId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if(array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if(array[1].length() == 1){
                                                                centavos = centavos.concat("0");
                                                            }

                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos.substring(0,2) +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                        }else {
                                                            valorInteiro = array[0];
                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    }else{
                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacoesUsers.get(position).getFornecedorId());
                                                        hashMap.put("saldo", String.valueOf(saldoTotalParaAddCarteira));

                                                        referenceCarteiraUser.child(transacoesUsers.get(position).getFornecedorId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if(array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if(array[1].length() == 1){
                                                                centavos = centavos.concat("0");
                                                            }

                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos.substring(0,2) +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                        }else {
                                                            valorInteiro = array[0];
                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                            referenceCarteiraUser.child(transacoesUsers.get(position).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                        String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) - saldoTotalParaAddCarteira);

                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacoesUsers.get(position).getUserId());
                                                        hashMap.put("saldo", saldoTotal);

                                                        referenceCarteiraUser.child(transacoesUsers.get(position).getUserId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if(array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if(array[1].length() == 1){
                                                                centavos = centavos.concat("0");
                                                            }

                                                            Toast.makeText(holder.layout.getContext(), "Obrigado por comprar o jogo. R$ " + valorInteiro + ","+ centavos.substring(0,2) +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                        }else {
                                                            valorInteiro = array[0];
                                                            Toast.makeText(holder.layout.getContext(), "Obrigado por comprar o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    }else{

                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacoesUsers.get(position).getUserId());
                                                        hashMap.put("saldo", String.valueOf(-saldoTotalParaAddCarteira));

                                                        Log.d("ENTROUPROXIMAS4", "CurrentUser vai ter menos" + saldoTotalParaAddCarteira);

                                                        referenceCarteiraUser.child(transacoesUsers.get(position).getUserId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if(array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if(array[1].length() == 1){
                                                                centavos = centavos.concat("0");
                                                            }

                                                            Toast.makeText(holder.layout.getContext(), "Obrigado por comprar o jogo. R$ " + valorInteiro + ","+ centavos.substring(0,2) +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                        }else {
                                                            valorInteiro = array[0];
                                                            Toast.makeText(holder.layout.getContext(), "Obrigado por comprar o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        } else {

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

                                            String data = String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second) + "-" + String.valueOf(dia) + "/" + String.valueOf(mesUmAteDoze) + "/" + String.valueOf(ano);

                                            referenceTransacaoUser.child(key).child("time").setValue(data);
                                            referenceTransacaoUser.child(key).child("status").setValue("ENTREGADO");
                                        }

                                        //mostraDialog(transacoesUsers.get(position).getJogo().getNome());

                                        ((Activity)holder.layout.getContext()).finish();

                                        //dialog.show();


                                    } else {
                                        if(!podeRealizarATransacao){
                                            Toast.makeText(holder.layout.getContext(), "Você não tem saldo suficiente!", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(holder.layout.getContext(), "O jogo já está entregue!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Toast.makeText(holder.layout.getContext(), "Você está desconectado. Por favor se conecte e tente novamente", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(holder.layout.getContext(), "Listener was cancelled", Toast.LENGTH_LONG).show();
                    }
                });





                //Intent intentInfoTransacao = new Intent(holder.layout.getContext(), InfoTransacaoActivity.class);
                //intentInfoTransacao.putExtra("TRANSACAO", transacoesUsers.get(position));
                //layout.getContext().startActivity(intentInfoTransacao);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transacoesUsers.size();
    }

}