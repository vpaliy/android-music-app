package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v4.util.Pair
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import com.vpaliy.mediaplayer.ui.home.loved.LovedFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.support.v4.view.ViewCompat



class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        navigation.setNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.history->load(HistoryFragment())
                R.id.loved->load(LovedFragment())
                else ->false
            }
        }
        load(HistoryFragment())
    }

    private fun load(fragment: BaseFragment):Boolean{
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame,fragment)
                .commit()
        drawer.closeDrawers()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.search-> {
                val search =toolbar.findViewById(R.id.search)
                ViewCompat.setTransitionName(search, getString(R.string.search_trans))
                navigator.search(this, Pair(search, getString(R.string.search_trans)))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun inject()=FitnessSound.app().component().inject(this)
}
