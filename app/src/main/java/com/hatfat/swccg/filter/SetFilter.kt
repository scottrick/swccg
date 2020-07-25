package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

class SetFilter constructor(
    val filterSet: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterSet.equals(card.set)
    }
}
