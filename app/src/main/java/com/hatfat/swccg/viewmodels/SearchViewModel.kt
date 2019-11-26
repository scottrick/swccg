package com.hatfat.swccg.viewmodels

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardType
import com.hatfat.swccg.data.SWCCGSet
import com.hatfat.swccg.data.SWCCGSide
import com.hatfat.swccg.filter.*
import com.hatfat.swccg.repo.CardRepository
import com.hatfat.swccg.repo.MetaDataRepository
import com.hatfat.swccg.repo.SetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val cardRepository: CardRepository,
    val metaDataRepository: MetaDataRepository,
    val resources: Resources,
    val setRepository: SetRepository
) : ViewModel() {

    enum class State {
        ENTERING_INFO,
        SEARCHING,
    }

    private val anySet = SWCCGSet("", name = resources.getString(R.string.search_any_set))
    private val anySide = SWCCGSide("", resources.getString(R.string.search_any_side))
    private val anyCardType = SWCCGCardType("", resources.getString(R.string.search_any_cardtype))

    private val searchStringLiveData = MutableLiveData<String>()
    private val searchTitleLiveData = MutableLiveData<Boolean>()
    private val searchGametextLiveData = MutableLiveData<Boolean>()
    private val searchLoreLiveData = MutableLiveData<Boolean>()
    private val stateLiveData = MutableLiveData<State>()
    private val searchResultsLiveData = MutableLiveData<List<SWCCGCard>>()
    private val selectedSetLiveData = MutableLiveData<SWCCGSet>()
    private val selectedSideLiveData = MutableLiveData<SWCCGSide>()
    private val selectedCardTypeLiveData = MutableLiveData<SWCCGCardType>()

    val searchString: LiveData<String>
        get() = searchStringLiveData

    val searchTitle: LiveData<Boolean>
        get() = searchTitleLiveData

    val searchGametext: LiveData<Boolean>
        get() = searchGametextLiveData

    val searchLore: LiveData<Boolean>
        get() = searchLoreLiveData

    val state: LiveData<State>
        get() = stateLiveData

    val searchResults: LiveData<List<SWCCGCard>>
        get() = searchResultsLiveData

    val sets: LiveData<List<SWCCGSet>> = Transformations.map(setRepository.sets) {
        val sets = it.values.toMutableList()
        sets.sort()
        sets.add(0, anySet)
        sets
    }

    val sides: LiveData<List<SWCCGSide>> = Transformations.map(metaDataRepository.sides) {
        val sides = it.values.toMutableList()
        sides.sort()
        sides.add(0, anySide)
        sides
    }

    val cardTypes: LiveData<List<SWCCGCardType>> =
        Transformations.map(metaDataRepository.cardTypes) {
            val cardTypes = it.values.toMutableList()
            cardTypes.sort()
            cardTypes.add(0, anyCardType)
            cardTypes
        }

    val selectedSet: LiveData<SWCCGSet>
        get() = selectedSetLiveData

    val selectedSide: LiveData<SWCCGSide>
        get() = selectedSideLiveData

    val selectedCardType: LiveData<SWCCGCardType>
        get() = selectedCardTypeLiveData

    init {
        searchStringLiveData.value = ""
        searchTitleLiveData.value = true
        searchGametextLiveData.value = false
        searchLoreLiveData.value = false
        stateLiveData.value = State.ENTERING_INFO
        selectedSideLiveData.value = anySide
    }

    fun titleToggled(newValue: Boolean) {
        searchTitleLiveData.value = newValue
    }

    fun gametextToggled(newValue: Boolean) {
        searchGametextLiveData.value = newValue
    }

    fun loreToggled(newValue: Boolean) {
        searchLoreLiveData.value = newValue
    }

    fun setSelectedSet(newValue: SWCCGSet) {
        selectedSetLiveData.value = newValue
    }

    fun setSelectedSide(newValue: SWCCGSide) {
        selectedSideLiveData.value = newValue
    }

    fun setSelectedCardType(newValue: SWCCGCardType) {
        selectedCardTypeLiveData.value = newValue
    }

    fun searchPressed(searchString: String) {
        searchStringLiveData.value = searchString

        stateLiveData.value = State.SEARCHING
        GlobalScope.launch(Dispatchers.IO) {
            doSearch()
        }
    }

    fun doneHandlingSearchResults() {
        searchResultsLiveData.value = null
    }

    private suspend fun doSearch() {
        val filters = LinkedList<Filter>()

        searchStringLiveData.value?.let {
            if (it.isNotEmpty()) {
                val stringFilter = StringFilter(
                    it,
                    searchTitle.value ?: false,
                    searchGametext.value ?: false,
                    searchLore.value ?: false
                )

                filters.add(stringFilter)
            }
        }

        selectedSet.value?.let {
            if (!it.equals(anySet)) {
                filters.add(SetFilter(it))
            }
        }

        selectedSide.value?.let {
            if (!it.equals(anySide)) {
                filters.add(SideFilter(it))
            }
        }

        selectedCardType.value?.let {
            if (!it.equals(anyCardType)) {
                filters.add(CardTypeFilter(it))
            }
        }

        var results = cardRepository.cardsArray.value?.toList()

        for (filter in filters) {
            results = results?.filter { filter.filter(it) }
        }

        withContext(Dispatchers.Main) {
            searchResultsLiveData.value = results
            stateLiveData.postValue(State.ENTERING_INFO)
        }
    }
}
