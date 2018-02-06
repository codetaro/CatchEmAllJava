package com.terrapages.catchemalljava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;

public class RetrievePokemonAsyncTask extends AsyncTask<Void, Void, ArrayList<Pokemon>> {

    private Context mContext;

    ProgressDialog progressDialog;

    private Location mCurrentLocation;
    private GoogleMap mGoogleMap;

    public RetrievePokemonAsyncTask(Context context, Location location, GoogleMap googleMap) {
        mContext = context;
        mCurrentLocation = location;
        mGoogleMap = googleMap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Retrieving pokemon...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected ArrayList<Pokemon> doInBackground(Void... voids) {
        PokeApi pokeApi = new PokeApiClient();
        Pokemon pokemon;

        ArrayList<Pokemon> pokemonArrayList = new ArrayList<Pokemon>();
        int id;
        Random generator = new Random();
        for (int i = 0; i < 8; i++) {
            // [1, 802]
            id = generator.nextInt(802) + 1;
            pokemon = pokeApi.getPokemon(id);
            pokemonArrayList.add(pokemon);
        }

        return pokemonArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Pokemon> pokemons) {
        progressDialog.dismiss();

        // Populate the map with pokemon
        double lat;
        double lng;
        Marker pMarker;
        Random generator = new Random();
        for (Pokemon p : pokemons) {
            lat = generator.nextDouble() / 300;
            lng = generator.nextDouble() / 300;
            if (generator.nextBoolean()) {
                lat = -lat;
            }
            if (generator.nextBoolean()) {
                lng = -lng;
            }
            pMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mCurrentLocation.getLatitude() + lat, mCurrentLocation.getLongitude() + lng))
                    .title(p.getName()));
            pMarker.setTag(p);
        }
    }
}
