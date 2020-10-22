package com.hatfat.cards.filter

import com.hatfat.cards.data.SWCCGCard

class CardSubTypeFilter constructor(
    val filterCardSubType: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return card.front.subType?.contains(filterCardSubType) ?: false
    }
}
