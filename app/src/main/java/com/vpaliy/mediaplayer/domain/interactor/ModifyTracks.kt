package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import com.vpaliy.mediaplayer.domain.interactor.params.SimpleConsumer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModifyTracks @Inject
constructor(val repository: Repository, val scheduler: BaseScheduler):ModifyInteractor{

    override fun insert(consumer: SimpleConsumer, param: ModifyParam) {
        repository.insert(param)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(consumer.onSuccess,consumer.onError)
    }

    override fun clearAll(consumer: SimpleConsumer, type: TrackType) {
        repository.clearAll(type)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(consumer.onSuccess,consumer.onError)
    }

    override fun remove(consumer: SimpleConsumer, param: ModifyParam) {
        repository.remove(param)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(consumer.onSuccess,consumer.onError)
    }
}