package com.vpaliy.mediaplayer.domain.playback

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.then
import java.util.Collections

class QueueManager(private var tracks: MutableList<Track>, var index: Int) {

  fun next() = !hasNext() then (current()) ?: tracks[++index]

  fun previous() = !hasPrevious() then (current()) ?: tracks[--index]

  fun shuffle() = Collections.shuffle(tracks)

  fun hasNext() = tracks.size > (index + 1)

  private fun hasPrevious() = index - 1 >= 0

  fun size() = tracks.size

  fun addTrack(track: Track) = tracks.add(track)

  fun current() = tracks[index]
}