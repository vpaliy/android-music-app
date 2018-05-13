package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest

class ModifyTracks(val repository: Repository, val scheduler: BaseScheduler) : ModifyInteractor {

  override fun insert(success: () -> Unit, error: (Throwable) -> Unit, request: ModifyRequest) {
    repository.insert(request)
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe(success, error)
  }

  override fun clearAll(success: () -> Unit, error: (Throwable) -> Unit, type: TrackType) {
    repository.clearAll(type)
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe(success, error)
  }

  override fun remove(success: () -> Unit, error: (Throwable) -> Unit, request: ModifyRequest) {
    repository.remove(request)
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe(success, error)
  }
}