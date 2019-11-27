package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardSubType

class CardSubTypeFilter constructor(
    val filterCardSubType: SWCCGCardSubType
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterCardSubType.code.equals(card.subtype_code)
    }
}