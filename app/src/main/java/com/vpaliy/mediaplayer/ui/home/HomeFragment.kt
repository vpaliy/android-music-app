package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.*
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
        setHasOptionsMenu(true)
        view?.let {
            adapter=TrackAdapter(context,{navigator.navigate(activity,it)},
                    {navigator.actions(activity,it)})
            list.adapter=adapter
        }
    }

    fun adjustPosition(position:Int){
        list.scrollToPosition(position)
        postponeEnterTransition()
        list.viewTreeObserver.addOnPreDrawListener(object:ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                list.viewTreeObserver.removeOnPreDrawListener(this)
                list.requestLayout()
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.home,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.delete){
            AlertDialog.Builder(context)
                    .setMessage(alertMessage())
                    .setPositiveButton("Yes",{_,_->presenter.clear()})
                    .setNegativeButton("No",{dialog,_->dialog.dismiss()})
                    .show()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        setMenuVisibility(false)
        empty.visibility=View.VISIBLE
    }

    override fun empty(){
        setMenuVisibility(false)
        empty.visibility=View.VISIBLE
    }

    override fun setLoading(isLoading: Boolean){
        progress.visibility=if(isLoading) View.VISIBLE else View.GONE
    }

    abstract fun alertMessage():String

    override fun show(list: List<Track>)=adapter.set(list.toMutableList())

    override fun layoutId()= R.layout.fragment_home

    override fun removed(track: Track)= showMessage(R.string.removed_message)

    override fun cleared()= showMessage(R.string.cleared_message)
}
