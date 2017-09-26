package com.vpaliy.mediaplayer.data


import com.vpaliy.mediaplayer.data.local.TrackHandler
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.Page
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject
constructor(val mapper: Mapper<Track,TrackEntity>, val service:SoundCloudService,
            val handler: TrackHandler, val filter:Filter):Repository{

    private var page:Page<TrackEntity>?=null

    override fun fetchHistory():Single<List<Track>>
            = Single.fromCallable({handler.queryHistory()})

    override fun fetchLiked():Single<List<Track>>
            = Single.fromCallable({handler.queryLoved()})

    override fun query(query: String?): Single<List<Track>> {
        return service.searchTracksPage(TrackEntity.Filter
                .start().byName(query)
                .withPagination()
                .limit(100)
                .createOptions())
                .map({result->
                    page=result
                    result.collection
                }).map(filter::filter)
                .map(mapper::map)
    }

    override fun nextPage(): Single<List<Track>> {
        if(page!=null){
            return service.searchTracksPage(TrackEntity.Filter
                    .start().nextPage(page)
                    .withPagination()
                    .limit(100)
                    .createOptions())
                    .map({result->
                        page=result
                        result.collection
                    }).map(filter::filter)
                    .map(mapper::map)
        }
        return Single.error(IllegalArgumentException("No more data"))
    }

    override fun like(track: Track?):Completable=
            Completable.fromCallable({handler.update(love(track,true))})

    override fun clearHistory():Completable=
            Completable.fromCallable({handler.deleteHistory()})

    override fun clearLoved():Completable=
            Completable.fromCallable({handler.deleteLoved()})

    override fun removeLoved(track: Track):Completable=
            Completable.fromCallable({handler.update(love(track,false))})

    override fun removeRecent(track: Track):Completable=
            Completable.fromCallable({handler.update(save(track,false))})

    override fun insertRecent(track: Track?):Completable=
            Completable.fromCallable({handler.update(save(track,true))})

    private fun save(track:Track?,saved:Boolean)=track?.let {
        it.isSaved=saved
        it
    }

    private fun love(track:Track?,liked:Boolean)=track?.let {
        it.isLiked=liked
        it
    }
}
