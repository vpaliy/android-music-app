package com.vpaliy.mediaplayer.data.local

import android.net.Uri

interface MusicContract{

    object Track{
        const val TABLE="tracks"
        const val STREAM_URL="track_stream_url"
        const val ART_URL="art_url"
        const val ID="track_id"
        const val TITLE="track_title"
        const val RELEASE="track_release"
        const val ARTIST="track_artist"
        const val IS_SAVED="track_is_saved"
        const val IS_LIKED="track_is_liked"
        const val DURATION="track_duration"

        val CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH).build()

        fun buildTrackUri(id:String)= CONTENT_URI.buildUpon().appendPath(id).build()
        fun trackId(uri:Uri)=uri.pathSegments[0]
    }

    companion object {
        const val CONTENT_AUTHORITY = "com.vpaliy.fitness_sound"
        const val PATH="tracks"
        val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)

    }
}