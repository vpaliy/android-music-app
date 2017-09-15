package com.vpaliy.mediaplayer.domain.interactor

interface InsertInteractor<in Params>{
    fun insert(success:()->Unit,error:(Throwable)->Unit,params: Params)
}
