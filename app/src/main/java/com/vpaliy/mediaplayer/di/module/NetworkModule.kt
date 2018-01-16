package com.vpaliy.mediaplayer.di.module

import android.content.Context
import com.vpaliy.mediaplayer.CLIENT_ID
import com.vpaliy.soundcloud.SoundCloud
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.Token
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule constructor(val token: Token?) {
  @Singleton
  @Provides
  internal fun service(context: Context): SoundCloudService =
      SoundCloud.create(CLIENT_ID)
          .appendToken(token)
          .createService(context)
}
