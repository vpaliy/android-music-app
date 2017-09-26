package com.vpaliy.mediaplayer

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.soundcloud.model.TrackEntity

object FakeDataProvider {

    const val FAKE_ID = "fake_id"
    const val FAKE_ART = "fake_art"
    const val FAKE_TITLE = "fake_title"
    const val FAKE_DURATION = "fake_duration"
    const val FAKE_RELEASE_DATE = "fake_release_date"
    const val FAKE_STREAM_URL = "fake_stream_url"
    const val FAKE_ARTIST = "fake_artist"
    const val FAKE_GENRES = "Rock,Indie,Pop,Pop Rock,Alternative,Rap,Others"
    const val FAKE_BOOLEAN = true

    fun buildTrack(): Track {
        val track = Track()
        track.id = FAKE_ID
        track.streamUrl = FAKE_STREAM_URL
        track.artworkUrl = FAKE_ART
        track.duration = FAKE_DURATION
        track.releaseDate = FAKE_RELEASE_DATE
        track.title = FAKE_TITLE
        track.artist = FAKE_ARTIST
        track.isLiked = FAKE_BOOLEAN
        return track
    }

    fun buildTrackEntity():TrackEntity {
        val trackEntity = TrackEntity()
        trackEntity.id= FAKE_ID
        trackEntity.stream_url= FAKE_STREAM_URL
        trackEntity.artwork_url= FAKE_ART
        trackEntity.duration= FAKE_DURATION
        trackEntity.tags_list= FAKE_GENRES
        trackEntity.release= FAKE_RELEASE_DATE
        trackEntity.title= FAKE_TITLE
        trackEntity.is_streamable=true
        return trackEntity
    }

    fun<T> buildList(count:Int, create:()->T):List<T> {
        val list= arrayListOf<T>()
        for (index in 0..count){
            list.add(create())
        }
        return list
    }
}