package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import io.reactivex.Completable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

abstract class CompletableInteractor<in Params> constructor(val scheduler: BaseScheduler){

    fun execute(complete: Action, error:Consumer<in Throwable>, params:Params?){
        buildCompletable(params)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(complete,error)
    }

    protected abstract fun buildCompletable(params: Params?):Completable
}