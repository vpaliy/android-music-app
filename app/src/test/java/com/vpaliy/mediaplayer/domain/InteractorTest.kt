package com.vpaliy.mediaplayer.domain

import com.nhaarman.mockito_kotlin.*
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.mediaplayer.domain.model.Track
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks

@RunWith(MockitoJUnitRunner::class)
class InteractorTest{

    private val repository=mock<Repository>()
    private val scheduler=mock<BaseScheduler>{
        on(it.io()).thenReturn(Schedulers.trampoline())
        on(it.ui()).thenReturn(Schedulers.trampoline())
    }

    @InjectMocks lateinit var historyInteractor:TrackHistory
    @InjectMocks lateinit var lovedInteractor:LovedTracks

    @Test
    fun clearsHistory(){
        whenever(repository.clearHistory()).thenReturn(Completable.complete())
        historyInteractor.clear({},{})
        verify(repository).clearHistory()
        verify(scheduler).ui()
        verify(scheduler).io()
    }

    @Test
    fun throwsErrorOnHistoryClean(){
        whenever(repository.clearHistory()).thenReturn(Completable.error(Exception()))
        historyInteractor.clear({},{ assertEquals(it,Exception())})
        verify(repository).clearHistory()
        verify(scheduler).ui()
        verify(scheduler).io()
    }

    @Test
    fun clearsLoved(){
        whenever(repository.clearLoved()).thenReturn(Completable.complete())
        lovedInteractor.clear({},{})
        verify(repository).clearLoved()
        verify(scheduler).ui()
        verify(scheduler).io()
    }

    @Test
    fun throwsErrorOnLovedClean(){
        whenever(repository.clearLoved()).thenReturn(Completable.error(Exception()))
        lovedInteractor.clear({},{ assertEquals(it,Exception())})
        verify(repository).clearLoved()
        verify(scheduler).ui()
        verify(scheduler).io()
    }

    @Test
    fun removesLoved(){
        val track=Track()
        whenever(repository.removeLoved(any<Track>())).thenReturn(Completable.complete())
        lovedInteractor.remove({},{},track)
        verify(repository).removeLoved(eq(track))
        verify(scheduler).ui()
        verify(scheduler).io()
    }

    @Test
    fun removesRecent(){
        val track=Track()
        whenever(repository.removeRecent(any<Track>())).thenReturn(Completable.complete())
        historyInteractor.remove({},{},track)
        verify(repository).removeRecent(eq(track))
        verify(scheduler).io()
        verify(scheduler).ui()
    }

    @Test
    fun insertsLoved(){
        val track=Track()
        whenever(repository.like(any<Track>())).thenReturn(Completable.complete())
        lovedInteractor.insert({},{},track)
        verify(repository).like(track)
        verify(scheduler).ui()
        verify(scheduler).io()
    }

    @Test
    fun insertsRecent(){
        val track=Track()
        whenever(repository.insertRecent(any<Track>())).thenReturn(Completable.complete())
        historyInteractor.insert({},{},track)
        verify(repository).insertRecent(track)
        verify(scheduler).ui()
        verify(scheduler).io()
    }
}