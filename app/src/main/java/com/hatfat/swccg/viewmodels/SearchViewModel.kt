package com.hatfat.swccg.viewmodels

import android.content.res.Resources
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGFormat
import com.hatfat.swccg.filter.*
import com.hatfat.swccg.repo.CardRepository
import com.hatfat.swccg.repo.FormatRepository
import com.hatfat.swccg.repo.MetaDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val resources: Resources,
    val cardRepository: CardRepository,
    val metaDataRepository: MetaDataRepository,
    val formatRepository: FormatRepository
) : ViewModel() {

    enum class State {
        LOADING,
        ENTERING_INFO,
        SEARCHING,
    }

    private val anySet = resources.getString(R.string.search_any_set)
    private val anySide = resources.getString(R.string.search_any_side)
    private val anyCardType = resources.getString(R.string.search_any_card_type)
    private val anyCardSubType = resources.getString(R.string.search_any_card_subtype)
    private val anyFormat = SWCCGFormat("", resources.getString(R.string.search_any_format))

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
    private val searchResultsLiveData = MutableLiveData<List<SWCCGCard>>()
    private val selectedSetLiveData = MutableLiveData<String>()
    private val selectedSideLiveData = MutableLiveData<String>()
    private val selectedCardTypeLiveData = MutableLiveData<String>()
    private val selectedCardSubTypeLiveData = MutableLiveData<String>()
    private val selectedFormatLiveData = MutableLiveData<SWCCGFormat>()

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

    val searchResults: LiveData<List<SWCCGCard>>
        get() = searchResultsLiveData

    val sets: LiveData<List<String>> = Transformations.map(metaDataRepository.sets) {
        val sets = it.toMutableList()
        sets.sort()
        sets.add(0, anySet)
        sets
    }

    val sides: LiveData<List<String>> = Transformations.map(metaDataRepository.sides) {
        val sides = it.toMutableList()
        sides.sort()
        sides.add(0, anySide)
        sides
    }

    val cardTypes: LiveData<List<String>> =
        Transformations.map(metaDataRepository.cardTypes) {
            val cardTypes = it.toMutableList()
            cardTypes.sort()
            cardTypes.add(0, anyCardType)
            cardTypes
        }

    val cardSubTypes: LiveData<List<String>> =
        Transformations.map(metaDataRepository.cardSubTypes) {
            val cardSubTypes = it.toMutableList()
            cardSubTypes.sort()
            cardSubTypes.add(0, anyCardSubType)
            cardSubTypes
        }

    val formatTypes: LiveData<List<SWCCGFormat>> =
        Transformations.map(formatRepository.formats) {
            val formatTypes = it.values.toMutableList()
            formatTypes.sort()
            formatTypes.add(0, anyFormat)
            formatTypes
        }

    val loaded: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        val observer = Observer<Boolean> {
            this.value = cardRepository.loaded.value ?: false &&
                    formatRepository.loaded.value ?: false &&
                    metaDataRepository.loaded.value ?: false
        }

        this.addSource(cardRepository.loaded, observer)
        this.addSource(formatRepository.loaded, observer)
        this.addSource(metaDataRepository.loaded, observer)
    }

    private val stateLiveData: MediatorLiveData<State> = MediatorLiveData()

    init {
        stateLiveData.addSource(loaded) {
            if (!it) {
                stateLiveData.value = State.LOADING
            } else if (it && state.value == State.LOADING) {
                stateLiveData.value = State.ENTERING_INFO
            }
        }

        stateLiveData.value = State.LOADING
    }

    val state: LiveData<State>
        get() = stateLiveData

    val selectedSet: LiveData<String>
        get() = selectedSetLiveData

    val selectedSide: LiveData<String>
        get() = selectedSideLiveData

    val selectedCardType: LiveData<String>
        get() = selectedCardTypeLiveData

    val selectedCardSubType: LiveData<String>
        get() = selectedCardSubTypeLiveData

    val selectedFormat: LiveData<SWCCGFormat>
        get() = selectedFormatLiveData

    init {
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
        selectedFormatLiveData.value = anyFormat
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

    fun setSelectedSet(newValue: String) {
        selectedSetLiveData.value = newValue
    }

    fun setSelectedSide(newValue: String) {
        selectedSideLiveData.value = newValue
    }

    fun setSelectedCardType(newValue: String) {
        selectedCardTypeLiveData.value = newValue
    }

    fun setSelectedCardSubType(newValue: String) {
        selectedCardSubTypeLiveData.value = newValue
    }

    fun setSelectedFormat(newValue: SWCCGFormat) {
        selectedFormatLiveData.value = newValue
    }

    fun setSearchString(newValue: String) {
        searchStringLiveData.value = newValue
    }

    fun searchPressed() {
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
            if (it != anySet) {
                filters.add(SetFilter(it))
            }
        }

        selectedSide.value?.let {
            if (it != anySide) {
                filters.add(SideFilter(it))
            }
        }

        selectedCardType.value?.let {
            if (it != anyCardType) {
                filters.add(CardTypeFilter(it))
            }
        }

        selectedCardSubType.value?.let {
            if (it != anyCardSubType) {
                filters.add(CardSubTypeFilter(it))
            }
        }

        selectedFormat.value?.let {
            if (it != anyFormat) {
                filters.add(FormatFilter(it))
            }
        }

        var results = cardRepository.cardsArray.value?.toList()

        for (filter in filters) {
            results = results?.filter { filter.filter(it) }
        }

        withContext(Dispatchers.Main) {
            searchResultsLiveData.value = results
            stateLiveData.value = State.ENTERING_INFO
        }
    }
}
