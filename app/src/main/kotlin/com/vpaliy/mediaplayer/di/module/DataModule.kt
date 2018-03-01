package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.data.Filter
import com.vpaliy.mediaplayer.data.MusicRepository
import com.vpaliy.mediaplayer.data.local.MusicDatabase
import com.vpaliy.mediaplayer.data.local.TrackHandler
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.data.mapper.TrackMapper
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.soundcloud.model.TrackEntity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
  @Singleton
  @Provides
  internal fun repository(repository: MusicRepository): Repository = repository

  @Singleton
  @Provides
  internal fun mapper(mapper: TrackMapper): Mapper<Track, TrackEntity> = mapper

  @Singleton
  @Provides
  internal fun filter() = Filter()

  @Singleton
  @Provides
  internal fun handler(helper: MusicDatabase) = TrackHandler(helper)
}