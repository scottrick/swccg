package com.hatfat.swccg.repo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCardSubType
import com.hatfat.swccg.data.SWCCGCardType
import com.hatfat.swccg.data.SWCCGSide
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
    /* code -> SWCCGCardType */
    private val cardTypesLiveData = MutableLiveData<Map<String, SWCCGCardType>>()
    /* code -> SWCCGCardSubType */
    private val cardSubTypesLiveData = MutableLiveData<Map<String, SWCCGCardSubType>>()
    /* code -> SWCCGSide */
    private val sidesLiveData = MutableLiveData<Map<String, SWCCGSide>>()

    val cardTypes: LiveData<Map<String, SWCCGCardType>>
        get() = cardTypesLiveData

    val cardSubTypes: LiveData<Map<String, SWCCGCardSubType>>
        get() = cardSubTypesLiveData

    val sides: LiveData<Map<String, SWCCGSide>>
        get() = sidesLiveData

    init {
        cardTypesLiveData.value = HashMap()
        cardSubTypesLiveData.value = HashMap()
        sidesLiveData.value = HashMap()

        GlobalScope.launch(Dispatchers.IO) {
            load()
        }
    }

    private suspend fun load() {
        val subtypesInputStream = resources.openRawResource(R.raw.subtypes)
        val typesInputStream = resources.openRawResource(R.raw.types)
        val sidesInputStream = resources.openRawResource(R.raw.sides)
        val typesReader = BufferedReader(InputStreamReader(typesInputStream))
        val subtypesReader = BufferedReader(InputStreamReader(subtypesInputStream))
        val sidesReader = BufferedReader(InputStreamReader(sidesInputStream))

        val types = gson.fromJson(typesReader, Array<SWCCGCardType>::class.java)
        val subtypes = gson.fromJson(subtypesReader, Array<SWCCGCardSubType>::class.java)
        val sides = gson.fromJson(sidesReader, Array<SWCCGSide>::class.java)

        val typesHashMap = HashMap<String, SWCCGCardType>()
        val subtypesHashMap = HashMap<String, SWCCGCardSubType>()
        val sidesHashMap = HashMap<String, SWCCGSide>()

        for (type in types) {
            typesHashMap.put(type.code, type)
        }

        for (subtype in subtypes) {
            subtypesHashMap.put(subtype.code, subtype)
        }

        for (side in sides) {
            sidesHashMap.put(side.code, side)
        }

        withContext(Dispatchers.Main) {
            cardTypesLiveData.value = typesHashMap
            cardSubTypesLiveData.value = subtypesHashMap
            sidesLiveData.value = sidesHashMap
        }
    }
}