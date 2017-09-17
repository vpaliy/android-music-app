package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.view.View
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import kotlinx.android.synthetic.main.fragment_home.*

abstract class HomeFragment: BaseFragment(),HomeContract.View{

    protected lateinit var presenter: Presenter
    protected lateinit var adapter:BaseAdapter<Track>

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.let {
            adapter=TrackAdapter(context,{navigator.navigate(activity,it)})
            list.adapter=adapter
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun error() {
        empty.visibility=View.VISIBLE
    }

    override fun empty(){
        empty.visibility=View.VISIBLE
    }

    override fun setLoading(isLoading: Boolean){
        progress.visibility=if(isLoading) View.VISIBLE else View.GONE
    }

    override fun show(list: List<Track>)=adapter.set(list.toMutableList())

    override fun layoutId()= R.layout.fragment_home

    override fun removed(track: Track)= showMessage(R.string.removed_message)

    override fun cleared()= showMessage(R.string.cleared_message)
}
