package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.Track
import java.lang.IllegalArgumentException
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SearchTracks @Inject
constructor(val repository: Repository, scheduler: BaseScheduler):
        SingleInteractor<List<Track>,String>(scheduler), SearchInteractor{

    override fun buildObservable(params: String?): Single<List<Track>> {
        if(!params.isNullOrBlank()){
            return repository.query(params)
        }
        return Single.error(IllegalArgumentException("Query is null or empty!"))
    }

    override fun query(success: (List<Track>) -> Unit, error: (Throwable) -> Unit, query: String?)
                = execute(success,error,query)

    override fun nextPage(success:(List<Track>)->Unit,error:(Throwable)->Unit){
        repository.nextPage()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(success,error)
    }
}