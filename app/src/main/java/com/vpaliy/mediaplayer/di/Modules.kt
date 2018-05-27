package com.vpaliy.mediaplayer.di

import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import com.vpaliy.mediaplayer.CLIENT_ID
import com.vpaliy.mediaplayer.data.AppPreferences
import com.vpaliy.mediaplayer.data.Filter
import com.vpaliy.mediaplayer.data.MusicRepository
import com.vpaliy.mediaplayer.data.local.MusicDatabase
import com.vpaliy.mediaplayer.data.local.TrackHandler
import com.vpaliy.mediaplayer.data.mapper.TrackMapper
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.executor.SchedulerProvider
import com.vpaliy.mediaplayer.domain.interactor.*
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.Playback
import com.vpaliy.mediaplayer.playback.MediaPlayback21
import com.vpaliy.mediaplayer.playback.MetadataMapper
import com.vpaliy.mediaplayer.playback.PlaybackManager
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.details.ActionsPresenter
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.favorite.FavoritePresenter
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.search.SearchContract
import com.vpaliy.mediaplayer.ui.search.TrackPresenter
import com.vpaliy.soundcloud.SoundCloud
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext


val general = applicationContext {
  bean { androidApplication() } bind Context::class
  bean { Navigator() }
  bean { SchedulerProvider() } bind BaseScheduler::class
}

val dataProviders = applicationContext {
  bean { ErrorHandler() }
  bean { Filter(get()) }
  bean { MusicDatabase(get()) }
  bean { AppPreferences() } bind PreferencesInteractor::class
  bean { TrackHandler(get<MusicDatabase>()) }
  bean {
    MusicRepository(get<TrackMapper>(), get(),
        get(), get(), get())
  } bind Repository::class
}

val network = applicationContext {
  bean {
    SoundCloud.Builder(get(), CLIENT_ID)
        .setToken(null).build()
        .soundCloudService
  }
}

val mappers = applicationContext {
  factory { TrackMapper() }
  factory { MetadataMapper() }
}

val presenters = applicationContext {
  bean { ModifyTracks(get(), get(), get()) }
  bean { SearchTracks(get(), get(), get()) }
  bean { GetTracks(get(), get(), get()) }

  factory(Params.HISTORY) { arguments ->
    HistoryPresenter(get<GetTracks>(),
        get<ModifyTracks>(), arguments[Params.HISTORY])
  } bind HomeContract.Presenter::class

  factory(Params.FAVORITE) { arguments ->
    FavoritePresenter(get<GetTracks>(),
        get<ModifyTracks>(), arguments[Params.FAVORITE])
  } bind HomeContract.Presenter::class

  factory(Params.ACTIONS) { arguments ->
    ActionsPresenter(get(), arguments[Params.ACTIONS])
  } bind ActionsPresenter::class

  factory(Params.SEARCH) { arguments ->
    TrackPresenter(get<SearchTracks>(),
        arguments[Params.SEARCH]) as SearchContract.Presenter<Track>
  }

  bean {
    PlaybackManager(get(), get(), get<ModifyTracks>(), get<MetadataMapper>())
  }
}

val mediaPlayer = applicationContext {
  bean { playback(get()) }
}

private fun playback(context: Context): Playback {
  val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
  val wifiManager = (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
      .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock")
  return MediaPlayback21(context, audioManager, wifiManager)
}


object Params {
  const val HISTORY = "history"
  const val FAVORITE = "favorite"
  const val SEARCH = "search"
  const val ACTIONS = "actions"
}