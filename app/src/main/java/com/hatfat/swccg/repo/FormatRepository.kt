package com.hatfat.swccg.repo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormatRepository @Inject constructor(
    private val resources: Resources,
    private val gson: Gson
) : SWCCGRepository() {
    private val formatLiveData = MutableLiveData<Map<String, SWCCGFormat>>()

    val formats: LiveData<Map<String, SWCCGFormat>>
        get() = formatLiveData

    init {
        formatLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val inputStream = resources.openRawResource(R.raw.formats)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val formats = gson.fromJson(reader, Array<SWCCGFormat>::class.java)

        val hashMap = HashMap<String, SWCCGFormat>()
        for (format in formats) {
            hashMap.put(format.code, format)
        }

        withContext(Dispatchers.Main) {
            formatLiveData.value = hashMap
            loadedLiveData.value = true
        }
    }
}
