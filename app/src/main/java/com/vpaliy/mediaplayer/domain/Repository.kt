package com.vpaliy.mediaplayer.domain

import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
    fun fetchHistory():Single<List<Track>>
    fun fetchLiked():Single<List<Track>>
    fun query(query:String?):Single<List<Track?>>
    fun nextPage(): Single<List<Track?>>
    fun insertRecent(track:Track?):Completable
    fun like(track:Track?):Completable
    fun clearHistory():Completable
    fun clearLoved():Completable
    fun removeLoved(track:Track):Completable
    fun removeRecent(track:Track):Completable
}
