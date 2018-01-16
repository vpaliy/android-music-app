package com.vpaliy.mediaplayer

import android.app.Application
import com.vpaliy.mediaplayer.di.component.ApplicationComponent
import com.vpaliy.mediaplayer.di.component.DaggerApplicationComponent
import com.vpaliy.mediaplayer.di.component.DaggerPlaybackComponent
import com.vpaliy.mediaplayer.di.component.PlaybackComponent
import com.vpaliy.mediaplayer.di.module.*

class App : Application() {
  val component: ApplicationComponent by lazy(LazyThreadSafetyMode.NONE){
    DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .dataModule(DataModule())
        .interactorModule(InteractorModule())
        .networkModule(NetworkModule(null))
        .build()
  }

  val playbackComponent: PlaybackComponent by lazy(LazyThreadSafetyMode.NONE){
    DaggerPlaybackComponent.builder()
        .applicationComponent(component)
        .mediaPlayerModule(MediaPlayerModule())
        .build()
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
  }

  companion object {
    private var instance: App? = null
    val component by lazy(LazyThreadSafetyMode.NONE) {
      instance?.component
    }

    val playbackComponent by lazy(LazyThreadSafetyMode.NONE) {
      instance?.playbackComponent
    }
  }
}