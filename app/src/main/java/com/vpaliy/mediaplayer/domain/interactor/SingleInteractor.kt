package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.RequestError
import io.reactivex.Single

abstract class SingleInteractor<Request, Result>(
    protected val scheduler: BaseScheduler,
    private val errorHandler: ErrorHandler
) {

  fun execute(success: (Result) -> Unit, error: (RequestError) -> Unit, request: Request? = null) {
    buildSingle(request)
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe(success, { throwable ->
          error(errorHandler.handle(throwable))
        })
  }

  fun execute(success: (Request, Result) -> Unit, error: (RequestError) -> Unit, request: Request) {
    execute({ result -> success.invoke(request, result) }, error, request)
  }

  abstract fun buildSingle(request: Request?): Single<Result>
}