package com.hatfat.swccg.repo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaDataRepository @Inject constructor(
    private val resources: Resources,
    private val gson: Gson
) {
    /* maps code -> SWCCGCardType */
    private val typesLiveData = MutableLiveData<Map<String, SWCCGCardType>>()

    val types: LiveData<Map<String, SWCCGCardType>>
        get() = typesLiveData

    init {
        typesLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val inputStream = resources.openRawResource(R.raw.types)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val types = gson.fromJson(reader, Array<SWCCGCardType>::class.java)

        val hashMap = HashMap<String, SWCCGCardType>()
        for (type in types) {
            hashMap.put(type.code, type)
        }

        withContext(Dispatchers.Main) {
            typesLiveData.value = hashMap
        }
    }
}