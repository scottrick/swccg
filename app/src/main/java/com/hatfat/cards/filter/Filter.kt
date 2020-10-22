package com.hatfat.cards.filter

import com.hatfat.cards.data.SWCCGCard
import java.io.Serializable

abstract class Filter : Serializable {
    abstract fun filter(card: SWCCGCard): Boolean
}
