package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.notNullThen
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchTracks @Inject constructor(repository: Repository, scheduler: BaseScheduler)
  : SingleInteractor<SearchPage>(repository, scheduler) {

  override fun buildCase(params: SearchPage?)
      = params.notNullThen(repository::search)
      ?: Single.error(IllegalArgumentException())
}