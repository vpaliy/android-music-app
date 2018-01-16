package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModifyTracks @Inject
constructor(val repository: Repository, val scheduler: BaseScheduler) : ModifyInteractor {

  override fun insert(success: () -> Unit, error: (Throwable) -> Unit, param: ModifyParam) {
    repository.insert(param)
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

  override fun remove(success: () -> Unit, error: (Throwable) -> Unit, param: ModifyParam) {
    repository.remove(param)
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe(success, error)
  }
}