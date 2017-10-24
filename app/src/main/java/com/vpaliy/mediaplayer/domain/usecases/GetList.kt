package com.vpaliy.mediaplayer.domain.usecases

import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.domain.IRepository
import com.vpaliy.mediaplayer.ifNotNull
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetList @Inject constructor(repository: IRepository, scheduler: BaseScheduler)
    :SingleInteractor<TrackType>(repository,scheduler){

    override fun buildCase(params: TrackType?)
            =params.ifNotNull(repository::fetch,
            Single.error(IllegalArgumentException()))
}