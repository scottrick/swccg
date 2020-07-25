package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGCardFace(
    val title: String?,
    val imageUrl: String?,
    val type: String?,
    val subType: String?,
    val uniqueness: String?,
    val destiny: String?,
    val power: String?,
    val ability: String?,
    val deploy: String?,
    val forfeit: String?,
    val icons: List<String>?,
    val characteristics: List<String>?,
    val gametext: String?,
    val lore: String?
) : Serializable, Comparable<SWCCGCardFace> {
    override fun compareTo(other: SWCCGCardFace): Int {

        if (title.isNullOrBlank() && other.title.isNullOrBlank()) {
            return 0;
        }

        if (title.isNullOrBlank()) {
            return -1
        }

        if (other.title.isNullOrBlank()) {
            return 1
        }

        return title.compareTo(other.title)
    }
}
