package com.vpaliy.mediaplayer.domain.usecases.params

class Consumer<in Request>(val success:(Response<Request>)->Unit, val error:(Throwable)->Unit)