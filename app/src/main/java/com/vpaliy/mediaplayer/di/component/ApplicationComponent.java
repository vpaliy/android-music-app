package com.vpaliy.mediaplayer.di.component;


import android.content.Context;

import com.vpaliy.mediaplayer.di.module.ApplicationModule;
import com.vpaliy.mediaplayer.di.module.MediaPlayerModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, MediaPlayerModule.class})
public interface ApplicationComponent {
    Context context();

}
