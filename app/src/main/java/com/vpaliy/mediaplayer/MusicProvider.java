package com.vpaliy.mediaplayer;

public interface MusicProvider {
    String at(int index);
    String next();
    String prev();
    int count();
}
