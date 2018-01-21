package com.vpaliy.mediaplayer.di.module

import android.content.Context
import com.github.simonpercic.oklog3.OkLogInterceptor
import com.vpaliy.mediaplayer.CLIENT_ID
import com.vpaliy.soundcloud.SoundCloud
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.Token
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule constructor(private val token: Token?) {
  @Singleton
  @Provides
  internal fun service(context: Context): SoundCloudService =
      SoundCloud.Builder(context, CLIENT_ID)
          .setInterceptor(OkLogInterceptor.builder()
              .setBaseUrl("http://oklog.responseecho.com")
              .build())
          .setToken(token).build().soundCloudService
}
