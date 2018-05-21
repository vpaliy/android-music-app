package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.view.View
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.favorite.FavoriteFragment
import com.vpaliy.mediaplayer.ui.settings.SettingsFragment

class HomeActivity : BaseActivity() {
  private var currentFragment: Fragment? = null
  private val historyFragment: BaseFragment by lazy { HistoryFragment() }
  private val favoriteFragment: BaseFragment by lazy { FavoriteFragment() }
  private val settingsFragment: Fragment by lazy { SettingsFragment() }

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
          toolbar.menu.clear()
          load(settingsFragment)
        }
      }
    }
    load(historyFragment)
  }

  private fun load(fragment: Fragment): Boolean {
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
