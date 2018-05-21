package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.RequestError

interface InsertInteractor {
  fun insert(success: () -> Unit, error: (RequestError) -> Unit, request: ModifyRequest)
}