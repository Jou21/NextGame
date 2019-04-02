package com.game.next.nextgame;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.game.next.nextgame.adapters.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private LinearLayout tabXbox;
    private LinearLayout item1;
    private LinearLayout tabPS4;
    private LinearLayout item2;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

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



        tabXbox = (LinearLayout) mTabLayout.getChildAt(0);
        item1 = (LinearLayout) tabXbox.getChildAt(0);
        item1.setBackgroundColor(Color.parseColor("#28DE00"));

        tabPS4 = (LinearLayout) mTabLayout.getChildAt(0);
        item2 = (LinearLayout) tabPS4.getChildAt(1);
        item2.setBackgroundColor(Color.parseColor("#0065DE"));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (myFragmentPagerAdapter.getItem(0) != null) {
            myFragmentPagerAdapter.getItem(0).onActivityResult(requestCode, resultCode, intent);
        }

        if (myFragmentPagerAdapter.getItem(1) != null) {
            myFragmentPagerAdapter.getItem(1).onActivityResult(requestCode, resultCode, intent);
        }
    }
}
