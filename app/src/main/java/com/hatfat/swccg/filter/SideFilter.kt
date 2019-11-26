package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGSide

class SideFilter constructor(
    val filterSide: SWCCGSide
) : Filter() {
    override fun filter(card: SWCCGCard): Boolean {
        return filterSide.code.equals(card.side_code)
    }


}