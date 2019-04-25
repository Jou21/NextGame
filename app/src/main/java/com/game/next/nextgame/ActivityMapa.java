package com.game.next.nextgame;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.LocationData;
import com.game.next.nextgame.entidades.UserGame;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityMapa extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    private ProgressBar mProgressBar;

    private Marker currentLocationMaker, markerEncontro;
    private LatLng currentLocationLatLong;
    private DatabaseReference reference;
    private FirebaseUser user;

    private boolean currentLocationExiste = false;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private LatLng localDeEncontro;

    private MarkerOptions marker;

    private Button btnMapsConfirmarLocal;

    private HashMap<String, Object> model;

    private String resultXboxOuPS4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        getWindow().setStatusBarColor(Color.parseColor("#28DE00"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarMaps);
        btnMapsConfirmarLocal = (Button) findViewById(R.id.btn_maps_confirmar_local);

        exibirProgress(true);

        Toast.makeText(this, "Estamos localizando seu atual local de entrega...", Toast.LENGTH_LONG).show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        if (getIntent().hasExtra("HASHMAP")) {
            model = (HashMap<String, Object>) getIntent().getSerializableExtra("HASHMAP");

        } else {
            //Toast.makeText(ActivityMapa.this,"Activity cannot find  extras " + "HASHMAP",Toast.LENGTH_SHORT).show();
            Log.d("EXTRASJOGO","Activity cannot find  extras " + "HASHMAP");
        }

        if (getIntent().hasExtra("resultXboxOuPS4")) {
            resultXboxOuPS4 =  getIntent().getStringExtra("resultXboxOuPS4");

        } else {
            //Toast.makeText(ActivityMapa.this,"Activity cannot find  extras " + "HASHMAP",Toast.LENGTH_SHORT).show();
            Log.d("resultXboxOuPS4","Activity cannot find  extras " + "resultXboxOuPS4");
        }


        btnMapsConfirmarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resultXboxOuPS4.equals("XBOX")) {

                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(model);
                    UserGame userGame = gson.fromJson(jsonElement, UserGame.class);

                    Jogo jogo = new Jogo();
                    jogo.setCategoria("Ação e Aventura");
                    jogo.setCodigoDeBarra("9999999999999");
                    jogo.setDataLancamento("");
                    jogo.setDescricao("Este jogo não tem descrição pois foi cadastrado pelo usuário.");
                    jogo.setFaixaEtaria("");
                    jogo.setMultiplayer("");
                    jogo.setNome(userGame.getNomeJogo());
                    jogo.setPreco("");
                    jogo.setRating("5.0");
                    jogo.setSku("");
                    jogo.setUrlImgJogo(userGame.getImgJogo());
                    jogo.setUrlVideo("");
                    jogo.setCodigoDeBarra1("");
                    jogo.setCodigoDeBarra2("");
                    jogo.setCodigoDeBarra3("");
                    jogo.setCodigoDeBarra4("");
                    jogo.setCodigoDeBarra5("");
                    jogo.setCodigoDeBarra6("");
                    jogo.setCodigoDeBarra7("");
                    jogo.setCodigoDeBarra8("");
                    jogo.setCodigoDeBarra9("");
                    jogo.setCodigoDeBarra10("");
                    jogo.setUrlImgComplementar1("");
                    jogo.setUrlImgComplementar2("");
                    jogo.setUrlImgComplementar3("");
                    jogo.setUrlImgComplementar4("");
                    jogo.setUrlImgComplementar5("");

                    reference.child("Xbox").push().setValue(jogo);
                }
                if(resultXboxOuPS4.equals("PS4")) {

                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(model);
                    UserGame userGame = gson.fromJson(jsonElement, UserGame.class);

                    Jogo jogo = new Jogo();
                    jogo.setCategoria("Ação e Aventura");
                    jogo.setCodigoDeBarra("9999999999999");
                    jogo.setDataLancamento("");
                    jogo.setDescricao("Este jogo não tem descrição pois foi cadastrado pelo usuário.");
                    jogo.setFaixaEtaria("");
                    jogo.setMultiplayer("");
                    jogo.setNome(userGame.getNomeJogo());
                    jogo.setPreco("");
                    jogo.setRating("5.0");
                    jogo.setSku("");
                    jogo.setUrlImgJogo(userGame.getImgJogo());
                    jogo.setUrlVideo("");
                    jogo.setCodigoDeBarra1("");
                    jogo.setCodigoDeBarra2("");
                    jogo.setCodigoDeBarra3("");
                    jogo.setCodigoDeBarra4("");
                    jogo.setCodigoDeBarra5("");
                    jogo.setCodigoDeBarra6("");
                    jogo.setCodigoDeBarra7("");
                    jogo.setCodigoDeBarra8("");
                    jogo.setCodigoDeBarra9("");
                    jogo.setCodigoDeBarra10("");
                    jogo.setUrlImgComplementar1("");
                    jogo.setUrlImgComplementar2("");
                    jogo.setUrlImgComplementar3("");
                    jogo.setUrlImgComplementar4("");
                    jogo.setUrlImgComplementar5("");

                    reference.child("PS4").push().setValue(jogo);
                }
                reference.child("UserGame").child(user.getUid()).push().setValue(model);
                finish();
                //finishAffinity();
                //Intent intent = new Intent(ActivityMapa.this, ActivityMeusJogos.class);
                //startActivity(intent);

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng cascavel = new LatLng(-24.952327, -53.461767);
        //mMap.addMarker(new MarkerOptions().position(cascavel).title("Cascavel"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cascavel));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
    }

    @Override
    public void onLocationChanged(Location location) {

        if (currentLocationMaker != null) {
            currentLocationMaker.remove();
        }
        //Add marker
        currentLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocationLatLong);
        markerOptions.title("Sua localização atual");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMaker = mMap.addMarker(markerOptions);

        //Move to new location
        //CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //Add to firebase

        //reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
        //reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

        //Toast.makeText(this, "Localização atualizada", Toast.LENGTH_SHORT).show();

        //currentLocationLatLong = new LatLng(location.getLatitude(),location.getLongitude());
        currentLocationExiste = true;

        reference.child("location").child(user.getUid()).child("latitude").setValue(String.valueOf(location.getLatitude()));
        reference.child("location").child(user.getUid()).child("longitude").setValue(String.valueOf(location.getLongitude()));


    }


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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
                        //String key = dataSnapshot.getKey();

                        //reference.child("location").child(key).child("userId").setValue(user.getUid());
                        //reference.child("location").child(key).child("time").setValue(String.valueOf(new Date().getTime()));
                        //reference.child("location").child(key).child("latitude").setValue(String.valueOf(currentLocationLatLong.latitude));
                        //reference.child("location").child(key).child("longitude").setValue(String.valueOf(currentLocationLatLong.longitude));

                        //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //    LocationData locationData = snapshot.getValue(LocationData.class);
                        //    Log.d("ENTROU0","ENTROU0");
                        //if(reference.child("location").child(key).child("entregaLatitude").exists()){
                        //if(locationData.getEntregaLatitude() == null){
                        if (currentLocationExiste == true) {

                            if (!dataSnapshot.exists()) {
                                Log.d("ENTROU1", "ENTROU1");
                                localDeEncontro = currentLocationLatLong;

                                if (markerEncontro != null) {
                                    markerEncontro.remove();
                                }

                                marker = new MarkerOptions().position(localDeEncontro).title("Seu local de entrega dos jogos!").snippet("Obs: Segure e arraste para mover").draggable(true);
                                markerEncontro = mMap.addMarker(marker);
                                markerEncontro.showInfoWindow();

                                exibirProgress(false);

                                if(model == null){
                                    btnMapsConfirmarLocal.setVisibility(View.GONE);
                                }else{
                                    btnMapsConfirmarLocal.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(ActivityMapa.this, "Localização atualizada", Toast.LENGTH_SHORT).show();

                                //Add to firebase
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("userId", user.getUid());
                                hashMap.put("time", String.valueOf(new Date().getTime()));
                                hashMap.put("latitude", String.valueOf(currentLocationLatLong.latitude));
                                hashMap.put("longitude", String.valueOf(currentLocationLatLong.longitude));
                                hashMap.put("entregaLatitude", String.valueOf(localDeEncontro.latitude));
                                hashMap.put("entregaLongitude", String.valueOf(localDeEncontro.longitude));

                                reference.child("location").child(user.getUid()).setValue(hashMap);

                                CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(localDeEncontro).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDrag(Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDragEnd(Marker marker) {
                                        localDeEncontro = marker.getPosition();

                                        //Add to firebase
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("userId", user.getUid());
                                        hashMap.put("time", String.valueOf(new Date().getTime()));
                                        hashMap.put("latitude", String.valueOf(currentLocationLatLong.latitude));
                                        hashMap.put("longitude", String.valueOf(currentLocationLatLong.longitude));
                                        hashMap.put("entregaLatitude", String.valueOf(localDeEncontro.latitude));
                                        hashMap.put("entregaLongitude", String.valueOf(localDeEncontro.longitude));

                                        reference.child("location").child(user.getUid()).setValue(hashMap);

                                        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(localDeEncontro).build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                });
                            } else {
                                Log.d("ENTROU2", "ENTROU2");
                                String entregaLatitude = dataSnapshot.child("entregaLatitude").getValue(String.class);
                                String entregaLongitude = dataSnapshot.child("entregaLongitude").getValue(String.class);

                                localDeEncontro = new LatLng(Double.parseDouble(entregaLatitude), Double.parseDouble(entregaLongitude));

                                if (markerEncontro != null) {
                                    markerEncontro.remove();
                                }

                                marker = new MarkerOptions().position(localDeEncontro).title("Seu local de entrega dos jogos!").snippet("Obs: Segure e arraste para mover").draggable(true);
                                markerEncontro = mMap.addMarker(marker);
                                markerEncontro.showInfoWindow();

                                exibirProgress(false);

                                if(model == null){
                                    btnMapsConfirmarLocal.setVisibility(View.GONE);
                                }else{
                                    btnMapsConfirmarLocal.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(ActivityMapa.this, "Localização atualizada", Toast.LENGTH_SHORT).show();

                                if(currentLocationLatLong != null) {
                                    if ((Double.parseDouble(entregaLatitude) != currentLocationLatLong.latitude) || (Double.parseDouble(entregaLongitude) != currentLocationLatLong.longitude)) {
                                        //Add to firebase
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("userId", user.getUid());
                                        hashMap.put("time", String.valueOf(new Date().getTime()));
                                        hashMap.put("latitude", String.valueOf(currentLocationLatLong.latitude));
                                        hashMap.put("longitude", String.valueOf(currentLocationLatLong.longitude));
                                        hashMap.put("entregaLatitude", String.valueOf(localDeEncontro.latitude));
                                        hashMap.put("entregaLongitude", String.valueOf(localDeEncontro.longitude));

                                        reference.child("location").child(user.getUid()).setValue(hashMap);
                                    }
                                }

                                CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(localDeEncontro).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDragStart(Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDrag(Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDragEnd(Marker marker) {
                                        localDeEncontro = marker.getPosition();

                                        //Add to firebase
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("userId", user.getUid());
                                        hashMap.put("time", String.valueOf(new Date().getTime()));
                                        hashMap.put("latitude", String.valueOf(currentLocationLatLong.latitude));
                                        hashMap.put("longitude", String.valueOf(currentLocationLatLong.longitude));
                                        hashMap.put("entregaLatitude", String.valueOf(localDeEncontro.latitude));
                                        hashMap.put("entregaLongitude", String.valueOf(localDeEncontro.longitude));

                                        reference.child("location").child(user.getUid()).setValue(hashMap);

                                        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(localDeEncontro).build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                });
                            }

                            //============================================================================================================


                            //}
                            //reference.child("location").child(key).child("entregaLatitude").setValue(String.valueOf(localDeEncontro.latitude));
                            //reference.child("location").child(key).child("entregaLongitude").setValue(String.valueOf(localDeEncontro.longitude));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

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

            if (currentLocationMaker != null) {
                currentLocationMaker.remove();
            }
            //Add marker
            currentLocationLatLong = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocationLatLong);
            markerOptions.title("Sua localização atual");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentLocationMaker = mMap.addMarker(markerOptions);

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void exibirProgress(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
