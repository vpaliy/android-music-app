package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import com.vpaliy.mediaplayer.domain.interactor.params.SimpleConsumer

interface InsertInteractor{
    fun insert(consumer: SimpleConsumer,param: ModifyParam)
}