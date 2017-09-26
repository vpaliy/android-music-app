package com.vpaliy.mediaplayer.domain.playback

import com.vpaliy.mediaplayer.domain.model.Track
import java.util.Collections

class QueueManager(private var tracks: MutableList<Track>, var index: Int) {

    fun setTracks(tracks: MutableList<Track>) {
        this.tracks = tracks
        invalidateIndexIfNeeded()
    }

    operator fun next(): Track {
        if (hasNext()) {
            return tracks[++index]
        }
        return current()
    }

    fun previous(): Track {
        if (hasPrevious()) {
            return tracks[--index]
        }
        return current()
    }

    private fun invalidateIndexIfNeeded() {
        if (tracks.size <= index) {
            index = 0
        }
    }

    fun shuffle()=Collections.shuffle(tracks)

    operator fun hasNext()=tracks.size > index + 1

    fun hasPrevious()=index - 1 >= 0

    fun size()=tracks.size

    fun addTrack(track: Track)=tracks.add(track)

    fun current()= tracks[index]
}