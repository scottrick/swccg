package com.hatfat.swccg.repo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetRepository @Inject constructor(
    private val resources: Resources,
    private val gson: Gson
) {
    private val setLiveData = MutableLiveData<Map<String, SWCCGSet>>()

    val sets: LiveData<Map<String, SWCCGSet>>
        get() = setLiveData

    init {
        setLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val inputStream = resources.openRawResource(R.raw.sets)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val sets = gson.fromJson(reader, Array<SWCCGSet>::class.java)

        val hashMap = HashMap<String, SWCCGSet>()
        for (set in sets) {
            hashMap.put(set.code, set)
        }

        withContext(Dispatchers.Main) {
            setLiveData.value = hashMap
        }
    }
}