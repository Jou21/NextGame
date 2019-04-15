package com.game.next.nextgame;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.game.next.nextgame.adapters.MyFragmentPagerAdapter;
import com.game.next.nextgame.entidades.LocationData;
import com.game.next.nextgame.entidades.TransacaoUser;
import com.game.next.nextgame.entidades.User;
import com.google.android.gms.maps.model.LatLng;
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

public class MainActivity extends AppCompatActivity {

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
    private DatabaseReference referenceTransacaoUser;

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

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        if (myFragmentPagerAdapter.getItem(0) != null) {
            myFragmentPagerAdapter.getItem(0).onActivityResult(requestCode, resultCode, intent);
        }

        if (myFragmentPagerAdapter.getItem(1) != null) {
            myFragmentPagerAdapter.getItem(1).onActivityResult(requestCode, resultCode, intent);
        }

        if(scanningResult != null){
            if(scanningResult.getContents() != null) {
                if (intent.hasExtra("SCAN_RESULT")) {
                    contents = intent.getStringExtra("SCAN_RESULT");

                    referenceTransacaoUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                transacaoUser = snapshot.getValue(TransacaoUser.class);
                                String key = snapshot.getKey();

                                if(transacaoUser.getJogo().getCodigoDeBarra().equals(contents) && transacaoUser.getFornecedorId() != user.getUid() ){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                    builder.setMessage("Você acaba de receber o jogo " + transacaoUser.getJogo().getNome() + ". Boa diversão!!!").setTitle("PARABÉNS!!!");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    referenceTransacaoUser.child(key).child("status").setValue("ENTREGADO");
                                    dialog.show();


                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                } else {
                    Toast.makeText(MainActivity.this,"Activity cannot find  extras " + "SCAN_RESULT",Toast.LENGTH_SHORT).show();
                    Log.d("EXTRASJOGO","Activity cannot find  extras " + "SCAN_RESULT");
                }
            }
        }




    }

    /*
    @Override
    protected void onResume() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);


        super.onResume();
    }
    */
}
