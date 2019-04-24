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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private DatabaseReference referenceTransacaoUser, reference;

    private boolean currentLocationExiste = false;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private LatLng localDeEncontro;

    private LatLng currentLocationLatLong;

    private boolean pegouTransacoes = false;
    private boolean pegouTransacoesProximas = false;

    private int contador = 0;

    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<TransacaoUser> transacaoUsers = new ArrayList<>();
    private ArrayList<TransacaoUser> transacaoUsersProximas = new ArrayList<>();
    private HashMap<String,String> hashMap = new HashMap<>();

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
        referenceTransacaoUser = FirebaseDatabase.getInstance().getReference("Transacoes").child(user.getUid());

        tabXbox = (LinearLayout) mTabLayout.getChildAt(0);
        item1 = (LinearLayout) tabXbox.getChildAt(0);
        item1.setBackgroundColor(Color.parseColor("#28DE00"));

        tabPS4 = (LinearLayout) mTabLayout.getChildAt(0);
        item2 = (LinearLayout) tabPS4.getChildAt(1);
        item2.setBackgroundColor(Color.parseColor("#0065DE"));

        referenceTransacaoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //transacaoUser = snapshot.getValue(TransacaoUser.class);
                    //String key = snapshot.getKey();
                    //Toast.makeText(MainActivity.this,dataSnapshot.getKey(),Toast.LENGTH_LONG);
                Log.d("ENTROUUU1","ENTROUUU1");
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

                    keys = new ArrayList<>();

                    hashMap = new HashMap<>();

                    pegouTransacoes = false;

                    reference.child("location").child(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String currentUserlatitude = dataSnapshot.child("latitude").getValue(String.class);
                                    String currentUserlongitude = dataSnapshot.child("longitude").getValue(String.class);

                                    final LatLng posicaoUsuario = new LatLng(Double.parseDouble(currentUserlatitude), Double.parseDouble(currentUserlongitude));

                                    referenceTransacaoUser.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                transacaoUser = snapshot.getValue(TransacaoUser.class);
                                                String key = snapshot.getKey();

                                                transacaoUsers.add(transacaoUser);
                                                keys.add(key);
                                                hashMap.put(transacaoUser.getUserId(),key);
                                            }

                                            pegouTransacoes = true;
                                            //Log.d("ENTROUPROXIMAS0", "ENTROUPROXIMAS0");
                                            //Toast.makeText(MainActivity.this, "ENTROUPROXIMAS0", Toast.LENGTH_SHORT).show();


                                            if (pegouTransacoes == true) {

                                            contador = 0;

                                                for (final TransacaoUser transacaoUser : transacaoUsers) {

                                                    //Log.d("TRASACOESUSERS", "" + transacaoUser.getFornecedorId());

                                                    if(transacaoUser.getFornecedorId() != null){

                                                    reference.child("location").child(transacaoUser.getFornecedorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            //Log.d("ENTROUPROXIMAS1", "ENTROUPROXIMAS1");
                                                            //Toast.makeText(MainActivity.this, "ENTROUPROXIMAS1", Toast.LENGTH_SHORT).show();

                                                            String latitudeFonecedor = dataSnapshot.child("latitude").getValue(String.class);
                                                            String longitudeFonecedor = dataSnapshot.child("longitude").getValue(String.class);

                                                            LatLng posicaoFonecedor = new LatLng(Double.parseDouble(latitudeFonecedor), Double.parseDouble(longitudeFonecedor));

                                                            double distance = SphericalUtil.computeDistanceBetween(posicaoUsuario, posicaoFonecedor);

                                                            if (transacaoUser.getFornecedorId() != user.getUid() && distance < 3000) {

                                                                transacaoUsersProximas.add(transacaoUser);

                                                            }

                                                            contador++;

                                                            if (contador == transacaoUsers.size()) {
                                                                //Log.d("ENTROUPROXIMAS2", "ENTROUPROXIMAS2");
                                                                //Toast.makeText(MainActivity.this, "ENTROUPROXIMAS2", Toast.LENGTH_SHORT).show();
                                                                if (transacaoUsersProximas.size() > 1) {

                                                                    //gerar lista para o usuario para ele escolher a transação correta

                                                                } else if (transacaoUsersProximas.size() == 1) {

                                                                    if(transacaoUsersProximas.get(0).getStatus().equals("INICIO")) {
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                                                        builder.setMessage("Você acaba de receber o jogo " + transacaoUser.getJogo().getNome() + ". Boa diversão!!!").setTitle("PARABÉNS!!!");
                                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {

                                                                            }
                                                                        });

                                                                        String key = (String) hashMap.get(transacaoUsersProximas.get(0).getUserId());

                                                                        AlertDialog dialog = builder.create();
                                                                        referenceTransacaoUser.child(key).child("status").setValue("ENTREGADO");
                                                                        dialog.show();
                                                                    }else {
                                                                        Toast.makeText(MainActivity.this, "O jogo já está entregue!", Toast.LENGTH_LONG).show();
                                                                    }

                                                                } else {
                                                                    Toast.makeText(MainActivity.this, "É necessário estar próximo do usuário fornecedor...", Toast.LENGTH_LONG).show();
                                                                }

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

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

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

            } else if (isNetwork) {
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
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (currentLocationExiste == true) {

                            if (!dataSnapshot.exists()) {

                                localDeEncontro = currentLocationLatLong;

                                reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
                                reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

                            } else {

                                String entregaLatitude = dataSnapshot.child("entregaLatitude").getValue(String.class);
                                String entregaLongitude = dataSnapshot.child("entregaLongitude").getValue(String.class);

                                localDeEncontro = new LatLng(Double.parseDouble(entregaLatitude), Double.parseDouble(entregaLongitude));

                                if(currentLocationLatLong != null){

                                    if ((Double.parseDouble(entregaLatitude) != currentLocationLatLong.latitude) || (Double.parseDouble(entregaLongitude) != currentLocationLatLong.longitude)) {

                                        reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
                                        reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

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

        currentLocationExiste = true;

        //Add to firebase

        reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
        reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

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



}
