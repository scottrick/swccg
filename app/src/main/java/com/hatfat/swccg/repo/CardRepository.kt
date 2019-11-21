package com.hatfat.swccg.repo

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hatfat.swccg.app.SWCCGApplication
import com.hatfat.swccg.data.SWCCGCard
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
class CardRepository @Inject constructor(
    private val application: SWCCGApplication,
    private val resources: Resources,
    private val gson: Gson,
    setRepository: SetRepository
) {
    private val cardLiveData = MutableLiveData<Map<String, SWCCGCard>>()

    val cards: LiveData<Map<String, SWCCGCard>>
        get() = cardLiveData

    init {
        cardLiveData.value = HashMap()

        setRepository.sets.observeForever {
            it?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    load(it.values)
                }
            }
        }
    }

    private suspend fun load(sets: Collection<SWCCGSet>) {
        val hashMap = HashMap<String, SWCCGCard>()

        for (set in sets) {
            val inputStream = resources.openRawResource(
                resources.getIdentifier(
                    set.code,
                    "raw",
                    application.packageName
                )
            )
            val reader = BufferedReader(InputStreamReader(inputStream))

            val cards = gson.fromJson(reader, Array<SWCCGCard>::class.java)

            for (card in cards) {
                hashMap.put(card.code, card)
            }
        }

        withContext(Dispatchers.Main) {
            cardLiveData.value = hashMap
        }
    }
}