package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import com.vpaliy.mediaplayer.domain.interactor.params.SimpleConsumer

interface ClearInteractor{
    fun clearAll(consumer: SimpleConsumer,type:TrackType)
    fun remove(consumer: SimpleConsumer,param: ModifyParam)
}