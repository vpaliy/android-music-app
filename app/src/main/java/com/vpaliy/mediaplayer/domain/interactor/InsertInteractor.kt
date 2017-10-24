package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import com.vpaliy.mediaplayer.domain.interactor.params.SimpleConsumer

interface InsertInteractor{
    fun insert(success:()->Unit,error:(Throwable)->Unit,param: ModifyParam)
}