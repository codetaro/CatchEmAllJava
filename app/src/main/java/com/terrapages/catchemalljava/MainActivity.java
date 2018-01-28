package com.terrapages.catchemalljava;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    Fragment mapFragment = null;
    Fragment pokemonFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Catch 'em All");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        TabLayout.Tab mapTab = tabLayout.newTab();
        mapTab.setText("Map");
        tabLayout.addTab(mapTab);
        TabLayout.Tab pokemonTab = tabLayout.newTab();
        pokemonTab.setText("Pokemon");
        tabLayout.addTab(pokemonTab);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
