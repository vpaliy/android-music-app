package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.*

abstract class HomePresenter (val interactor: SingleInteractor<List<Track>, Void>) : Presenter {

    private lateinit var view:View

    override fun start() {
        interactor.execute(this::onSuccess,this::onError)
    }

    private fun onSuccess(result:List<Track>){
        when(result.isEmpty()){
            true->view.empty()
            else->view.show(result)
        }
    }

    private fun onError(error:Throwable){
        error.printStackTrace()
        view.error()
    }

    override fun stop() {
        interactor.dispose()
    }

    override fun attach(view: View) {
        this.view=view
    }
}
