package com.vpaliy.mediaplayer.di.module


import android.content.Context
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }
}
