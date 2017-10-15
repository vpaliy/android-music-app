package com.vpaliy.mediaplayer.data.mapper

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.soundcloud.model.TrackEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackMapper @Inject
constructor():Mapper<Track,TrackEntity>(){
    override fun map(fake: TrackEntity?): Track? {
        return fake?.let {
            val real=Track()
            real.artworkUrl=it.artwork_url
            real.artist=it.user?.username
            real.title=it.title
            real.duration=it.duration
            real.releaseDate=it.release
            real.id=it.id
            real.streamUrl=it.stream_url
            return real
        }
    }
    override fun reverse(real: Track?): TrackEntity? {
        return real?.let {
            val fake=TrackEntity()
            fake.title=it.title
            fake.artwork_url=it.artworkUrl
            fake.duration=it.duration
            fake.release=it.releaseDate
            fake.id=it.id
            fake.stream_url=it.streamUrl
            return fake
        }
    }
}
