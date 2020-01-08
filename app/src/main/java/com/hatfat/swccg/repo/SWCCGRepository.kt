package com.hatfat.swccg.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class SWCCGRepository {
    protected val loadedLiveData = MutableLiveData<Boolean>()

    val loaded: LiveData<Boolean>
        get() = loadedLiveData

    init {
        loadedLiveData.value = false
    }
}