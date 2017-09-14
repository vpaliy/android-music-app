package com.vpaliy.mediaplayer.data

import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Singleton

@Singleton
class MusicRepository:Repository{
    override fun fetchHistory(): Single<List<Track>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchLiked(): Single<List<Track>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(query: String?): Single<List<Track>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nextPage(): Single<List<Track>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun like(track: Track?): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
