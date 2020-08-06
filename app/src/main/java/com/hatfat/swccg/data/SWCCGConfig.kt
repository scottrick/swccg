package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGConfig(
    /* playstore images are greatly reduced in resolution */
    val shouldUsePlaystoreImages: Boolean = false
) : Serializable
