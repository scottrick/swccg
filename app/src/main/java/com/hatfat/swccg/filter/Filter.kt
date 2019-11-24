package com.hatfat.swccg.filter

import com.hatfat.swccg.data.SWCCGCard
import java.io.Serializable

abstract class Filter : Serializable {
    abstract fun filter(card: SWCCGCard): Boolean
}