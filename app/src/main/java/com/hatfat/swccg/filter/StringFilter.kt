package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard

class StringFilter constructor(
    val filterText: String,
    val filterTitle: Boolean,
    val filterGametext: Boolean,
    val filterLore: Boolean
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        if (filterTitle && card.front.title?.contains(filterText, true) == true) {
            return true
        }

        if (filterGametext && card.front.gametext?.contains(filterText, true) == true) {
            return true
        }

        if (filterLore && card.front.lore?.contains(filterText, true) == true) {
            return true
        }

        return false
    }
}
