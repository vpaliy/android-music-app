package com.vpaliy.mediaplayer.ui.details

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.reflect.TypeToken
import com.vpaliy.mediaplayer.FlashApp
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_actions.*
import javax.inject.Inject
import android.content.Intent
import com.vpaliy.mediaplayer.ui.utils.*


class ActionsActivity:BaseActivity(),ActionsContract.View{

    private lateinit var presenter: ActionsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_actions)
        val bundle=savedInstanceState?:intent.extras
        container.setOnClickListener{supportFinishAfterTransition()}
        bundle?.let {
            val track= BundleUtils.fetchHeavyObject<Track>(object: TypeToken<Track>() {}.type, it, Constants.EXTRA_TRACK)
            track?.let {
                loadTrack(it)
                like.assignTextIf(!it.isLiked,R.string.like_action,R.string.unlike_action)
                history.assignTextIf(!it.isSaved,R.string.add_action,R.string.remove_action)
                like.click {
                    it.executeIf(it.isLiked,{presenter.like(it)}, {presenter.dislike(it)})
                }
                history.click {
                    it.executeIf(!it.isSaved,{presenter.add(it)}, {presenter.remove(it)})
                }
                share.click {
                    val intent = Intent(Intent.ACTION_SEND)
                    val message=getString(R.string.intro_share_message)+track.title+
                            getString(R.string.by_label)+track.artist
                    intent.putExtra(Intent.EXTRA_TEXT,message)
                    intent.type = "text/plain"
                    startActivity(Intent.createChooser(intent, getString(R.string.choose_to_share_text)))
                }

            }
        }
    }

    private fun animateText(text:TextView,string:String){
        text.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(100)
                .setListener(object:AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        text.text=string
                        text.alpha=1f
                        text.animate()
                                .scaleY(1f)
                                .scaleX(1f)
                                .setDuration(150)
                                .setListener(null)
                                .setInterpolator(OvershootInterpolator(2f))
                                .start()
                    }
                }).start()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,R.anim.slide_out_down)
    }

    override fun added()=
            animateText(history,getString(R.string.remove_action))

    override fun removed()=
            animateText(history,getString(R.string.add_action))

    override fun liked()=
            animateText(like,getString(R.string.unlike_action))

    override fun disliked() =
            animateText(like,getString(R.string.like_action))

    override fun error()=
            Snackbar.make(container,R.string.cleared_message,2000).show()

    @SuppressLint("SetTextI18n")
    private fun loadTrack(track:Track){
        Glide.with(this)
                .load(track.artworkUrl)
                .placeholder(R.drawable.placeholder)
                .animate(R.anim.fade_in)
                .into(art)
        artist.text=track.artist
        song.text=track.title
        duration.text="\u2022 ${track.formatedDuration}"
    }

    override fun inject()=DaggerViewComponent.builder()
            .presenterModule(PresenterModule())
            .applicationComponent(FlashApp.app().component())
            .build().inject(this)

    @Inject
    override fun attach(presenter: ActionsContract.Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }
}
