package com.vpaliy.mediaplayer.ui.home

import android.support.v7.widget.RecyclerView
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import butterknife.BindView

abstract class HomeFragment: BaseFragment(),HomeContract.View{

    protected lateinit var presenter: Presenter
    protected lateinit var adapter:BaseAdapter<Track>

    @BindView(R.id.list)
    lateinit var list: RecyclerView

    override fun error() {

    }

    override fun cleared() {

    }

    override fun empty() {

    }

    override fun removed(track: Track) {

    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun layoutId()= R.layout.fragment_home
}
