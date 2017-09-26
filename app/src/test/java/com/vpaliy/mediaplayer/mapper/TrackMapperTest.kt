package com.vpaliy.mediaplayer.mapper

import com.vpaliy.mediaplayer.FakeDataProvider
import com.vpaliy.mediaplayer.data.mapper.TrackMapper
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.soundcloud.model.TrackEntity
import org.junit.runners.JUnit4
import org.junit.Test
import org.junit.runner.RunWith
import junit.framework.Assert.assertEquals

@RunWith(JUnit4::class)
class TrackMapperTest {

    private val mapper=TrackMapper()

    @Test
    fun mapsFakeToReal(){
        val fake=FakeDataProvider.buildTrackEntity()
        assertEqual(mapper.map(fake),fake)
    }

    @Test
    fun mapsRealToFake(){
        val real=FakeDataProvider.buildTrack()
        assertEqual(real,mapper.reverse(real))
    }

    @Test
    fun testsNullInput(){
        val fake:TrackEntity?=null
        val real:Track?=null
        assertEqual(mapper.map(fake),mapper.reverse(real))
    }

    private fun assertEqual(real: Track?,fake:TrackEntity?){
        if(real==null || fake==null){
            assertEquals(real, fake)
            return
        }
        assertEquals(real.id,fake.id)
        assertEquals(real.artworkUrl,fake.artwork_url)
        assertEquals(real.duration,fake.duration)
        assertEquals(real.releaseDate,fake.release)
        assertEquals(real.streamUrl,fake.stream_url)
        assertEquals(real.title,fake.title)
    }
}