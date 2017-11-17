package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam

interface InsertInteractor{
    fun insert(success:()->Unit,error:(Throwable)->Unit,param: ModifyParam)
}