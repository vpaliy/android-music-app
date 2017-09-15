package com.vpaliy.mediaplayer.domain.interactor

interface ClearInteractor<in Params> {
    fun clear(complete: () -> Unit, error: (Throwable) -> Unit)
    fun remove(complete: () -> Unit, error: (Throwable) -> Unit, params: Params)
}