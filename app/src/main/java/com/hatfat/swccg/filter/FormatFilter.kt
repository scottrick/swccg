package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGFormat

class FormatFilter constructor(
    val filterFormat: SWCCGFormat
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterFormat.all_sets_allowed || filterFormat.allowed_sets.contains(card.set)
    }
}
