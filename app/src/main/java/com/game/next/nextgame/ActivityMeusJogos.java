package com.game.next.nextgame;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.next.nextgame.adapters.MyAdapterMeusJogos;
import com.game.next.nextgame.entidades.Jogo;
import com.game.next.nextgame.entidades.User;
import com.game.next.nextgame.entidades.UserGame;
import com.game.next.nextgame.fragments.FragmentCarteira;
import com.game.next.nextgame.fragments.FragmentMeusJogos;
import com.game.next.nextgame.fragments.ProfileFragment;
import com.game.next.nextgame.fragments.UsersFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMeusJogos extends AppCompatActivity {

    private DatabaseReference referenceUser;
    private FirebaseUser user;

    private CircleImageView profile_image;
    private TextView username;

    private Toolbar toolbar;

    private FragmentMeusJogos fragmentMeusJogos;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_jogos);

        getWindow().setStatusBarColor(Color.parseColor("#FFA900"));

        toolbar = findViewById(R.id.toolbar_meus_jogos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tabLayout = findViewById(R.id.tab_layout_meus_jogos);
        viewPager = findViewById(R.id.view_pager_meus_jogos);
        profile_image = findViewById(R.id.profile_image_meus_jogos);
        username = findViewById(R.id.username_meus_jogos);

        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUser = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (!user.getImageURL().equals("default")){
                    //change this
                    if( getApplicationContext() != null && ActivityMeusJogos.this != null) {
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }
                } else {

                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fragmentMeusJogos = new FragmentMeusJogos();

        ActivityMeusJogos.ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(fragmentMeusJogos, "Meus Jogos");
        viewPagerAdapter.addFragment(new FragmentCarteira(), "Carteira");
        viewPagerAdapter.addFragment(new ProfileFragment(), "Perfil");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                // change this code beacuse your app will crash
                Intent intent = new Intent(ActivityMeusJogos.this, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.putExtra("EXIT", true);
                finishAffinity();
                startActivity(intent);
                //Intent broadcastIntent = new Intent();
                //broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                //sendBroadcast(broadcastIntent);
                //sair();
                killApplication("com.game.next.nextgame");
                return true;
        }

        return false;
    }

    public void sair(){
        //System.runFinalizersOnExit(true);
        //super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Fragment fragment = fragmentMeusJogos;
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void killApplication(String killPackage) {//com.game.next.nextgame
        ActivityManager am =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        android.os.Process.killProcess(android.os.Process.myPid());
        am.killBackgroundProcesses(killPackage);
    }
}
