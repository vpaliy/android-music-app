package com.vpaliy.mediaplayer.domain

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.QueueManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QueueManagerTest{

    private val list=mock< MutableList<Track>>{
        on(it[any<Int>()]).thenReturn(Track())
    }
    private val queueManager= QueueManager(list,0)

    @Test
    fun returnsNext(){
        whenever(list.size).thenReturn(2)
        val track=queueManager.next()
        assertNotNull(track)
        verify(list).size
        verify(list)[any<Int>()]
    }

    @Test
    fun returnsPrevious(){
        whenever(list.size).thenReturn(2)
        val track=queueManager.previous()
        assertNotNull(track)
        verify(list)[any<Int>()]
    }

    @Test
    fun addTrack(){
        queueManager.addTrack(Track())
        verify(list).add(any<Track>())
    }

    @Test
    fun returnsCurrent(){
        queueManager.current()
        verify(list)[any<Int>()]
    }
}