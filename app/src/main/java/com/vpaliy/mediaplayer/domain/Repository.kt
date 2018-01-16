package com.vpaliy.mediaplayer.domain

import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
  fun clearAll(type: TrackType): Completable
  fun remove(request: ModifyRequest): Completable
  fun insert(request: ModifyRequest): Completable
  fun fetch(type: TrackType): Single<List<Track>>
  fun search(page: SearchPage): Single<List<Track>>
}