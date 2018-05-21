package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.RequestError
import com.vpaliy.mediaplayer.domain.model.TrackType

interface ClearInteractor {
  fun clearAll(success: () -> Unit, error: (RequestError) -> Unit, type: TrackType)
  fun remove(success: () -> Unit, error: (RequestError) -> Unit, request: ModifyRequest)
}