package com.vpaliy.mediaplayer.di.component

import android.content.Context
import com.vpaliy.mediaplayer.di.module.ApplicationModule
import com.vpaliy.mediaplayer.di.module.DataModule
import com.vpaliy.mediaplayer.di.module.InteractorModule
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.LikeTrack
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.SearchTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.soundcloud.SoundCloudService
import javax.inject.Singleton
import dagger.Component

@Singleton
@Component(modules = arrayOf(ApplicationModule::class,
        InteractorModule::class, DataModule::class))
interface ApplicationComponent {
    fun context(): Context
    fun scheduler():BaseScheduler
    fun repository():Repository
    fun likeInteractor():LikeTrack
    fun lovedInteractor():LovedTracks
    fun searchInteractor():SearchTracks
    fun historyInteractor():TrackHistory
    fun service():SoundCloudService
}