package com.vpaliy.mediaplayer.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder
import android.view.ViewGroup
import javax.inject.Inject
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes

abstract class BaseFragment : Fragment() {

    private var unbinder: Unbinder? = null

    @Inject
    protected lateinit var rxBus: RxBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        initializeDependencies()
    }

    fun bind(root: View) {
        unbinder = ButterKnife.bind(this, root)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null) {
            unbinder!!.unbind()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(layoutId(), container, false)
        bind(view)
        return view
    }

    protected fun showMessage(@StringRes res: Int) {

    }

    @LayoutRes abstract fun layoutId(): Int

    fun initializeDependencies() {}
}