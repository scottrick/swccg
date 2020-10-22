package com.hatfat.cards.filter

import com.hatfat.cards.data.SWCCGCard

class SideFilter constructor(
    val filterSide: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterSide.equals(card.side)
    }
}
