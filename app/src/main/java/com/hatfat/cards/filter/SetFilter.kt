package com.hatfat.cards.filter

import com.hatfat.cards.data.SWCCGCard

class SetFilter constructor(
    val filterSet: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterSet.equals(card.set)
    }
}
