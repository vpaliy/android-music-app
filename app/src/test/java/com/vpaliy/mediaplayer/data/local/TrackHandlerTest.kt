package com.vpaliy.mediaplayer.data.local

import android.content.Context
import android.database.Cursor
import android.os.Build
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.vpaliy.mediaplayer.BuildConfig
import com.vpaliy.mediaplayer.FakeDataProvider
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.data.local.MusicDatabase.Tracks
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [(Build.VERSION_CODES.LOLLIPOP)])
class TrackHandlerTest {

  private var database: MusicDatabase = MusicDatabase(RuntimeEnvironment.application ?: mock<Context>())
  private val handler: TrackHandler by lazy { TrackHandler(database) }

  @Test
  fun dummyTest() {
    val list = FakeDataProvider.buildList(10, { Track() })
    for ((index, element) in list.withIndex()) {
      element.id = index.toString()
      element.isSaved = true
      handler.update(element)
    }
  }

  private fun compare(track: Track?, result: Track?) {
    if (track == null || result == null) {
      assertNull(track)
      assertNull(result)
      return
    }
    assertEquals(track.title, result.title)
    assertEquals(track.streamUrl, result.streamUrl)
    assertEquals(track.artist, result.artist)
    assertEquals(track.artworkUrl, result.artworkUrl)
    assertEquals(track.duration, result.duration)
    assertEquals(track.id, result.id)
    assertEquals(track.releaseDate, result.releaseDate)
    assertEquals(track.isLiked, result.isLiked)
    assertEquals(track.isSaved, result.isSaved)
  }

  private fun mockCursor(track: Track): Cursor {
    val cursor = mock<Cursor>()
    for ((index, element) in Tracks.COLUMNS.withIndex())
      given(cursor.getColumnIndex(element)).willReturn(index)
    whenever(cursor.getString(any())).thenAnswer({
      val index = it.arguments[0]
      when (index) {
        Tracks.ARTIST -> track.artist
        Tracks.ART_URL -> track.artworkUrl
        Tracks.DURATION -> track.duration
        Tracks.ID -> track.id
        Tracks.RELEASE -> track.releaseDate
        Tracks.STREAM_URL -> track.streamUrl
        Tracks.TITLE -> track.title
        else -> IllegalArgumentException()
      }
    })
    whenever(cursor.getInt(any())).thenAnswer {
      when (it.arguments[0]) {
        Tracks.IS_LIKED -> if (track.isLiked) 1 else 0
        Tracks.IS_SAVED -> if (track.isSaved) 1 else 0
        else -> IllegalArgumentException()
      }
    }
    return cursor
  }

}