package com.vpaliy.mediaplayer

import android.app.Application
import com.vpaliy.mediaplayer.di.*
import org.koin.android.ext.android.startKoin

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin(this, listOf(general, network, presenters,
        dataProviders, mediaPlayer, mappers))
  }
}