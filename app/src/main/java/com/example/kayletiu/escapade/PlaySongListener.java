package com.example.kayletiu.escapade;

/**
 * Created by Kayle Tiu on 04/04/2018.
 */

public interface PlaySongListener {
    void onPlayRequested(int songIndex);
    void onSongUpdated(int songIndex);
}

