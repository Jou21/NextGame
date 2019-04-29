package com.game.next.nextgame;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.game.next.nextgame.adapters.MyFragmentPagerAdapter;
import com.game.next.nextgame.entidades.Carteira;
import com.game.next.nextgame.entidades.LocationData;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.game.next.nextgame.entidades.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private LinearLayout tabXbox;
    private LinearLayout item1;
    private LinearLayout tabPS4;
    private LinearLayout item2;

    private String contents;

    private TransacaoUser transacaoUser;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private FirebaseUser user;
    private DatabaseReference referenceTransacaoUser, reference, referenceCarteiraUser, referenceTransacoes;

    private boolean currentLocationExiste = false;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private LatLng localDeEncontro;

    private LatLng currentLocationLatLong;

    private boolean pegouTransacoes = false;
    private boolean pegouTransacoesProximas = false;

    private int contador = 0;
    private int contador2 = 0;

    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> keys2 = new ArrayList<>();
    private ArrayList<TransacaoUser> transacaoUsers = new ArrayList<>();
    private ArrayList<TransacaoUser> transacaoUsersProximas = new ArrayList<>();
    private ArrayList<TransacaoUser> transacaoUsersProximasFornecedorReceberDeVoltaOJogo = new ArrayList<>();
    private ArrayList<TransacaoUser> todasAsTransacoes = new ArrayList<>();
    private HashMap<String,String> hashMap = new HashMap<>();
    private HashMap<String,String> hashMap2 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),getResources().getStringArray(R.array.titles_tab));

        mViewPager.setAdapter(myFragmentPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        Window window = getWindow();

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        referenceTransacoes = FirebaseDatabase.getInstance().getReference("Transacoes");
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());
        referenceCarteiraUser = FirebaseDatabase.getInstance().getReference("CarteiraUsers");

        tabXbox = (LinearLayout) mTabLayout.getChildAt(0);
        item1 = (LinearLayout) tabXbox.getChildAt(0);
        item1.setBackgroundColor(Color.parseColor("#28DE00"));

        tabPS4 = (LinearLayout) mTabLayout.getChildAt(0);
        item2 = (LinearLayout) tabPS4.getChildAt(1);
        item2.setBackgroundColor(Color.parseColor("#0065DE"));

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        startGettingLocations();

        referenceTransacoes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    for(DataSnapshot snapShot2 : snapShot.getChildren()) {

                    if (snapShot2.exists()) {
                        final TransacaoUser transacaoUser = snapShot2.getValue(TransacaoUser.class);

                        if (transacaoUser.getStatus() != null) {
                            if (transacaoUser.getFornecedorId().equals(user.getUid())) {

                                final String key3 = snapShot2.getKey();

                                if (transacaoUser.getStatus().equals("ENTREGADO")) {

                                    Log.d("SNAP3", "" + transacaoUser.getStatus());

                                    String saldoParaAddCarteiraCaucao = transacaoUser.getValorCaucao();

                                    String array[] = transacaoUser.getTime().split("-");

                                    String horario = array[0];
                                    String data = array[1];

                                    try {
                                        Calendar rightNow = Calendar.getInstance();
                                        TimeZone tz = TimeZone.getTimeZone("GMT-3:00");
                                        rightNow.setTimeZone(tz);

                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                        df.setLenient(false);
                                        Date dt = df.parse(data);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dt);

                                        rightNow.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_MONTH));

                                        int diasQueOUsuarioFicouComOJogo = rightNow.get(Calendar.DAY_OF_MONTH);


                                        if (diasQueOUsuarioFicouComOJogo > 13) {

                                            final Double saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraCaucao);

                                            referenceCarteiraUser.child(transacaoUser.getFornecedorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                        String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) + saldoTotalParaAddCarteira);

                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacaoUser.getFornecedorId());
                                                        hashMap.put("saldo", saldoTotal);

                                                        referenceCarteiraUser.child(transacaoUser.getFornecedorId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if (array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if (array[1].length() == 1) {
                                                                centavos = centavos.concat("0");
                                                            }

                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");

                                                        } else {
                                                            valorInteiro = array[0];
                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    } else {
                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacaoUser.getFornecedorId());
                                                        hashMap.put("saldo", String.valueOf(saldoTotalParaAddCarteira));

                                                        referenceCarteiraUser.child(transacaoUser.getFornecedorId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if (array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if (array[1].length() == 1) {
                                                                centavos = centavos.concat("0");
                                                            }

                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");

                                                        } else {
                                                            valorInteiro = array[0];
                                                            //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            referenceCarteiraUser.child(transacaoUser.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                        String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) - saldoTotalParaAddCarteira);

                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacaoUser.getUserId());
                                                        hashMap.put("saldo", saldoTotal);

                                                        referenceCarteiraUser.child(transacaoUser.getUserId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if (array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if (array[1].length() == 1) {
                                                                centavos = centavos.concat("0");
                                                            }

                                                            //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ","+ centavos +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");

                                                        } else {
                                                            valorInteiro = array[0];
                                                            //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    } else {

                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("id", transacaoUser.getUserId());
                                                        hashMap.put("saldo", String.valueOf(-saldoTotalParaAddCarteira));

                                                        referenceCarteiraUser.child(transacaoUser.getUserId()).setValue(hashMap);

                                                        String valorInteiro, centavos;

                                                        String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                        String array[] = saldoDebitado.split("\\.");

                                                        if (array.length > 1) {
                                                            valorInteiro = array[0];
                                                            centavos = array[1];

                                                            if (array[1].length() == 1) {
                                                                centavos = centavos.concat("0");
                                                            }

                                                            //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ","+ centavos +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");

                                                        } else {
                                                            valorInteiro = array[0];
                                                            //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                            referenceTransacaoUser.child(key3).child("status").setValue("CONCLUIDO");
                                                        }

                                                        //entrou = true;

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }


                                    } catch (ParseException e) {
                                        //Toast.makeText(MainActivity.this, "Ocorreu algum erro... Caso o status mudou para CONCLUIDO, desconsidere essa mensagem!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (myFragmentPagerAdapter.getItem(0) != null) {
            myFragmentPagerAdapter.getItem(0).onActivityResult(requestCode, resultCode, intent);
        }

        if (myFragmentPagerAdapter.getItem(1) != null) {
            myFragmentPagerAdapter.getItem(1).onActivityResult(requestCode, resultCode, intent);
        }

        if (scanningResult != null) {
            if (scanningResult.getContents() != null) {
                if (intent.hasExtra("SCAN_RESULT")) {
                    contents = intent.getStringExtra("SCAN_RESULT");
                    //Toast.makeText(MainActivity.this,"" + contents,Toast.LENGTH_LONG).show();

                    transacaoUsers = new ArrayList<>();

                    todasAsTransacoes = new ArrayList<>();

                    keys = new ArrayList<>();

                    hashMap = new HashMap<>();

                    hashMap2 = new HashMap<>();

                    pegouTransacoes = false;

                    reference.child("location").child(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String currentUserlatitude = dataSnapshot.child("latitude").getValue(String.class);
                                    String currentUserlongitude = dataSnapshot.child("longitude").getValue(String.class);

                                    if( currentUserlatitude != null && currentUserlongitude != null) {

                                        final LatLng posicaoUsuario = new LatLng(Double.parseDouble(currentUserlatitude), Double.parseDouble(currentUserlongitude));

                                        referenceTransacaoUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                                    transacaoUser = snapshot1.getValue(TransacaoUser.class);
                                                    String key = snapshot1.getKey();

                                                    transacaoUsers.add(transacaoUser);
                                                    keys.add(key);
                                                    hashMap.put(transacaoUser.getUserId(), key);
                                                }

                                                if(dataSnapshot1.exists()){
                                                    referenceTransacoes.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                                                for (DataSnapshot snapShot2 : snapShot.getChildren()) {
                                                                    final TransacaoUser transacaoUser = snapShot2.getValue(TransacaoUser.class);
                                                                    String key4 = snapShot2.getKey();

                                                                    todasAsTransacoes.add(transacaoUser);
                                                                    keys2.add(key4);
                                                                    hashMap2.put(transacaoUser.getUserId(), key4);
                                                                }
                                                            }

                                                            executaTransacoes(posicaoUsuario);

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }else {
                                                    referenceTransacoes.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                                                for (DataSnapshot snapShot2 : snapShot.getChildren()) {
                                                                    final TransacaoUser transacaoUser = snapShot2.getValue(TransacaoUser.class);
                                                                    String key4 = snapShot2.getKey();

                                                                    todasAsTransacoes.add(transacaoUser);
                                                                    keys2.add(key4);
                                                                    hashMap2.put(transacaoUser.getUserId(), key4);
                                                                }
                                                            }

                                                            executaTransacoes(posicaoUsuario);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                } else {
                    Toast.makeText(MainActivity.this, "Activity cannot find  extras " + "SCAN_RESULT", Toast.LENGTH_SHORT).show();
                    Log.d("EXTRASJOGO", "Activity cannot find  extras " + "SCAN_RESULT");
                }
            }
        }

    }


    private void executaTransacoes(final LatLng posicaoUsuario){

        //Log.d("ENTROUFUNCIONARIO3", "" + transacaoUsers.get(0).getFornecedorId());

        pegouTransacoes = true;
        //Log.d("ENTROUPROXIMAS0", "ENTROUPROXIMAS0");
        //Toast.makeText(MainActivity.this, "ENTROUPROXIMAS0", Toast.LENGTH_SHORT).show();


        if (pegouTransacoes == true) {

            contador = 0;
            contador2 = 0;

            transacaoUsersProximas = new ArrayList<>();
            transacaoUsersProximasFornecedorReceberDeVoltaOJogo = new ArrayList<>();


            for (final TransacaoUser transacaoUser1 : todasAsTransacoes) {

                Log.d("ENTROUPROXIMAS0", "ENTROUPROXIMAS0");

                if (transacaoUser1.getFornecedorId() != null && transacaoUser1.getUserId() != null) {

                    Log.d("ENTROUPROXIMAS0", transacaoUser1.getFornecedorId());
                    Log.d("ENTROUPROXIMAS0", transacaoUser1.getUserId());
                    Log.d("ENTROUPROXIMAS0", user.getUid());

                    if(transacaoUser1.getFornecedorId().equals(user.getUid())) {

                        Log.d("ENTROUPROXIMAS0", "ENTROUPROXIMAS0");

                        reference.child("location").child(transacaoUser1.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String latitudeUsuarioQuePegouOJogo = dataSnapshot.child("latitude").getValue(String.class);
                                String longitudeUsuarioQuePegouOJogo = dataSnapshot.child("longitude").getValue(String.class);

                                if (latitudeUsuarioQuePegouOJogo != null && longitudeUsuarioQuePegouOJogo != null) {

                                    LatLng posicaoUsuarioQuePegouOJogo = new LatLng(Double.parseDouble(latitudeUsuarioQuePegouOJogo), Double.parseDouble(longitudeUsuarioQuePegouOJogo));

                                    double distance = SphericalUtil.computeDistanceBetween(posicaoUsuario, posicaoUsuarioQuePegouOJogo);

                                    if (transacaoUser1.getUserId() != user.getUid() && distance < 100) {

                                        transacaoUsersProximasFornecedorReceberDeVoltaOJogo.add(transacaoUser1);

                                    }else {



                                    }

                                }


                                contador2++;


                                if (contador2 == todasAsTransacoes.size()) {

                                    //=========FORNECEDOR PEGAR DE VOLTA O JOGO============================================================================



                                    if (transacaoUsersProximasFornecedorReceberDeVoltaOJogo.size() > 1) {

                                        //gerar lista para o usuario para ele escolher a transação correta
                                        Toast.makeText(MainActivity.this, "Tem mais de um usuário próximo que iniciou uma transação com você. Por favor fique próximo do usuário em que você " +
                                                "deseja realizar a transação e a pelo menos 100 metros de distância dos outros", Toast.LENGTH_LONG).show();

                                    } else if (transacaoUsersProximasFornecedorReceberDeVoltaOJogo.size() == 1) {

                                        if (transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getStatus().equals("ENTREGADO") && user.getUid().equals(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getFornecedorId())) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                            builder.setMessage("Você acaba de receber de volta o jogo " + transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getJogo().getNome() + ". Obrigado por entreter as pessoas que utilizam o NextGame!").setTitle("PARABÉNS!!!");
                                            builder.setPositiveButton("Ok, Valeu!", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                }
                                            });

                                            final String key = (String) hashMap2.get(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId());

                                            Log.d("FUNFOU3", key);

                                            AlertDialog dialog = builder.create();

                                            //entrou = false;


                                            String saldoParaAddCarteiraAluguel = transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getValorAluguel();

                                            String saldoParaAddCarteiraCaucao = transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getValorCaucao();

                                            Double caucaoPuroSemOAluguel = Double.parseDouble(saldoParaAddCarteiraCaucao) - Double.parseDouble(saldoParaAddCarteiraAluguel);

                                            Double parcelaCaucaoPuroSemOAluguel = caucaoPuroSemOAluguel / 7;

                                            Double parcelaAluguel = (Double.parseDouble(saldoParaAddCarteiraAluguel)) / 7;

                                            Double saldoTotalParaAddCarteira = 0.0;

                                            String array[] = transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getTime().split("-");

                                            String horario = array[0];
                                            String data = array[1];

                                            try {

                                                Calendar rightNow = Calendar.getInstance();
                                                TimeZone tz = TimeZone.getTimeZone("GMT-3:00");
                                                rightNow.setTimeZone(tz);

                                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                                df.setLenient (false);
                                                Date dt = df.parse(data);
                                                Calendar cal = Calendar.getInstance();
                                                cal.setTime(dt);

                                                rightNow.add(Calendar.DATE, - cal.get(Calendar.DAY_OF_MONTH));

                                                int diasQueOUsuarioFicouComOJogo = rightNow.get(Calendar.DAY_OF_MONTH);

                                                if(diasQueOUsuarioFicouComOJogo == 0){

                                                }else if(diasQueOUsuarioFicouComOJogo == 1){
                                                    saldoTotalParaAddCarteira = parcelaAluguel ;
                                                }else if(diasQueOUsuarioFicouComOJogo == 2){
                                                    saldoTotalParaAddCarteira = parcelaAluguel * 2 ;
                                                }else if(diasQueOUsuarioFicouComOJogo == 3){
                                                    saldoTotalParaAddCarteira = parcelaAluguel * 3 ;
                                                }else if(diasQueOUsuarioFicouComOJogo == 4){
                                                    saldoTotalParaAddCarteira = parcelaAluguel * 4 ;
                                                }else if(diasQueOUsuarioFicouComOJogo == 5){
                                                    saldoTotalParaAddCarteira = parcelaAluguel * 5 ;
                                                }else if(diasQueOUsuarioFicouComOJogo == 6){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel);

                                                }else if(diasQueOUsuarioFicouComOJogo == 7){ //começa a descontar do caução por + 7 dias
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + parcelaCaucaoPuroSemOAluguel;
                                                }else if(diasQueOUsuarioFicouComOJogo == 8){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + parcelaCaucaoPuroSemOAluguel * 2;
                                                }else if(diasQueOUsuarioFicouComOJogo == 9){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + parcelaCaucaoPuroSemOAluguel * 3;
                                                }else if(diasQueOUsuarioFicouComOJogo == 10){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + parcelaCaucaoPuroSemOAluguel * 4;
                                                }else if(diasQueOUsuarioFicouComOJogo == 11){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + parcelaCaucaoPuroSemOAluguel * 5;
                                                }else if(diasQueOUsuarioFicouComOJogo == 12){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + parcelaCaucaoPuroSemOAluguel * 6;
                                                }else if(diasQueOUsuarioFicouComOJogo == 13){
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + caucaoPuroSemOAluguel;
                                                }else {
                                                    // Não precisa mais devolver o jogo
                                                    saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraAluguel) + caucaoPuroSemOAluguel;
                                                }

                                                final Double saldoParaAddCarteiraDescontadoOsDias = saldoTotalParaAddCarteira;

                                                referenceCarteiraUser.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getFornecedorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                            String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) + saldoParaAddCarteiraDescontadoOsDias);

                                                            HashMap<String, String> hashMap = new HashMap<>();
                                                            hashMap.put("id", transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getFornecedorId());
                                                            hashMap.put("saldo", saldoTotal);

                                                            referenceCarteiraUser.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getFornecedorId()).setValue(hashMap);

                                                            String valorInteiro, centavos;

                                                            String saldoDebitado = String.valueOf(saldoParaAddCarteiraDescontadoOsDias);

                                                            String array[] = saldoDebitado.split("\\.");

                                                            if(array.length > 1) {
                                                                valorInteiro = array[0];
                                                                centavos = array[1];

                                                                if(array[1].length() == 1){
                                                                    centavos = centavos.concat("0");
                                                                }

                                                                Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");

                                                            }else {
                                                                valorInteiro = array[0];
                                                                Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");
                                                            }

                                                            //entrou = true;

                                                        }else{
                                                            HashMap<String, String> hashMap = new HashMap<>();
                                                            hashMap.put("id", transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getFornecedorId());
                                                            hashMap.put("saldo", String.valueOf(saldoParaAddCarteiraDescontadoOsDias));

                                                            referenceCarteiraUser.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getFornecedorId()).setValue(hashMap);

                                                            String valorInteiro, centavos;

                                                            String saldoDebitado = String.valueOf(saldoParaAddCarteiraDescontadoOsDias);

                                                            String array[] = saldoDebitado.split("\\.");

                                                            if(array.length > 1) {
                                                                valorInteiro = array[0];
                                                                centavos = array[1];

                                                                if(array[1].length() == 1){
                                                                    centavos = centavos.concat("0");
                                                                }

                                                                Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");

                                                            }else {
                                                                valorInteiro = array[0];
                                                                Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");
                                                            }

                                                            //entrou = true;

                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                referenceCarteiraUser.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                            String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) - saldoParaAddCarteiraDescontadoOsDias);

                                                            HashMap<String, String> hashMap = new HashMap<>();
                                                            hashMap.put("id", transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId());
                                                            hashMap.put("saldo", saldoTotal);

                                                            referenceCarteiraUser.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).setValue(hashMap);

                                                            String valorInteiro, centavos;

                                                            String saldoDebitado = String.valueOf(saldoParaAddCarteiraDescontadoOsDias);

                                                            String array[] = saldoDebitado.split("\\.");

                                                            if(array.length > 1) {
                                                                valorInteiro = array[0];
                                                                centavos = array[1];

                                                                if(array[1].length() == 1){
                                                                    centavos = centavos.concat("0");
                                                                }

                                                                //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ","+ centavos +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");

                                                            }else {
                                                                valorInteiro = array[0];
                                                                //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");
                                                            }

                                                            //entrou = true;

                                                        }else{

                                                            HashMap<String, String> hashMap = new HashMap<>();
                                                            hashMap.put("id", transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId());
                                                            hashMap.put("saldo", String.valueOf(-saldoParaAddCarteiraDescontadoOsDias));

                                                            referenceCarteiraUser.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).setValue(hashMap);

                                                            String valorInteiro, centavos;

                                                            String saldoDebitado = String.valueOf(saldoParaAddCarteiraDescontadoOsDias);

                                                            String array[] = saldoDebitado.split("\\.");

                                                            if(array.length > 1) {
                                                                valorInteiro = array[0];
                                                                centavos = array[1];

                                                                if(array[1].length() == 1){
                                                                    centavos = centavos.concat("0");
                                                                }

                                                                //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ","+ centavos +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");

                                                            }else {
                                                                valorInteiro = array[0];
                                                                //Toast.makeText(MainActivity.this, "Obrigado por devolver o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                referenceTransacoes.child(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getUserId()).child(key).child("status").setValue("CONCLUIDO");
                                                            }

                                                            //entrou = true;

                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                dialog.show();

                                            } catch (ParseException e) {
                                                Toast.makeText(MainActivity.this, "Ocorreu algum erro... Caso o status mudou para CONCLUIDO, desconsidere essa mensagem!", Toast.LENGTH_LONG).show();
                                            }

                                        } else if(transacaoUsersProximasFornecedorReceberDeVoltaOJogo.get(0).getStatus().equals("INICIO")){
                                            Toast.makeText(MainActivity.this, "O usuário ainda não pegou o seu jogo...", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "O jogo já está entregue!", Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        Toast.makeText(MainActivity.this, "É necessário estar próximo do usuário que deseja seu jogo...", Toast.LENGTH_LONG).show();
                                    }

                                    //=====================================================================================================================


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }








            for (final TransacaoUser transacaoUser : transacaoUsers) {

                Log.d("ENTROUFUNCIONARIO3", "ENTROUFUNCIONARIO3");

                if (transacaoUser.getFornecedorId() != null) {

                    reference.child("location").child(transacaoUser.getFornecedorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Log.d("ENTROUPROXIMAS1", "ENTROUPROXIMAS1");


                            String latitudeFonecedor = dataSnapshot.child("latitude").getValue(String.class);
                            String longitudeFonecedor = dataSnapshot.child("longitude").getValue(String.class);

                            if (latitudeFonecedor != null && longitudeFonecedor != null) {

                                LatLng posicaoFonecedor = new LatLng(Double.parseDouble(latitudeFonecedor), Double.parseDouble(longitudeFonecedor));

                                double distance = SphericalUtil.computeDistanceBetween(posicaoUsuario, posicaoFonecedor);

                                if (transacaoUser.getFornecedorId() != user.getUid() && distance < 100) {

                                    transacaoUsersProximas.add(transacaoUser);

                                }else {



                                }
                                Log.d("ENTROUFUNCIONARIO3", "ENTROUFUNCIONARIO3");
                            }

                            contador++;


                            if (contador == transacaoUsers.size()) {

                                //=======USUÁRIO PEGAR O JOGO===================================================================================

                                if (transacaoUsersProximas.size() > 1) {

                                    //gerar lista para o usuario para ele escolher a transação correta
                                    Toast.makeText(MainActivity.this, "Tem mais de um usuário próximo que iniciou uma transação com você. Por favor fique próximo do usuário em que você " +
                                            "deseja realizar a transação e a pelo menos 100 metros de distância dos outros", Toast.LENGTH_LONG).show();

                                } else if (transacaoUsersProximas.size() == 1) {

                                    referenceCarteiraUser.child(transacaoUsersProximas.get(0).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                            somatorio += Double.parseDouble(transacaoUsersProximas.get(0).getValorCaucao());

                                            if(quantoUsuarioTemNaCarteira >= somatorio){
                                                podeRealizarATransacao = true;
                                            }

                                            if (transacaoUsersProximas.get(0).getStatus().equals("INICIO") && podeRealizarATransacao) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                                builder.setMessage("Você acaba de receber o jogo " + transacaoUsersProximas.get(0).getJogo().getNome() + ". Boa diversão!!!").setTitle("PARABÉNS!!!");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                    }
                                                });

                                                String key = (String) hashMap.get(transacaoUsersProximas.get(0).getUserId());

                                                AlertDialog dialog = builder.create();
                                                if (transacaoUsersProximas.get(0).getValorAluguel().equals("N")) {

                                                    final String key2 = (String) hashMap.get(transacaoUsersProximas.get(0).getUserId());

                                                    //entrou = false;


                                                    String saldoParaAddCarteiraCaucao = transacaoUsersProximas.get(0).getValorCaucao();

                                                    String array[] = transacaoUsersProximas.get(0).getTime().split("-");

                                                    String horario = array[0];
                                                    String data = array[1];

                                                    final Double saldoTotalParaAddCarteira = Double.parseDouble(saldoParaAddCarteiraCaucao);


                                                    referenceCarteiraUser.child(transacaoUsersProximas.get(0).getFornecedorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                                String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) + saldoTotalParaAddCarteira);

                                                                Log.d("ENTROUPROXIMAS2", "Fornecedor vai ter " + saldoTotal);

                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                hashMap.put("id", transacaoUsersProximas.get(0).getFornecedorId());
                                                                hashMap.put("saldo", saldoTotal);

                                                                referenceCarteiraUser.child(transacaoUsersProximas.get(0).getFornecedorId()).setValue(hashMap);

                                                                String valorInteiro, centavos;

                                                                String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                                String array[] = saldoDebitado.split("\\.");

                                                                if(array.length > 1) {
                                                                    valorInteiro = array[0];
                                                                    centavos = array[1];

                                                                    if(array[1].length() == 1){
                                                                        centavos = centavos.concat("0");
                                                                    }

                                                                    //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                                    referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                                }else {
                                                                    valorInteiro = array[0];
                                                                    //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ",00 de saldo a sua carteira!", Toast.LENGTH_LONG).show();

                                                                    referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");
                                                                }

                                                                //entrou = true;

                                                            }else{
                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                hashMap.put("id", transacaoUsersProximas.get(0).getFornecedorId());
                                                                hashMap.put("saldo", String.valueOf(saldoTotalParaAddCarteira));

                                                                Log.d("ENTROUPROXIMAS2", "Fornecedor vai ter " + saldoTotalParaAddCarteira);

                                                                referenceCarteiraUser.child(transacaoUsersProximas.get(0).getFornecedorId()).setValue(hashMap);

                                                                String valorInteiro, centavos;

                                                                String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                                String array[] = saldoDebitado.split("\\.");

                                                                if(array.length > 1) {
                                                                    valorInteiro = array[0];
                                                                    centavos = array[1];

                                                                    if(array[1].length() == 1){
                                                                        centavos = centavos.concat("0");
                                                                    }

                                                                    //Toast.makeText(MainActivity.this, "Parabéns, foi adicionado R$ " + valorInteiro + ","+ centavos +" de saldo a sua carteira!", Toast.LENGTH_LONG).show();

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


                                                    referenceCarteiraUser.child(transacaoUsersProximas.get(0).getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.exists()){
                                                                Carteira userCarteira = dataSnapshot.getValue(Carteira.class);

                                                                String saldoTotal = String.valueOf(Double.parseDouble(userCarteira.getSaldo()) - saldoTotalParaAddCarteira);

                                                                Log.d("ENTROUPROXIMAS3", "CurrentUser vai ter " + saldoTotal);

                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                hashMap.put("id", transacaoUsersProximas.get(0).getUserId());
                                                                hashMap.put("saldo", saldoTotal);

                                                                referenceCarteiraUser.child(transacaoUsersProximas.get(0).getUserId()).setValue(hashMap);

                                                                String valorInteiro, centavos;

                                                                String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                                String array[] = saldoDebitado.split("\\.");

                                                                if(array.length > 1) {
                                                                    valorInteiro = array[0];
                                                                    centavos = array[1];

                                                                    if(array[1].length() == 1){
                                                                        centavos = centavos.concat("0");
                                                                    }

                                                                    Toast.makeText(MainActivity.this, "Obrigado por comprar o jogo. R$ " + valorInteiro + ","+ centavos +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                    referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                                }else {
                                                                    valorInteiro = array[0];
                                                                    Toast.makeText(MainActivity.this, "Obrigado por comprar o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                    referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");
                                                                }

                                                                //entrou = true;

                                                            }else{

                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                hashMap.put("id", transacaoUsersProximas.get(0).getUserId());
                                                                hashMap.put("saldo", String.valueOf(-saldoTotalParaAddCarteira));

                                                                Log.d("ENTROUPROXIMAS4", "CurrentUser vai ter menos" + saldoTotalParaAddCarteira);

                                                                referenceCarteiraUser.child(transacaoUsersProximas.get(0).getUserId()).setValue(hashMap);

                                                                String valorInteiro, centavos;

                                                                String saldoDebitado = String.valueOf(saldoTotalParaAddCarteira);

                                                                String array[] = saldoDebitado.split("\\.");

                                                                if(array.length > 1) {
                                                                    valorInteiro = array[0];
                                                                    centavos = array[1];

                                                                    if(array[1].length() == 1){
                                                                        centavos = centavos.concat("0");
                                                                    }

                                                                    Toast.makeText(MainActivity.this, "Obrigado por comprar o jogo. R$ " + valorInteiro + ","+ centavos +" foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

                                                                    referenceTransacaoUser.child(key2).child("status").setValue("CONCLUIDO");

                                                                }else {
                                                                    valorInteiro = array[0];
                                                                    Toast.makeText(MainActivity.this, "Obrigado por comprar o jogo. R$ " + valorInteiro + ",00 foi debitado da sua carteira!", Toast.LENGTH_LONG).show();

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
                                                    referenceTransacaoUser.child(key).child("status").setValue("ENTREGADO");
                                                }


                                                dialog.show();


                                            } else {
                                                if(!podeRealizarATransacao){
                                                    Toast.makeText(MainActivity.this, "Você não tem saldo suficiente!", Toast.LENGTH_LONG).show();
                                                }else {
                                                    Toast.makeText(MainActivity.this, "O jogo já está entregue!", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                } else {
                                    Toast.makeText(MainActivity.this, "É necessário estar próximo do usuário fornecedor...", Toast.LENGTH_LONG).show();
                                }

                                //=====================================================================================================================

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }

        }

    }






    //===========================ATUALIZA LOCALIZAÇÃO===============================================================================================

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desativado!");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void startGettingLocations() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();
        } else {
            // check permissions

            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }


        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            }

            if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            }


        } else {
            Toast.makeText(this, "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {

        //UPDATE
        reference.child("location").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        LocationData locationData = dataSnapshot.getValue(LocationData.class);

                        if (currentLocationExiste == true && currentLocationLatLong != null) {

                            if (!dataSnapshot.exists()) {

                                localDeEncontro = currentLocationLatLong;

                                reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
                                reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

                            } else {

                                if (locationData.getEntregaLatitude() == null) {

                                    localDeEncontro = currentLocationLatLong;

                                    reference.child("location").child(user.getUid()).child("userId").setValue(user.getUid());
                                    reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
                                    reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));
                                }else {

                                    String entregaLatitude = dataSnapshot.child("entregaLatitude").getValue(String.class);
                                    String entregaLongitude = dataSnapshot.child("entregaLongitude").getValue(String.class);

                                    if (entregaLatitude != null && entregaLongitude != null) {
                                        localDeEncontro = new LatLng(Double.parseDouble(entregaLatitude), Double.parseDouble(entregaLongitude));


                                        if ((Double.parseDouble(entregaLatitude) != currentLocationLatLong.latitude) || (Double.parseDouble(entregaLongitude) != currentLocationLatLong.longitude)) {

                                            reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
                                            reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

                                        }
                                    }

                                }


                            }

                            //============================================================================================================

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocationLatLong = new LatLng(location.getLatitude(),location.getLongitude());
        currentLocationExiste = true;

        reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(location.getLatitude()));
        reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(location.getLongitude()));

        //Add to firebase
        //if(currentLocationLatLong != null){
        //    reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
        //    reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));
        //}

        //Toast.makeText(this, "Localização atualizada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            currentLocationLatLong = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            currentLocationExiste = true;

            getLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        System.runFinalizersOnExit(true) ;
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
