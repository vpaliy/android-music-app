package com.vpaliy.mediaplayer.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.vpaliy.mediaplayer.ui.search.SearchActivity
import javax.inject.Inject
import javax.inject.Singleton
import android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import android.support.v4.util.Pair
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.details.ActionsActivity
import com.vpaliy.mediaplayer.ui.player.PlayerActivity
import com.vpaliy.mediaplayer.ui.utils.Packer


@Singleton
class Navigator @Inject constructor(){

    fun navigate(activity: Activity, bundle: Bundle){
        val intent= Intent(activity,PlayerActivity::class.java)
        intent.putExtras(bundle)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.slide_out_up,0)
    }

    fun search(activity:Activity, pair:Pair<View,String>){
        val intent= Intent(activity,SearchActivity::class.java)
        val optionsCompat = makeSceneTransitionAnimation(activity, pair)
        activity.startActivity(intent, optionsCompat.toBundle())
    }

    fun actions(activity: Activity, bundle: Bundle){
        val intent= Intent(activity,ActionsActivity::class.java)
        intent.putExtras(bundle)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.slide_out_up,0)
    }
}
