package com.vpaliy.mediaplayer.di.module

import android.content.Context
import android.os.Build
import com.vpaliy.mediaplayer.playback.MediaPlayback
import com.vpaliy.mediaplayer.playback.MediaPlayback21
import android.net.wifi.WifiManager
import android.media.AudioManager
import android.support.v4.media.MediaMetadataCompat
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.domain.interactor.ModifyTracks
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.Playback
import com.vpaliy.mediaplayer.playback.MetadataMapper
import com.vpaliy.mediaplayer.playback.PlaybackManager
import com.vpaliy.mediaplayer.domain.playback.PlaybackScope
import dagger.Module
import dagger.Provides

@Module
class MediaPlayerModule{
    @PlaybackScope
    @Provides
    fun playback(context: Context): Playback {
        val audioManager = (context.getSystemService(Context.AUDIO_SERVICE)) as AudioManager
        val wifiManager = (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock")
        return if(Build.VERSION.SDK_INT>=21) MediaPlayback21(context, audioManager, wifiManager)
            else MediaPlayback(context, audioManager, wifiManager)
    }

    @PlaybackScope
    @Provides
    fun mapper(): Mapper<MediaMetadataCompat, Track> = MetadataMapper()

    @PlaybackScope
    @Provides
    fun manager(context:Context,playback:Playback,mapper:Mapper<MediaMetadataCompat,Track>,history:ModifyTracks)
            =PlaybackManager(playback,context,history,mapper)

}
