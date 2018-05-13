package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v4.util.Pair
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import com.vpaliy.mediaplayer.ui.home.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.view.View
import com.vpaliy.mediaplayer.App

class HomeActivity : BaseActivity() {

  private var fragment: HomeFragment? = null

  @Suppress("DEPRECATION")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    ButterKnife.bind(this)
    setSupportActionBar(toolbar)
    bottomNavigator.setOnTabSelectListener {
      when (it) {
        R.id.history -> {
          toolbar.title = getString(R.string.history_label)
          load(HistoryFragment())
        }
        R.id.favorite -> {
          toolbar.title = getString(R.string.favorite_label)
          load(FavoriteFragment())
        }
        R.id.settings -> {
          toolbar.title = getString(R.string.settings_label)
          //TODO add settings fragment
        }
      }
    }
    load(HistoryFragment())
  }

  private fun load(fragment: HomeFragment): Boolean {
    this.fragment = fragment
    supportFragmentManager.beginTransaction()
        .replace(R.id.frame, fragment)
        .commit()
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.search -> {
        val search = toolbar.findViewById<View>(R.id.search)
        search.transitionName = getString(R.string.search_trans)
        navigator.search(this, Pair(search, getString(R.string.search_trans)))
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }
}
