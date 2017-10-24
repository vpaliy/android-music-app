package com.vpaliy.mediaplayer.domain.usecases.params

import com.google.android.exoplayer2.extractor.mp4.Track

class Response<out Request>(val request:Request,val result:List<Track>)