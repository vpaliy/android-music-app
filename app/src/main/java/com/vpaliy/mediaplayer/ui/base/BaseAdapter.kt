package com.vpaliy.mediaplayer.ui.base

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import java.util.ArrayList
import android.view.ViewGroup

abstract class BaseAdapter<T>(context: Context,
                              protected var rxBus: RxBus) :
        RecyclerView.Adapter<BaseAdapter<T>.GenericViewHolder>() {

    protected var data:MutableList<T> =ArrayList<T>()
            set(value) = notifyDataSetChanged()

    protected val inflater: LayoutInflater= LayoutInflater.from(context)

    abstract inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBindData()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    protected fun inflate(@LayoutRes id: Int, container: ViewGroup): View {
        return inflater.inflate(id, container, false)
    }

    protected fun at(index: Int): T {
        return data[index]
    }

    fun appendData(data: List<T>) {
        val size = itemCount
        this.data.addAll(data)
        notifyItemRangeInserted(size, itemCount)
    }

    fun addItem(item: T): BaseAdapter<T> {
        val size = itemCount
        data.add(item)
        notifyItemRangeInserted(size, itemCount)
        return this
    }
}