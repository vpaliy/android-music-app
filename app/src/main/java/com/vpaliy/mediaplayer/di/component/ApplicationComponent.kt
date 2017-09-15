package com.vpaliy.mediaplayer.di.component

import android.content.Context
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.di.module.ApplicationModule
import com.vpaliy.mediaplayer.di.module.DataModule
import com.vpaliy.mediaplayer.di.module.InteractorModule
import com.vpaliy.mediaplayer.di.module.NetworkModule
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.SearchTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.base.RxBus
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.TrackEntity
import javax.inject.Singleton
import dagger.Component

@Singleton
@Component(modules = arrayOf(ApplicationModule::class,
        InteractorModule::class,
        DataModule::class,
        NetworkModule::class))
interface ApplicationComponent {
    fun inject(activity:BaseActivity)
    fun context(): Context
    fun scheduler():BaseScheduler
    fun mapper(): Mapper<Track, TrackEntity>
    fun bus(): RxBus
    fun navigator():Navigator
    fun repository():Repository
    fun lovedInteractor():LovedTracks
    fun searchInteractor():SearchTracks
    fun historyInteractor():TrackHistory
    fun service():SoundCloudService
}