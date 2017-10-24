package com.vpaliy.mediaplayer.domain.interactor.params

class Consumer<in Request>(val success:(Response<Request>)->Unit, val error:(Throwable)->Unit)