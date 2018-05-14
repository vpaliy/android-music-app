package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v4.util.Pair
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.view.View
import com.vpaliy.mediaplayer.ui.home.favorite.FavoriteFragment

class HomeActivity : BaseActivity() {
  private var currentFragment: HomeFragment? = null
  private val historyFragment: HomeFragment by lazy { HistoryFragment() }
  private val favoriteFragment: HomeFragment by lazy { FavoriteFragment() }

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
          load(historyFragment)
        }
        R.id.favorite -> {
          toolbar.title = getString(R.string.favorite_label)
          load(favoriteFragment)
        }
        R.id.settings -> {
          toolbar.title = getString(R.string.settings_label)
          //TODO add settings currentFragment
        }
      }
    }
    load(HistoryFragment())
  }

  private fun load(fragment: HomeFragment): Boolean {
    this.currentFragment = fragment
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
