package com.cairocart.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import com.cairocart.data.remote.model.CatModel


sealed class TreeScreenEffects {
    data class OpenDetails(val treeItem: CatModel) : TreeScreenEffects()
}

fun View.setVisibility(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.setMargins(
    start: Int = 0,
    top: Int = 0,
    end: Int = 0,
    bottom: Int = 0
) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = start
        params.topMargin = top
        params.marginEnd = end
        params.bottomMargin = bottom
    }
}


fun Int.dp(context: Context): Float = dpToPx(context, this)

fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp: Int): Float {
    val r = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        r.displayMetrics
    )
}