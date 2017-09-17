package com.vpaliy.mediaplayer.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.vpaliy.mediaplayer.R

class RoundedImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : AppCompatImageView(context, attrs, defStyle) {

    var radius = 18.0f
    private var path=Path()
    private lateinit var rect: RectF

    init {
        attrs?.let {
            val array = getContext().obtainStyledAttributes(it, R.styleable.RoundedImageView)
            radius=array.getFloat(R.styleable.RoundedImageView_radius,18f)
            array.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}