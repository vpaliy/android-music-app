package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.model.Track

interface SearchInteractor {
    fun query(success:(List<Track>)->Unit,error:(Throwable)->Unit,query:String?)
    fun nextPage(success: (List<Track>) -> Unit,error: (Throwable) -> Unit)
    fun dispose()
}