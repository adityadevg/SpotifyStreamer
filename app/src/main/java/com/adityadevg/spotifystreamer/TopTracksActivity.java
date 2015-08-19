package com.adityadevg.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;


public class TopTracksActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);
        Intent baseIntent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        String artistName = baseIntent.getStringExtra(getString(R.string.artist_name_key));
        if (null != artistName) {
            actionBar.setTitle(getString(R.string.top_tracks));
            actionBar.setSubtitle(artistName);
        }
    }
}
