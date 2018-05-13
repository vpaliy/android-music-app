package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.kotlin_extensions.then
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.wrongArgument

class GetTracks (val repository: Repository, scheduler: BaseScheduler)
  : SingleInteractor<TrackType, List<Track>>(scheduler) {

  override fun buildSingle(request: TrackType?)
      = request then (repository::fetch) ?: wrongArgument()
}