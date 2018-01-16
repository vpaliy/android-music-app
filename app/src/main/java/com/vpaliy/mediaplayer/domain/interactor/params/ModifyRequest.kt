package com.vpaliy.mediaplayer.domain.interactor.params

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType

data class ModifyRequest(val type: TrackType, val track: Track)