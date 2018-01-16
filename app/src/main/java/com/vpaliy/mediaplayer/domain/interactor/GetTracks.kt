package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.notNullThen
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTracks @Inject constructor(repository: Repository, scheduler: BaseScheduler)
  : SingleInteractor<TrackType>(repository, scheduler) {

  override fun buildCase(params: TrackType?)
      = params notNullThen (repository::fetch)
      ?: Single.error(IllegalArgumentException())
}