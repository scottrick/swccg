package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

class CardSubTypeFilter constructor(
    val filterCardSubType: String
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return card.front.subType?.contains(filterCardSubType) ?: false
    }
}
