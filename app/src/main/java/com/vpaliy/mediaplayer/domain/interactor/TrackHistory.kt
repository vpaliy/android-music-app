package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TrackHistory @Inject
constructor(val repository: Repository, scheduler: BaseScheduler) :InsertInteractor<Track>,
        ClearInteractor<Track>, SingleInteractor<List<Track>,Void?>(scheduler){

    public override fun buildObservable(params: Void?): Single<List<Track>> {
        return repository.fetchHistory()
    }

    override fun clear(complete: () -> Unit, error: (Throwable) -> Unit) {
        repository.clearHistory()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(complete,error)
    }

    override fun remove(complete: () -> Unit, error: (Throwable) -> Unit, params: Track) {
        repository.removeRecent(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(complete,error)
    }

    override fun insert(success: () -> Unit, error: (Throwable) -> Unit, params: Track) {
        repository.insertRecent(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(success,error)
    }
}
