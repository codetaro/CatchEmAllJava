package com.terrapages.catchemalljava;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Catch 'em All");
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tablayout);
        TabLayout.Tab mapTab = tabLayout.newTab();
        mapTab.setText("Map");
        tabLayout.addTab(mapTab);
        TabLayout.Tab pokemonTab = tabLayout.newTab();
        pokemonTab.setText("Pokemon");
        tabLayout.addTab(pokemonTab);

        viewPager = findViewById(R.id.viewpager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
