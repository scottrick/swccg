package com.hatfat.cards.filter

import com.hatfat.cards.data.SWCCGCard
import com.hatfat.cards.data.SWCCGFormat

class FormatFilter constructor(
    val filterFormat: SWCCGFormat
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterFormat.all_sets_allowed || filterFormat.allowed_sets.contains(card.set)
    }
}
