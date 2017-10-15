package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.search.SearchContract.*
import com.vpaliy.mediaplayer.domain.interactor.SearchInteractor
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.ui.utils.ifNullOrEmpty

@ViewScope
class SearchPresenter @Inject
constructor(private val search:SearchInteractor):Presenter {

    private lateinit var view:View

    override fun query(query: String?){
        view.setLoading(true)
        search.query(this::onSuccess,this::onError,query)
    }

    override fun more() {
        view.setLoading(true)
        search.nextPage(this::append,this::onError)
    }

    override fun attachView(view: View) {
        this.view=view
    }

    private fun onSuccess(list:List<Track>?){
        view.setLoading(false)
        list.ifNullOrEmpty(view::empty,{view.show(list!!)})
    }

    private fun append(list:List<Track>?){
        view.setLoading(false)
        list?.let { view.append(it) }
    }

    private fun onError(error:Throwable){
        error.printStackTrace()
        view.setLoading(false)
        view.error()
    }
    
    override fun stop()=search.dispose()
}