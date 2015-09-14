package com.example.adityadev.masteruimapping;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.adityadev.masteruimapping.artistsmodel.Artist;
import com.example.adityadev.masteruimapping.toptracks.Tracks;

import java.util.ArrayList;
import java.util.List;


/**
 * An activity representing a list of Artists. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TopTracksActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MainArtistActivityFragment} and the item details
 * (if present) is a {@link TopTracksActivityFragment}.
 * <p/>
 */
public class MainArtistActivity extends AppCompatActivity
        implements MainArtistActivityFragment.ArtistCallbacksInterface,
        TopTracksActivityFragment.TopTracksCallbacksInterface,
        MediaPlayerCallbacksInterface {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    protected boolean mTwoPane;
    protected static Bundle arguments;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MediaPlayerDialog mediaPlayerDialog;
    private BroadcastReceiver broadcastReceiver;
    private String externalURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_artist);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.ACTION_NOW_PLAYING));
        intentFilter.addAction(getString(R.string.MISC_ACTION));

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(getString(R.string.ACTION_NOW_PLAYING))) {

                }
            }
        };

        if (null != findViewById(R.id.frame_top_tracks)) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

        }
        getSupportActionBar().setTitle(getString(R.string.app_name));

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link MainArtistActivityFragment.ArtistCallbacksInterface}
     * indicating that the item with the given ID was selected.
     */
    public void onArtistSelected(Artist selectedArtist) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail topTracksActivityFragment using a
            // topTracksActivityFragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(getString(R.string.artist_id), selectedArtist);
            TopTracksActivityFragment topTracksActivityFragment = new TopTracksActivityFragment();
            topTracksActivityFragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_top_tracks, topTracksActivityFragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent topTracksIntent = new Intent(this, TopTracksActivity.class);
            topTracksIntent.putExtra(getString(R.string.artist_id), selectedArtist);
            startActivity(topTracksIntent);
        }
    }

    @Override
    public void onTopTrackSelected(List<Tracks> listOfTracksForPlayer, int selectedTrackPosition) {
        if (mTwoPane) {
            //To avoid implementing the interface from Media Service, create a method within Media Player Dialog
            mediaPlayerDialog.setMediaPlayerDialogObj(listOfTracksForPlayer, selectedTrackPosition);
            mediaPlayerDialog.show(fragmentManager, getString(R.string.fragment_media_player));

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent mediaPlayerIntent = new Intent(this, MediaPlayerActivity.class);
            mediaPlayerIntent.putParcelableArrayListExtra(getString(R.string.track_list_key), (ArrayList<? extends Parcelable>) listOfTracksForPlayer);
            mediaPlayerIntent.putExtra(getString(R.string.track_position_key), selectedTrackPosition);
            startActivity(mediaPlayerIntent);
        }
    }

    @Override
    public void onSelectedTrackStarted(String externalURL) {
        this.externalURL = externalURL;
    }

    @Override
    public void onSelectedTrackCompleted() {
        mediaPlayerDialog.dismiss();
        MediaService.unsetTrackEventListenerInterface();

        this.externalURL = "";
    }

    private Intent createPreviewShareIntent() {
        Intent previewShareIntent = new Intent(Intent.ACTION_SEND);
        previewShareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        previewShareIntent.setType(getString(R.string.text_plain));
        previewShareIntent.putExtra(Intent.EXTRA_TEXT, externalURL);
        Log.i("External Spotify Link: ", externalURL);
        return previewShareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_media, menu);
        MenuItem shareMenuItem = menu.findItem(R.id.action_share);
        MenuItem nowPlayingMenuItem = menu.findItem(R.id.action_now_playing);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider((shareMenuItem));
        if (null != mShareActionProvider) {
            mShareActionProvider.setShareIntent(createPreviewShareIntent());
        }
        if (isServiceRunning()) {
            shareMenuItem.setVisible(false);
            nowPlayingMenuItem.setVisible(false);
        }
        return true;
    }

    public boolean isServiceRunning() {
        List<ActivityManager.RunningServiceInfo> currentServiceList = ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo currentService : currentServiceList) {
            if (currentService.service.getClassName().equals(MediaService.class.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                Intent settingsActivityIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsActivityIntent);
                return true;


            case R.id.action_now_playing:
                if (isServiceRunning()) {
                    if (mTwoPane) {
                        if (null != mediaPlayerDialog)
                            mediaPlayerDialog.show(fragmentManager, getString(R.string.fragment_media_player));
                    } else {
                        Intent mediaPlayerIntent = new Intent(this, MediaPlayer.class);
                        startActivity(mediaPlayerIntent);
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
