package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import io.reactivex.Single

abstract class SingleInteractor<Request, Result>(val scheduler: BaseScheduler) {
  fun execute(success: (Result) -> Unit, error: (Throwable) -> Unit, request: Request? = null) {
    buildSingle(request)
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe(success, error)
  }

  fun execute(success: (Request, Result) -> Unit, error: (Throwable) -> Unit, request: Request) {
    execute({ result -> success.invoke(request, result) }, error, request)
  }

  abstract fun buildSingle(request: Request?): Single<Result>
}