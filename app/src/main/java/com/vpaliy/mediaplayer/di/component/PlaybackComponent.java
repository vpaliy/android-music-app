package com.vpaliy.mediaplayer.di.component;

import com.vpaliy.mediaplayer.di.module.MediaPlayerModule;
import com.vpaliy.mediaplayer.domain.playback.PlaybackScope;
import com.vpaliy.mediaplayer.playback.MusicPlaybackService;

import dagger.Component;

@PlaybackScope
@Component(dependencies = ApplicationComponent.class,
    modules = MediaPlayerModule.class)
public interface PlaybackComponent {
    void inject(MusicPlaybackService service);
}
