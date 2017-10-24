package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.ui.search.SearchContract.*
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.interactor.params.Consumer
import com.vpaliy.mediaplayer.domain.interactor.params.Response
import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.then

@ViewScope
class SearchPresenter @Inject
constructor(val search:SingleInteractor<SearchPage>):Presenter {

    private lateinit var view:View
    private var page=SearchPage(1)

    override fun query(query: String?){
        view.setLoading(true)
        page.query=query
        search.execute(Consumer(this::onSuccess,this::onError),page)
    }

    override fun more() {
        view.setLoading(true)
        page.current++
        search.execute(Consumer(this::append,this::onError),page)
    }

    override fun attachView(view: View) {
        this.view=view
    }

    private fun onSuccess(response: Response<SearchPage>){
        view.setLoading(false)
        response.result.isEmpty().then(view::empty, {view.show(response.result)})
    }

    private fun append(response: Response<SearchPage>){
        view.setLoading(false)
        val list=response.result
        list?.let { view.append(it) }
    }

    private fun onError(error:Throwable){
        error.printStackTrace()
        view.setLoading(false)
        view.error()
    }
    
    override fun stop(){}
}