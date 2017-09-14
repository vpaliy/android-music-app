package com.vpaliy.mediaplayer.ui.home.history

import android.support.v7.widget.RecyclerView
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import javax.inject.Inject
import butterknife.BindView

class HistoryFragment : BaseFragment(), HomeContract.View {

    private lateinit var presenter:Presenter
    private lateinit var adapter:BaseAdapter<Track>

    @BindView(R.id.list)
    lateinit var list:RecyclerView


    override fun show(list: List<Track>) {
        adapter.set(list.toMutableList())
    }

    override fun error() {

    }

    override fun empty() {

    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    @Inject
    override fun attach(presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }
}
