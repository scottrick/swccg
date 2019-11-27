package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardType

class CardTypeFilter constructor(
val filterCardType: SWCCGCardType
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterCardType.code.equals(card.type_code)
    }
}