package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

class StringFilter constructor(
    val filterText: String,
    val filterTitle: Boolean,
    val filterGametext: Boolean,
    val filterLore: Boolean
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        if (filterTitle && card.name.contains(filterText, true)) {
            return true
        }

        if (filterGametext && card.gametext.contains(filterText, true)) {
            return true
        }

        if (filterLore && card.lore.contains(filterText, true)) {
            return true
        }

        return false
    }
}