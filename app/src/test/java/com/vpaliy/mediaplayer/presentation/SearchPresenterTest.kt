package com.vpaliy.mediaplayer.presentation

import com.nhaarman.mockito_kotlin.*
import com.vpaliy.mediaplayer.FakeDataProvider
import com.vpaliy.mediaplayer.domain.interactor.SearchTracks
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.search.SearchContract
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchPresenterTest {

  /*  @Mock lateinit var interactor:SearchTracks
    @Mock lateinit var view: SearchContract.View
    @InjectMocks lateinit var presenter:SearchPresenter

    private val success= argumentCaptor<(List<Track>)->Unit>()
    private val error= argumentCaptor<(Throwable)->Unit>()

    @Before
    fun setUp()=presenter.attachView(view)

    @Test
    fun makesSuccessfulQuery(){
        presenter.query("Q")
        verify(view).setLoading(true)
        verify(interactor).query(success.capture(),error.capture(),any<String>())
        success.firstValue.invoke(FakeDataProvider.buildList(1,{Track()}))
        verify(view).showResult(any())
        verify(view).setLoading(false)
    }

    @Test
    fun failsOnQuery(){
        presenter.query("Q")
        verify(view).setLoading(true)
        verify(interactor).query(success.capture(),error.capture(), any<String>())
        error.firstValue.invoke(Exception())
        verify(view).error()
        verify(view).setLoading(false)
    }

    @Test
    fun showsEmptyOnQuery(){
        presenter.query("Q")
        verify(view).setLoading(true)
        verify(interactor).query(success.capture(),error.capture(), any<String>())
        success.firstValue.invoke(emptyList())
        verify(view).empty()
        verify(view).setLoading(false)
    }

    @Test
    fun makesSuccessfulMoreRequest(){
        presenter.more()
        verify(view).setLoading(true)
        verify(interactor).nextPage(success.capture(),error.capture())
        success.firstValue.invoke(FakeDataProvider.buildList(1,{Track()}))
        verify(view).appendResult(any<List<Track>>())
        verify(view).setLoading(false)
    }

    @Test
    fun failsOnMoreRequest(){
        presenter.more()
        verify(view).setLoading(true)
        verify(interactor).nextPage(success.capture(),error.capture())
        error.firstValue.invoke(Exception())
        verify(view).error()
        verify(view).setLoading(false)
    }   */
}