package com.vpaliy.mediaplayer.data.local

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import com.vpaliy.mediaplayer.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackHandler @Inject
constructor(private val database:SQLiteOpenHelper){

    fun queryHistory():List<Track>{
        val cursor=database.readableDatabase?.rawQuery("SELECT * FROM ${MusicDatabase.Tracks.TABLE} " +
                "WHERE ${MusicDatabase.Tracks.IS_SAVED}=?", arrayOf("1"))
        if(cursor!=null){
            val result= arrayListOf<Track>()
            while(cursor.moveToNext()) {
                val track=convert(cursor)
                if(track!=null) result.add(track)
            }
            cursor.close()
            return result
        }
        return emptyList()
    }

    fun queryLoved():List<Track>{
        val cursor=database.readableDatabase?.rawQuery("SELECT * FROM ${MusicDatabase.Tracks.TABLE} " +
                "WHERE ${MusicDatabase.Tracks.IS_LIKED}=?", arrayOf("1"))
        if(cursor!=null){
            val result= arrayListOf<Track>()
            while(cursor.moveToNext()) {
                val track=convert(cursor)
                if(track!=null) result.add(track)
            }
            cursor.close()
            return result
        }
        return emptyList()
    }

    fun update(track: Track?)=database.writableDatabase?.
            insert(MusicDatabase.Tracks.TABLE,null,convert(track))

    fun deleteHistory(){
        database.writableDatabase?.execSQL("DELETE FROM ${MusicDatabase.Tracks.TABLE} WHERE" +
                " ${MusicDatabase.Tracks.IS_SAVED}=?", arrayOf("1"))
    }

    fun deleteLoved(){
        database.writableDatabase?.execSQL("DELETE FROM ${MusicDatabase.Tracks.TABLE} WHERE" +
                " ${MusicDatabase.Tracks.IS_LIKED}=?", arrayOf("1"))
    }

    private fun convert(track:Track?)=track?.let {
        val values= ContentValues()
        values.put(MusicDatabase.Tracks.ARTIST,track.artist)
        values.put(MusicDatabase.Tracks.ART_URL,track.artworkUrl)
        values.put(MusicDatabase.Tracks.DURATION,track.duration)
        values.put(MusicDatabase.Tracks.STREAM_URL,track.streamUrl)
        values.put(MusicDatabase.Tracks.RELEASE,track.releaseDate)
        values.put(MusicDatabase.Tracks.ID,track.id)
        values.put(MusicDatabase.Tracks.IS_LIKED,if(track.isLiked) 1 else 0)
        values.put(MusicDatabase.Tracks.IS_SAVED,if(track.isSaved) 1 else 0)
        values
    }

    private fun convert(cursor:Cursor?)=cursor?.let {
        val track=Track()
        track.artworkUrl=cursor.getString(cursor.getColumnIndex(MusicDatabase.Tracks.ART_URL))
        track.artist=cursor.getString(cursor.getColumnIndex(MusicDatabase.Tracks.ARTIST))
        track.duration=cursor.getString(cursor.getColumnIndex(MusicDatabase.Tracks.DURATION))
        track.streamUrl=cursor.getString(cursor.getColumnIndex(MusicDatabase.Tracks.STREAM_URL))
        track.releaseDate=cursor.getString(cursor.getColumnIndex(MusicDatabase.Tracks.RELEASE))
        track.id=cursor.getString(cursor.getColumnIndex(MusicDatabase.Tracks.ID))
        track.isLiked=cursor.getInt(cursor.getColumnIndex(MusicDatabase.Tracks.IS_LIKED))==1
        track.isSaved=cursor.getInt(cursor.getColumnIndex(MusicDatabase.Tracks.IS_SAVED))==1
        track
    }
}
