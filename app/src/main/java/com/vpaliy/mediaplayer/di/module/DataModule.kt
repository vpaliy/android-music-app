package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.data.Filter
import com.vpaliy.mediaplayer.data.MusicRepository
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
    fun repository(repository:MusicRepository):Repository=repository

    @Singleton
    @Provides
    fun mapper(mapper:TrackMapper): Mapper<Track, TrackEntity> =mapper

    @Singleton
    @Provides
    fun filter()=Filter()
}