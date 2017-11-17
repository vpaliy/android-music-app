package com.vpaliy.mediaplayer.data


import com.vpaliy.mediaplayer.data.local.TrackHandler
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import com.vpaliy.mediaplayer.domain.interactor.params.Response
import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject
constructor(val mapper: Mapper<Track,TrackEntity>, val service:SoundCloudService,
            val handler: TrackHandler, val filter:Filter,scheduler: BaseScheduler):Repository {

    private var likeSet=HashSet<String>()
    private var recentSet=HashSet<String>()

    init {
        Single.fromCallable({handler.queryHistory()})
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({list->convertToSet(recentSet,list)})
        Single.fromCallable({handler.queryLoved()})
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe({list->convertToSet(likeSet,list)})
    }

    override fun search(page: SearchPage): Single<Response<SearchPage>> {
        return service.searchTracksPage(TrackEntity.Filter
                .start().byName(page.query)
                .withPagination()
                .limit(100)
                .createOptions())
                .map({result->
                    result.collection
                }).map(filter::filter)
                .map(mapper::map)
                .map(this::filter)
                .map {Response(page,it) }
    }

    override fun clearAll(type: TrackType): Completable {
        return when(type){
            TrackType.HISTORY-> {
                recentSet.clear()
                Completable.fromCallable(handler::deleteHistory)
            }
            TrackType.FAVORITE-> {
                likeSet.clear()
                Completable.fromCallable(handler::deleteLoved)
            }
        }
    }

    override fun fetch(type: TrackType): Single<Response<TrackType>> {
        return Single.fromCallable({
            when(type) {
                TrackType.FAVORITE -> handler.queryLoved()
                TrackType.HISTORY -> handler.queryHistory()
            }
        }).map{Response(type,it)}
    }

    override fun insert(param: ModifyParam): Completable {
        when(param.type){
            TrackType.FAVORITE->{
                if(!likeSet.contains(param.track.id)){
                    return Completable.fromCallable({handler.update(love(param.track,true))})
                }
            }
            TrackType.HISTORY->{
                if(!recentSet.contains(param.track.id)){
                    return Completable.fromCallable({handler.update(save(param.track,true))})
                }
            }
        }
        return Completable.complete()
    }

    override fun remove(param: ModifyParam): Completable {
        return when(param.type){
            TrackType.FAVORITE->{
                Completable.fromCallable({handler.update(love(param.track,false))})
            }
            TrackType.HISTORY->{
                Completable.fromCallable({handler.update(save(param.track,false))})
            }
        }
    }

    private fun convertToSet(set:HashSet<String>, list:List<Track>)=list.forEach{
        it.id?.let {set.add(it)}
    }

    private fun filter(list: List<Track>?)=list?.let{
        it.forEach{track->
            track.isSaved=likeSet.contains(track.id)
            track.isLiked=likeSet.contains(track.id)
        }
        it
    }?: emptyList()

    private fun save(track:Track,saved:Boolean):Track {
        if(!saved) recentSet.remove(track.id)
        else track.id?.let{recentSet.add(it)}
        track.isSaved=saved
        return track
    }

    private fun love(track:Track?,liked:Boolean)=track?.let {
        if(!liked) likeSet.remove(track.id)
        else track.id?.let{likeSet.add(it)}
        it.isLiked=liked
        it
    }
}
