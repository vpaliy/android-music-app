package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Single

open class LovedTracks constructor(val repository: Repository,
                                   scheduler: BaseScheduler):
        SingleInteractor<List<Track>,Void>(scheduler){

    override fun buildObservable(params: Void?): Single<List<Track>> {
        return repository.fetchLiked()
    }
}