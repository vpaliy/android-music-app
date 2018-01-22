package com.vpaliy.mediaplayer.presentation

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.details.ActionsContract
import com.vpaliy.mediaplayer.ui.details.ActionsPresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ActionsPresenterTest{

    @Mock lateinit var loveInteractor:LovedTracks
    @Mock lateinit var historyInteractor:TrackHistory
    @Mock lateinit var view:ActionsContract.View
    private lateinit var presenter:ActionsPresenter
    private val success= argumentCaptor<()->Unit>()
    private val error= argumentCaptor<(Throwable)->Unit>()
    private val track= Track()

    @Before
    fun setUp(){
        presenter= ActionsPresenter(loveInteractor,historyInteractor,
                loveInteractor,historyInteractor)
        presenter.attach(view)
    }

    @Test
    fun successfullyLikes(){
        presenter.like(track)
        verify(loveInteractor).insert(success.capture(),error.capture(), any<Track>())
        success.firstValue.invoke()
        verify(view).showLiked()
    }

    @Test
    fun failsToLike(){
        presenter.like(track)
        verify(loveInteractor).insert(success.capture(),error.capture(), eq(track))
        error.firstValue.invoke(Exception())
        verify(view).error()
    }

    @Test
    fun successfullyDislikes(){
        presenter.dislike(track)
        verify(loveInteractor).remove(success.capture(),error.capture(), any<Track>())
        success.firstValue.invoke()
        verify(view).showUnliked()
    }

    @Test
    fun failsToDislike(){
        presenter.dislike(track)
        verify(loveInteractor).remove(success.capture(),error.capture(), eq(track))
        error.firstValue.invoke(Exception())
        verify(view).error()
    }

    @Test
    fun successfullyAdds(){
        presenter.add(track)
        verify(historyInteractor).insert(success.capture(),error.capture(), any<Track>())
        success.firstValue.invoke()
        verify(view).showAdded()
    }

    @Test
    fun failsToAdd(){
        presenter.add(track)
        verify(historyInteractor).insert(success.capture(),error.capture(), any<Track>())
        error.firstValue.invoke(Exception())
        verify(view).error()
    }

    @Test
    fun successfullyRemoves(){
        presenter.remove(track)
        verify(historyInteractor).remove(success.capture(),error.capture(), any<Track>())
        success.firstValue.invoke()
        verify(view).showRemoved()
    }

    @Test
    fun failsToRemove(){
        presenter.remove(track)
        verify(historyInteractor).remove(success.capture(),error.capture(), any<Track>())
        error.firstValue.invoke(Exception())
        verify(view).error()
    }
}