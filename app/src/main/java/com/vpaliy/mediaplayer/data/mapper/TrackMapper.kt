package com.vpaliy.mediaplayer.data.mapper

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.soundcloud.model.TrackEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackMapper @Inject constructor():Mapper<Track,TrackEntity>(){

    override fun map(fake: TrackEntity?): Track? {
        if(fake!=null){
            val real=Track()
            return real
        }
        return null
    }

    override fun reverse(real: Track?): TrackEntity? {
        if(real!=null){
            val fake=TrackEntity()
            return fake
        }
        return null
    }
}
