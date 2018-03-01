package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule {
  @Singleton
  @Provides
  internal fun modifyInteractor(repository: Repository, scheduler: BaseScheduler) = ModifyTracks(repository, scheduler)

  @Singleton
  @Provides
  internal fun searchInteractor(repository: Repository, scheduler: BaseScheduler) = SearchTracks(repository, scheduler)

  @Singleton
  @Provides
  internal fun tracksInteractor(repository: Repository, scheduler: BaseScheduler) = GetTracks(repository, scheduler)
}
