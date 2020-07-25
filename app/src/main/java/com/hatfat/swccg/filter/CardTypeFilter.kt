package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

class CardTypeFilter constructor(
    val filterCardType: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return card.front.type?.contains(filterCardType) ?: false
    }
}
