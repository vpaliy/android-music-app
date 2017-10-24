package com.vpaliy.mediaplayer.domain.interactor.params

import com.vpaliy.mediaplayer.domain.model.Track

class Response<out Request>(val request:Request,val result:List<Track>)