package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest

interface InsertInteractor {
  fun insert(success: () -> Unit, error: (Throwable) -> Unit, request: ModifyRequest)
}