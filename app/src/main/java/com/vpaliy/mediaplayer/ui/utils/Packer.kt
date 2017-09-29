package com.vpaliy.mediaplayer.ui.utils

import android.os.Bundle
import android.support.v4.util.Pair
import android.view.View

@Suppress("ArrayInDataClass")
data class Packer (val bundle:Bundle, val pairs:Array<Pair<out View,String>>)