package com.vpaliy.mediaplayer.presentation

import com.nhaarman.mockito_kotlin.*
import com.vpaliy.mediaplayer.domain.Repository
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.interactor.SearchTracks
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.search.SearchContract
import com.vpaliy.mediaplayer.ui.search.TrackPresenter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchTrackPresenterTest {

  private val scheduler = mock<BaseScheduler> {
    on(it.io()).thenReturn(Schedulers.trampoline())
    on(it.ui()).thenReturn(Schedulers.trampoline())
  }

  private val repository = mock<Repository>()

  @Mock lateinit var view: SearchContract.View<Track>
  @InjectMocks lateinit var interactor:SearchTracks

  private val success = argumentCaptor<(List<Track>) -> Unit>()
  private val error = argumentCaptor<(Throwable) -> Unit>()

  private val presenter by lazy(LazyThreadSafetyMode.NONE) {
    val result = TrackPresenter(interactor)
    result.attachView(view)
    return@lazy result
  }

  @Test
  fun testQueryTest() {
    whenever(repository.search(any())).thenReturn(Single.just(arrayListOf(Track())))
    presenter.query(query = "Query")
    verify(interactor).execute(success.capture(), error.capture(), any())
    success.firstValue.invoke(arrayListOf(Track()))
    verify(view).showLoading()
  }
}