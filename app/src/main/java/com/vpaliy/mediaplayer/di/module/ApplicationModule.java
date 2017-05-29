package com.vpaliy.mediaplayer.di.module;


import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(@NonNull Context context){
        this.context=context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }
}
