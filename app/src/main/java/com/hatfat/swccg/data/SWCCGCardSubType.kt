package com.hatfat.swccg.data

import java.io.Serializable

class SWCCGCardSubType(
    val code: String,
    val name: String
) : Serializable, Comparable<SWCCGCardSubType> {
    override fun toString(): String {
        return name
    }

    override fun compareTo(other: SWCCGCardSubType): Int {
        return name.compareTo(other.name)
    }
}