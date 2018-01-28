package com.terrapages.catchemalljava;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
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

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        TabLayout.Tab mapTab = tabLayout.newTab();
        mapTab.setText("Map");
        tabLayout.addTab(mapTab);
        TabLayout.Tab pokemonTab = tabLayout.newTab();
        pokemonTab.setText("Pokemon");
        tabLayout.addTab(pokemonTab);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = mapFragment;
                        if (fragment == null) {
                            fragment = new MapFragment();
                        }
                        break;
                    case 1:
                        fragment = pokemonFragment;
                        if (fragment == null) {
                            fragment = new PokemonFragment();
                        }
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
