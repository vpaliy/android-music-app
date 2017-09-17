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
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_home.*

abstract class BaseFragment : Fragment() {

    private var unbinder: Unbinder? = null

    @Inject
    protected lateinit var navigator:Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        inject()
    }

    fun bind(root: View) {
        unbinder = ButterKnife.bind(this, root)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder?.unbind()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(layoutId(), container, false)
        bind(view)
        return view
    }

    @LayoutRes abstract fun layoutId(): Int

    protected open fun inject() {}

    protected fun showMessage(@StringRes res: Int)=Snackbar.make(rootView,res,300).show()
}