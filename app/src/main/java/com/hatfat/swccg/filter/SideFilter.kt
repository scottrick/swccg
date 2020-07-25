package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

class SideFilter constructor(
    val filterSide: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterSide.equals(card.side)
    }
}
