package com.vpaliy.mediaplayer

import android.app.Application

import com.vpaliy.mediaplayer.di.component.ApplicationComponent
import com.vpaliy.mediaplayer.di.component.DaggerApplicationComponent
import com.vpaliy.mediaplayer.di.module.ApplicationModule
import com.vpaliy.mediaplayer.di.module.DataModule
import com.vpaliy.mediaplayer.di.module.InteractorModule
import com.vpaliy.mediaplayer.di.module.NetworkModule

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initializeComponent()
    }

    private fun initializeComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .dataModule(DataModule())
                .interactorModule(InteractorModule())
                .networkModule(NetworkModule(null))
                .build()
    }
}
