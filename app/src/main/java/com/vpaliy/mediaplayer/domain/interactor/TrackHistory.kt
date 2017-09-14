package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackHistory @Inject constructor(val repository: Repository,
                               scheduler: BaseScheduler) :
        SingleInteractor<List<Track>,Void?>(scheduler){

    public override fun buildObservable(params: Void?): Single<List<Track>> {
        return repository.fetchHistory()
    }
}
