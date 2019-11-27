package com.hatfat.swccg.viewmodels

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.hatfat.swccg.R
import com.hatfat.swccg.data.*
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
    private val anyCardType = SWCCGCardType("", resources.getString(R.string.search_any_card_type))
    private val anyCardSubType =
        SWCCGCardSubType("", resources.getString(R.string.search_any_card_subtype))

    private val searchStringLiveData = MutableLiveData<String>()
    private val searchTitleLiveData = MutableLiveData<Boolean>()
    private val searchGametextLiveData = MutableLiveData<Boolean>()
    private val searchLoreLiveData = MutableLiveData<Boolean>()
    private val searchTextEnabledLiveData = MediatorLiveData<Boolean>().apply {
        val observer = Observer<Boolean>() {
            this.value =
                searchTitleLiveData.value ?: false || searchGametextLiveData.value ?: false || searchLoreLiveData.value ?: false
        }

        this.addSource(searchTitleLiveData, observer)
        this.addSource(searchGametextLiveData, observer)
        this.addSource(searchLoreLiveData, observer)
    }
    private val stateLiveData = MutableLiveData<State>()
    private val searchResultsLiveData = MutableLiveData<List<SWCCGCard>>()
    private val selectedSetLiveData = MutableLiveData<SWCCGSet>()
    private val selectedSideLiveData = MutableLiveData<SWCCGSide>()
    private val selectedCardTypeLiveData = MutableLiveData<SWCCGCardType>()
    private val selectedCardSubTypeLiveData = MutableLiveData<SWCCGCardSubType>()

    val searchString: LiveData<String>
        get() = searchStringLiveData

    val searchTitle: LiveData<Boolean>
        get() = searchTitleLiveData

    val searchGametext: LiveData<Boolean>
        get() = searchGametextLiveData

    val searchTextEnabled: LiveData<Boolean>
        get() = searchTextEnabledLiveData

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

    val cardSubTypes: LiveData<List<SWCCGCardSubType>> =
        Transformations.map(metaDataRepository.cardSubTypes) {
            val cardSubTypes = it.values.toMutableList()
            cardSubTypes.sort()
            cardSubTypes.add(0, anyCardSubType)
            cardSubTypes
        }

    val selectedSet: LiveData<SWCCGSet>
        get() = selectedSetLiveData

    val selectedSide: LiveData<SWCCGSide>
        get() = selectedSideLiveData

    val selectedCardType: LiveData<SWCCGCardType>
        get() = selectedCardTypeLiveData

    val selectedCardSubType: LiveData<SWCCGCardSubType>
        get() = selectedCardSubTypeLiveData

    init {
        stateLiveData.value = State.ENTERING_INFO
        reset()
    }

    private fun reset() {
        searchStringLiveData.value = ""
        searchTitleLiveData.value = true
        searchGametextLiveData.value = false
        searchLoreLiveData.value = false
        selectedSetLiveData.value = anySet
        selectedSideLiveData.value = anySide
        selectedCardTypeLiveData.value = anyCardType
        selectedCardSubTypeLiveData.value = anyCardSubType
    }

    fun resetPressed() {
        reset()
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

    fun setSelectedCardSubType(newValue: SWCCGCardSubType) {
        selectedCardSubTypeLiveData.value = newValue
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
            val title = searchTitle.value ?: false
            val gametext = searchGametext.value ?: false
            val lore = searchLore.value ?: false

            if (it.isNotEmpty() && (title || gametext || lore)) {
                /* only add string filter if we have a search string, and one of the boxes is checked */
                val stringFilter = StringFilter(
                    it, title, gametext, lore
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

        selectedCardSubType.value?.let {
            if (!it.equals(anyCardSubType)) {
                filters.add(CardSubTypeFilter(it))
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
