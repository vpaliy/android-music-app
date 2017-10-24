package com.vpaliy.mediaplayer.domain

import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.usecases.params.Response
import io.reactivex.Completable
import io.reactivex.Single

interface IRepository{
    fun clear(type:TrackType):Completable
    fun remove(track:Track,type:TrackType):Completable
    fun insert(track:Track,type: TrackType):Completable
    fun fetch(type:TrackType): Single<Response<TrackType>>
    fun search(page: SearchPage):Single<Response<SearchPage>>
}