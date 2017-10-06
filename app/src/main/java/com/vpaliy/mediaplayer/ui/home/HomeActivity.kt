package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v4.util.Pair
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import com.vpaliy.mediaplayer.ui.home.loved.LovedFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.support.v7.app.ActionBarDrawerToggle

class HomeActivity : BaseActivity() {

    private var fragment:HomeFragment?=null
    private val toggle by lazy {
        ActionBarDrawerToggle(this,drawer,toolbar,0,0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        drawer.setDrawerListener(toggle)
        navigation.setNavigationItemSelectedListener { item ->
            item.isChecked=true
            when(item.itemId){
                R.id.history->{
                    toolbar.title=getString(R.string.history_label)
                    load(HistoryFragment())
                }
                R.id.loved->{
                    toolbar.title=getString(R.string.liked_label)
                    load(LovedFragment())
                }
                else ->false
            }
        }
        navigation.setCheckedItem(R.id.history)
        load(HistoryFragment())
    }

    private fun load(fragment: HomeFragment):Boolean{
        this.fragment=fragment
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

    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.search-> {
                val search =toolbar.findViewById(R.id.search)
                search.transitionName=getString(R.string.search_trans)
                navigator.search(this, Pair(search, getString(R.string.search_trans)))
                return true
            }
        }
        return toggle.onOptionsItemSelected(item)
    }

    override fun inject()=FitnessSound.app().component().inject(this)
}
