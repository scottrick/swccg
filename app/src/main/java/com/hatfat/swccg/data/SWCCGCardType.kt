package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGCardType(
    val code: String,
    val name: String
) : Serializable, Comparable<SWCCGCardType> {
    override fun toString(): String {
        return name
    }

    override fun compareTo(other: SWCCGCardType): Int {
        return name.compareTo(other.name)
    }
}