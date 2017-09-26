package com.vpaliy.mediaplayer.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicDatabase @Inject
constructor(context: Context):SQLiteOpenHelper(context,name,null, version){

    companion object {
        private val version=1
        private val name="music.db"
    }

    object Tracks{
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
    }


    override fun onCreate(db: SQLiteDatabase?){
        db?.execSQL("CREATE TABLE ${Tracks.TABLE} ("+
                "${Tracks.ID} TEXT NOT NULL,"+
                "${Tracks.ARTIST} TEXT NOT NULL,"+
                "${Tracks.TITLE} TEXT NOT NULL,"+
                "${Tracks.ART_URL} TEXT NOT NULL,"+
                "${Tracks.DURATION} TEXT NOT NULL,"+
                "${Tracks.IS_LIKED} INTEGER NOT NULL,"+
                "${Tracks.IS_SAVED} INTEGER NOT NULL,"+
                "${Tracks.STREAM_URL} TEXT NOT NULL,"+
                "${Tracks.RELEASE} TEXT,"+
                " UNIQUE (${Tracks.ID}) ON CONFLICT REPLACE)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Tracks.TABLE}")
    }
}