package com.vpaliy.mediaplayer.presentation

import com.nhaarman.mockito_kotlin.*
import com.vpaliy.mediaplayer.FakeDataProvider
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.home.favorite.LovedPresenter
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.junit.Test
import org.junit.Before

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest{

    @Mock lateinit var view:HomeContract.View
    @Mock lateinit var lovedInteractor: LovedTracks
    @Mock lateinit var historyInteractor: TrackHistory
    @InjectMocks lateinit var lovedPresenter:LovedPresenter
    @InjectMocks lateinit var historyPresenter:HistoryPresenter

    private val success= argumentCaptor<(List<Track>?)->Unit>()
    private val error= argumentCaptor<(Throwable)->Unit>()

    @Before
    fun setUp(){
        historyPresenter.attach(view)
        lovedPresenter.attach(view)
    }

    @Test
    fun startsLoadingShowsTracks(){
        lovedPresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        verify(lovedInteractor).execute(success.capture(),error.capture(), eq(null))
        success.firstValue.invoke(FakeDataProvider.buildList(1,{Track()}))
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        success.firstValue.invoke(FakeDataProvider.buildList(1,{Track()}))
        verify(view, times(2)).setLoading(false)
        verify(view, times(2)).show(any<List<Track>>())
    }

    @Test
    fun startsLoadingShowsEmpty(){
        lovedPresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        verify(lovedInteractor).execute(success.capture(),error.capture(), eq(null))
        success.firstValue.invoke(arrayListOf())
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        success.firstValue.invoke(arrayListOf())
        verify(view, times(2)).setLoading(false)
        verify(view, times(2)).empty()
    }

    @Test
    fun startsLoadingShowsNothing(){
        lovedPresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        verify(lovedInteractor).execute(success.capture(),error.capture(), eq(null))
        success.firstValue.invoke(null)
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        success.firstValue.invoke(null)
        verify(view, times(2)).setLoading(false)
    }

    @Test
    fun startsLoadingShowsError(){
        lovedPresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        verify(lovedInteractor).execute(success.capture(),error.capture(), eq(null))
        error.firstValue.invoke(Exception())
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        error.firstValue.invoke(Exception())
        verify(view, times(2)).setLoading(false)
        verify(view, times(2)).error()
    }

    @Test
    fun removesTrackShowsCompleted(){
        val track=Track()
        val completed= argumentCaptor<()->Unit>()
        lovedPresenter.remove(track)
        historyPresenter.remove(track)
        verify(lovedInteractor).remove(completed.capture(),error.capture(),eq(track))
        completed.firstValue.invoke()
        verify(historyInteractor).remove(completed.capture(),error.capture(),eq(track))
        completed.firstValue.invoke()
        verify(view,times(2)).removed(eq(track))
    }

    @Test
    fun removesTrackShowsError(){
        val track=Track()
        lovedPresenter.remove(track)
        historyPresenter.remove(track)
        verify(lovedInteractor).remove(any<()->Unit>(),error.capture(),eq(track))
        error.firstValue.invoke(Exception())
        verify(historyInteractor).remove(any<()->Unit>(),error.capture(),eq(track))
        error.firstValue.invoke(Exception())
        verify(view,times(2)).error()
    }

    @Test
    fun clearsTracksShowsCompleted(){
        val completed= argumentCaptor<()->Unit>()
        lovedPresenter.clear()
        historyPresenter.clear()
        verify(lovedInteractor).clear(completed.capture(),error.capture())
        completed.firstValue.invoke()
        verify(historyInteractor).clear(completed.capture(),error.capture())
        completed.firstValue.invoke()
        verify(view,times(2)).cleared()
    }

    @Test
    fun clearsTracksShowsError(){
        lovedPresenter.clear()
        historyPresenter.clear()
        verify(lovedInteractor).clear(any<()->Unit>(),error.capture())
        error.firstValue.invoke(Exception())
        verify(historyInteractor).clear(any<()->Unit>(),error.capture())
        error.firstValue.invoke(Exception())
        verify(view,times(2)).error()
    }
}