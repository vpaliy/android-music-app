package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class LovedTracks @Inject
constructor(val repository: Repository,
            scheduler: BaseScheduler):
        SingleInteractor<List<Track>,Void>(scheduler),
        ClearInteractor<Track>, InsertInteractor<Track>{

    override fun insert(success: () -> Unit, error: (Throwable) -> Unit, params: Track) {
        repository.like(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(success,error)
    }

    override fun clear(complete: () -> Unit, error: (Throwable) -> Unit) {
        repository.clearLoved()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(complete,error)
    }

    override fun remove(complete: () -> Unit, error: (Throwable) -> Unit, params: Track) {
        repository.removeLoved(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(complete,error)
    }

    override fun buildObservable(params: Void?)=repository.fetchLiked()
}