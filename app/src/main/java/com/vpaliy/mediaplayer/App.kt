package com.vpaliy.mediaplayer

import android.app.Application
import com.vpaliy.mediaplayer.di.*
import org.koin.android.ext.android.startKoin

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    instance = this
    startKoin(this, listOf(network, presenters,
        dataProviders, mediaPlayer, mappers, general))
  }

  companion object {
    private var instance: App? = null
  }
}