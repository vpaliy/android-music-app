package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.LikeTrack
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.SearchTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InteractorModule{
    @Singleton
    @Provides
    fun likeInteractor(repository:Repository, scheduler: BaseScheduler) =LikeTrack(repository,scheduler)

    @Singleton
    @Provides
    fun lovedInteractor(repository:Repository, scheduler: BaseScheduler) =LovedTracks(repository,scheduler)

    @Singleton
    @Provides
    fun searchInteractor(repository:Repository, scheduler: BaseScheduler) = SearchTracks(repository,scheduler)

    @Singleton
    @Provides
    fun historyInteractor(repository:Repository, scheduler: BaseScheduler) =TrackHistory(repository,scheduler)
}
