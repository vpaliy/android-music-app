package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.kotlin_extensions.then
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.wrongArgument
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchTracks @Inject constructor(val repository: Repository, scheduler: BaseScheduler)
  : SingleInteractor<SearchPage, List<Track>>(scheduler) {

  override fun buildSingle(request: SearchPage?)
      = request then (repository::search) ?: wrongArgument()
}