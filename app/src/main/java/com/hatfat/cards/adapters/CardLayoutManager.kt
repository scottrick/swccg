package com.hatfat.cards.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardLayoutManager : RecyclerView.LayoutManager() {

    private val mFirstPosition = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun scrollToPosition(position: Int) {

    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        val parentBottom = height - paddingBottom
        val parentRight = width - paddingEnd

        val oldTopView: View? = if (childCount > 0) getChildAt(0) else null
        var oldTop = paddingTop
        if (oldTopView != null) {
            oldTop = oldTopView.top
        }

        detachAndScrapAttachedViews(recycler!!)

        var top = paddingTop
        var left = paddingLeft
        val count = state!!.getItemCount()
        var bottom: Int

        val cardWidth = 62
        val cardHeight = 87

        var i = 0
        while (mFirstPosition + i < count && left < parentRight) {
            val v: View = recycler.getViewForPosition(mFirstPosition + i)
            addView(v, i)
            measureChildWithMargins(v, 0, 0)
            layoutDecorated(v, left, top, left+cardWidth, top+cardHeight)
            i++
            left+=cardWidth
        }
    }
}