package com.vpaliy.mediaplayer.presentation

import com.nhaarman.mockito_kotlin.*
import com.vpaliy.mediaplayer.FakeDataProvider
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.home.favorite.FavoritePresenter
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.junit.Test
import org.junit.Before

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest{

    @Mock lateinit var view:HomeContract.View
    @Mock lateinit var historyInteractor: SingleInteractor<TrackType, List<Track>>
    @InjectMocks private lateinit var favoritePresenter: FavoritePresenter
    @InjectMocks private lateinit var historyPresenter:HistoryPresenter

    private val success= argumentCaptor<(List<Track>?)->Unit>()
    private val error= argumentCaptor<(Throwable)->Unit>()

    @Before
    fun setUp(){
        historyPresenter.attach(view)
        favoritePresenter.attach(view)
    }

    @Test
    fun startsLoadingShowsTracks(){
        favoritePresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        success.firstValue.invoke(FakeDataProvider.buildList(1,{Track()}))
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        success.firstValue.invoke(FakeDataProvider.buildList(1,{Track()}))
        verify(view, times(2)).setLoading(false)
        verify(view, times(2)).showTracks(any<List<Track>>())
    }

    @Test
    fun startsLoadingShowsEmpty(){
        favoritePresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        success.firstValue.invoke(arrayListOf())
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        success.firstValue.invoke(arrayListOf())
        verify(view, times(2)).setLoading(false)
        verify(view, times(2)).showEmpty()
    }

    @Test
    fun startsLoadingShowsNothing(){
        favoritePresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
        success.firstValue.invoke(null)
        verify(historyInteractor).execute(success.capture(),error.capture(),eq(null))
        success.firstValue.invoke(null)
        verify(view, times(2)).setLoading(false)
    }

    @Test
    fun startsLoadingShowsError(){
        favoritePresenter.start()
        historyPresenter.start()
        verify(view, times(2)).setLoading(true)
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
        favoritePresenter.remove(track)
        historyPresenter.remove(track)
        completed.firstValue.invoke()
        //verify(historyInteractor).remove(completed.capture(),error.capture(),eq(track))
        completed.firstValue.invoke()
        verify(view,times(2)).removed(eq(track))
    }

    @Test
    fun removesTrackShowsError(){
        val track=Track()
        favoritePresenter.remove(track)
        historyPresenter.remove(track)
        error.firstValue.invoke(Exception())
       // verify(historyInteractor).remove(any<()->Unit>(),error.capture(),eq(track))
        error.firstValue.invoke(Exception())
        verify(view,times(2)).error()
    }

    @Test
    fun clearsTracksShowsCompleted(){
        val completed= argumentCaptor<()->Unit>()
        favoritePresenter.clearAll()
        historyPresenter.clearAll()
        //verify(lovedInteractor).clearAll(completed.capture(),error.capture())
        completed.firstValue.invoke()
        //verify(historyInteractor).clearAll(completed.capture(),error.capture())
        completed.firstValue.invoke()
        verify(view,times(2)).showCleared()
    }

    @Test
    fun clearsTracksShowsError(){
        favoritePresenter.clearAll()
        historyPresenter.clearAll()
        //verify(lovedInteractor).clearAll(any<()->Unit>(),error.capture())
        error.firstValue.invoke(Exception())
        //
        error.firstValue.invoke(Exception())
        verify(view,times(2)).error()
    }
}