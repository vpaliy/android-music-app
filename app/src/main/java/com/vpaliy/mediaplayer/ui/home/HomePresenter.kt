package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.*
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
abstract class HomePresenter (val interactor: SingleInteractor<List<Track>, Void>,
                              val clear:ClearInteractor<Track>) : Presenter {

    protected lateinit var view:View

    override fun start() {
        view.setLoading(true)
        interactor.execute(this::onSuccess,this::onError)
    }

    private fun onSuccess(result:List<Track>?){
        view.setLoading(false)
        if(result!=null) {
            when (result.isEmpty()) {
                true -> view.empty()
                else -> view.show(result)
            }
        }
    }

    protected fun onError(error:Throwable){
        error.printStackTrace()
        view.setLoading(false)
        view.error()
    }

    override fun attach(view: View) {
        this.view = view
    }

    override fun remove(track: Track) = clear.remove({view.removed(track)}, this::onError,track)

    override fun clear()=clear.clear(view::cleared,this::onError)

    override fun stop()=interactor.dispose()
}
