package com.vpaliy.mediaplayer.data

import com.vpaliy.mediaplayer.data.local.TrackHandler
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.TrackEntity
import io.reactivex.Completable
import io.reactivex.Single

class MusicRepository (
    private val mapper: Mapper<Track, TrackEntity>,
    private val service: SoundCloudService,
    private val handler: TrackHandler,
    private val filter: Filter,
    scheduler: BaseScheduler
) : Repository {

  private var likeSet = HashSet<String>()
  private var recentSet = HashSet<String>()

  init {
    Single.fromCallable({ handler.queryHistory() })
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe({ list -> convertToSet(recentSet, list) })
    Single.fromCallable({ handler.queryLoved() })
        .subscribeOn(scheduler.io())
        .observeOn(scheduler.ui())
        .subscribe({ list -> convertToSet(likeSet, list) })
  }

  override fun search(page: SearchPage): Single<List<Track>> {
    return service.searchTracksPage(TrackEntity.Filter
        .start().byName(page.query)
        .withPagination()
        .limit(100)
        .createOptions())
        .map({ result ->
          result.collection
        }).map(filter::filter)
        .map(mapper::map)
        .map(this::filter)
  }

  override fun clearAll(type: TrackType): Completable {
    return when (type) {
      TrackType.History -> {
        recentSet.clear()
        Completable.fromCallable(handler::deleteHistory)
      }
      TrackType.Favorite -> {
        likeSet.clear()
        Completable.fromCallable(handler::deleteLoved)
      }
    }
  }

  override fun fetch(type: TrackType): Single<List<Track>> {
    return Single.fromCallable({
      when (type) {
        TrackType.Favorite -> handler.queryLoved()
        TrackType.History -> handler.queryHistory()
      }
    })
  }

  override fun insert(request: ModifyRequest): Completable {
    when (request.type) {
      TrackType.Favorite -> {
        if (!likeSet.contains(request.track.id)) {
          return Completable.fromCallable({ handler.update(love(request.track, true)) })
        }
      }
      TrackType.History -> {
        if (!recentSet.contains(request.track.id)) {
          return Completable.fromCallable({ handler.update(save(request.track, true)) })
        }
      }
    }
    return Completable.complete()
  }

  override fun remove(request: ModifyRequest): Completable {
    return when (request.type) {
      TrackType.Favorite -> {
        Completable.fromCallable({ handler.update(love(request.track, false)) })
      }
      TrackType.History -> {
        Completable.fromCallable({ handler.update(save(request.track, false)) })
      }
    }
  }

  private fun convertToSet(set: HashSet<String>, list: List<Track>) = list.forEach {
    it.id?.let { set.add(it) }
  }

  private fun filter(list: List<Track>?) = list?.let {
    it.forEach { track ->
      track.isSaved = likeSet.contains(track.id)
      track.isLiked = likeSet.contains(track.id)
    }
    it
  } ?: emptyList()

  private fun save(track: Track, saved: Boolean): Track {
    if (!saved) recentSet.remove(track.id)
    else track.id?.let { recentSet.add(it) }
    track.isSaved = saved
    return track
  }

  private fun love(track: Track?, liked: Boolean) = track?.let {
    if (!liked)
      likeSet.remove(track.id)
    else
      track.id?.let { likeSet.add(it) }
    it.isLiked = liked
    it
  }
}
