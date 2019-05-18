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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.next.nextgame.entidades.Chat;
import com.game.next.nextgame.entidades.User;
import com.game.next.nextgame.fragments.ChatsFragment;
import com.game.next.nextgame.fragments.FragmentTransacoes;
import com.game.next.nextgame.fragments.ProfileFragment;
import com.game.next.nextgame.fragments.UsersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityChat extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView username;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setStatusBarColor(Color.parseColor("#FFA900"));

        toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tabLayout = findViewById(R.id.tab_layout_chat);
        viewPager = findViewById(R.id.view_pager_chat);
        profile_image = findViewById(R.id.profile_image_chat);
        username = findViewById(R.id.username_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (!user.getImageURL().equals("default")){
                    //change this
                    if( getApplicationContext() != null && ActivityChat.this != null) {
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

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }

                if (unread == 0){
                    viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                } else {
                    viewPagerAdapter.addFragment(new ChatsFragment(), "("+unread+") Chats");
                }

                //viewPagerAdapter.addFragment(new UsersFragment(), "Users");
                //viewPagerAdapter.addFragment(new ProfileFragment(), "Perfil");
                viewPagerAdapter.addFragment(new FragmentTransacoes(), "Transações");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                Intent intent = new Intent(ActivityChat.this, ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finishAffinity();
                startActivity(intent);
                //sair();
                killApplication("com.game.next.nextgame");
                return true;
        }

        return false;
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

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void sair(){
        //System.runFinalizersOnExit(true) ;
        //super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void killApplication(String killPackage) {//com.game.next.nextgame
        ActivityManager am =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        android.os.Process.killProcess(android.os.Process.myPid());
        am.killBackgroundProcesses(killPackage);
    }
}
