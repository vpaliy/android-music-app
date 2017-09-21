package com.vpaliy.mediaplayer.ui.details

import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.gson.reflect.TypeToken
import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.utils.BundleUtils
import com.vpaliy.mediaplayer.ui.utils.Constants
import kotlinx.android.synthetic.main.fragment_actions.*
import javax.inject.Inject

class ActionsActivity:BaseActivity(),ActionsContract.View{

    private lateinit var presenter: ActionsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_actions)
        val bundle=savedInstanceState?:intent.extras
        bundle?.let {
            val track= BundleUtils.fetchHeavyObject<Track>(object: TypeToken<Track>() {}.type, it, Constants.EXTRA_TRACK)
            track?.let {
                loadTrack(it)
                like.setOnClickListener {_->
                    if(it.isLiked) presenter.like(it)
                    else presenter.dislike(it)
                }
                history.setOnClickListener {_->
                    if(it.isSaved) presenter.add(it)
                    else presenter.remove(it)
                }

            }
        }
    }

    override fun added()=showMessage(R.string.removed_message)
    override fun removed()=showMessage(R.string.removed_message)
    override fun liked()=showMessage(R.string.removed_message)
    override fun disliked()=showMessage(R.string.removed_message)
    override fun error()=showMessage(R.string.removed_message)

    private fun showMessage(resource:Int){}

    private fun loadTrack(track:Track){
        Glide.with(this)
                .load(track.artworkUrl)
                .placeholder(R.drawable.placeholder)
                .animate(R.anim.fade_in)
                .into(art)
        artist.text=track.artist
        song.text=track.title
    }

    override fun inject()=DaggerViewComponent.builder()
            .presenterModule(PresenterModule())
            .applicationComponent(FitnessSound.app().component())
            .build().inject(this)

    @Inject
    override fun attach(presenter: ActionsContract.Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }
}
