package com.hatfat.cards.filter

import com.hatfat.cards.data.SWCCGCard

class CardTypeFilter constructor(
    val filterCardType: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return card.front.type?.contains(filterCardType) ?: false
    }
}
