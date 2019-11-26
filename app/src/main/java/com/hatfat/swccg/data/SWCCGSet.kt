package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGSet(
    val code: String,
    val cycle_code: String = "",
    val date_release: String = "",
    val name: String,
    val position: Int = -1,
    val size: Int = -1
) : Serializable, Comparable<SWCCGSet> {
    override fun toString(): String {
        return name
    }

    override fun compareTo(other: SWCCGSet): Int {
        return name.compareTo(other.name)
    }
}
