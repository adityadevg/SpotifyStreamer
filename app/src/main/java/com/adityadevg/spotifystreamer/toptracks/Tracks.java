package com.adityadevg.spotifystreamer.toptracks;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adityadev.
 */
public class Tracks implements Parcelable {
    String albumImageURL;
    String albumName;

    protected Tracks(Parcel in) {
        albumImageURL = in.readString();
        albumName = in.readString();
        trackName = in.readString();
        artistName = in.readString();
        previewUrl = in.readString();
    }

    public static final Creator<Tracks> CREATOR = new Creator<Tracks>() {
        @Override
        public Tracks createFromParcel(Parcel in) {
            return new Tracks(in);
        }

        @Override
        public Tracks[] newArray(int size) {
            return new Tracks[size];
        }
    };

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    String trackName;

    public String getArtistName() {
        return artistName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    String artistName;
    String previewUrl;

    public Tracks(String albumName, String albumImageURL, String trackName, String artistName, String previewUrl) {
        this.albumName = albumName;
        this.albumImageURL = albumImageURL;
        this.trackName = trackName;
        this.artistName = artistName;
        this.previewUrl = previewUrl;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumImageURL() {
        return albumImageURL;
    }

    public void setAlbumImageURL(String albumImageURL) {
        this.albumImageURL = albumImageURL;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String artistName) {
        this.albumName = artistName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumImageURL);
        dest.writeString(albumName);
        dest.writeString(trackName);
        dest.writeString(artistName);
        dest.writeString(previewUrl);
    }
}