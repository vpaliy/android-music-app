package com.vpaliy.mediaplayer.di.module

import android.content.Context
import android.os.Build
import com.vpaliy.mediaplayer.playback.BasePlayback
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.vpaliy.mediaplayer.playback.MediaPlayback
import com.vpaliy.mediaplayer.playback.MediaPlayback21
import android.net.wifi.WifiManager
import android.media.AudioManager
import com.vpaliy.mediaplayer.domain.playback.Playback


@Module
class MediaPlayerModule{

    fun playback(context: Context): Playback {
        val audioManager = AudioManager::class.java.cast(context.getSystemService(Context.AUDIO_SERVICE))
        val wifiManager = (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock")
        return if(Build.VERSION.SDK_INT>=21) MediaPlayback21(context, audioManager, wifiManager)
            else MediaPlayback(context, audioManager, wifiManager)
    }

}
