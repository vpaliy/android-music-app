package com.vpaliy.mediaplayer.ui.utils

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

abstract class OnReachBottomListener(private val layoutManager:RecyclerView.LayoutManager,
                                     private val dataLoading: SwipeRefreshLayout?=null) :
                    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        // bail out if scrolling upward or already loading data
        val firstVisibleItem = fetchFirstVisibleItemPosition()
        if (dy < 0 || dataLoading != null && dataLoading.isRefreshing || firstVisibleItem == -1) return

        val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = layoutManager.itemCount

        if (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {
            onLoadMore()
        }
    }

    private fun fetchFirstVisibleItemPosition(): Int {
        if (layoutManager is LinearLayoutManager) {
            return LinearLayoutManager::class.java.cast(layoutManager).findFirstVisibleItemPosition()
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val manager = StaggeredGridLayoutManager::class.java.cast(layoutManager)
            val result = manager.findFirstVisibleItemPositions(null)
            if (result != null && result.isNotEmpty()) {
                return result[0]
            }
        }
        return -1
    }

    abstract fun onLoadMore()

    companion object {
        private val VISIBLE_THRESHOLD = 5
    }
}