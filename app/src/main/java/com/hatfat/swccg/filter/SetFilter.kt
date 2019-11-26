package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGSet

class SetFilter constructor(
    val filterSet: SWCCGSet
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterSet.code.equals(card.set_code)
    }
}