package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGSide(
    val code: String,
    val name: String
) : Serializable, Comparable<SWCCGSide> {
    override fun toString(): String {
        return name
    }

    override fun compareTo(other: SWCCGSide): Int {
        return name.compareTo(other.name)
    }
}
