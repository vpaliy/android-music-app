package com.vpaliy.mediaplayer.di.component

import android.content.Context
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.di.module.ApplicationModule
import com.vpaliy.mediaplayer.di.module.DataModule
import com.vpaliy.mediaplayer.di.module.InteractorModule
import com.vpaliy.mediaplayer.di.module.NetworkModule
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.*
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.TrackEntity
import javax.inject.Singleton
import dagger.Component

@Singleton
@Component(modules = [(ApplicationModule::class),
  (InteractorModule::class), (DataModule::class), (NetworkModule::class)])
interface ApplicationComponent {
  fun inject(activity: BaseActivity)
  fun context(): Context
  fun scheduler(): BaseScheduler
  fun mapper(): Mapper<Track, TrackEntity>
  fun navigator(): Navigator
  fun repository(): Repository
  fun modifyInteractor(): ModifyTracks
  fun searchInteractor(): SearchTracks
  fun singleInteractor(): GetTracks
  fun service(): SoundCloudService
}