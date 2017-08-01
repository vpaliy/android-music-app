package com.vpaliy.mediaplayer.di.component;


import android.content.Context;

import com.vpaliy.mediaplayer.di.module.ApplicationModule;
import com.vpaliy.mediaplayer.di.module.MediaPlayerModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    Context context();
}
