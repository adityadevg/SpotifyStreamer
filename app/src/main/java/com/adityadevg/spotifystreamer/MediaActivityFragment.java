package com.adityadevg.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.adityadevg.spotifystreamer.toptracks.Tracks;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MediaActivityFragment extends Fragment {

    private String previewUrl;
    public List<Tracks> tracksList;
    private Tracks currentTrack;
    private int position;

    public MediaActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent baseIntent = getActivity().getIntent();
        tracksList = baseIntent.getParcelableArrayListExtra(getString(R.string.track_list_key));
        this.position = baseIntent.getIntExtra(getString(R.string.track_position_key), -1);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media, container, false);
        this.currentTrack = tracksList.get(this.position);
        this.previewUrl = currentTrack.getPreviewUrl();

        TextView albumName_tv = (TextView) rootView.findViewById(R.id.albumNameTextView);
        albumName_tv.setText(currentTrack.getAlbumName());
        TextView mediaTrackName_tv = (TextView) rootView.findViewById(R.id.mediaTrackNameTextView);
        mediaTrackName_tv.setText(currentTrack.getTrackName());
        TextView artistName_tv = (TextView) rootView.findViewById(R.id.artistNameTextView);
        artistName_tv.setText(currentTrack.getArtistName());

        ImageView albumImage_iv = (ImageView) rootView.findViewById(R.id.albumArtImageView);
        if((null != currentTrack.getAlbumImageURL()) && (!currentTrack.getAlbumImageURL().isEmpty())) {
            Picasso.with(getActivity()).load(currentTrack.getAlbumImageURL()).resize(800, 800).centerCrop().into(albumImage_iv);
        }

        ImageButton prevTrack_iv = (ImageButton) rootView.findViewById(R.id.prevTrackBtn);

        ImageButton pauseTrack_iv = (ImageButton) rootView.findViewById(R.id.pauseTrackBtn);

        ImageButton nextTrack_iv = (ImageButton) rootView.findViewById(R.id.nextTrackBtn);

        return rootView;

    }



}
