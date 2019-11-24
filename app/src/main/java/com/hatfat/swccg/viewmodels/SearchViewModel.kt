package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.filter.StringFilter
import com.hatfat.swccg.repo.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    val cardRepository: CardRepository
) : ViewModel() {

    enum class State {
        ENTERING_INFO,
        SEARCHING,
    }

    private val searchStringLiveData = MutableLiveData<String>()
    private val searchTitleLiveData = MutableLiveData<Boolean>()
    private val searchGametextLiveData = MutableLiveData<Boolean>()
    private val searchLoreLiveData = MutableLiveData<Boolean>()
    private val stateLiveData = MutableLiveData<State>()
    private val searchResultsLiveData = MutableLiveData<List<SWCCGCard>>()

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

    init {
        searchStringLiveData.value = ""
        searchTitleLiveData.value = true
        searchGametextLiveData.value = false
        searchLoreLiveData.value = false
        stateLiveData.value = State.ENTERING_INFO
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

    fun searchPressed(searchString: String) {
        searchStringLiveData.value = searchString

        searchStringLiveData.value?.let {
            if (it.isNotEmpty()) {
                stateLiveData.value = State.SEARCHING
                GlobalScope.launch(Dispatchers.IO) {
                    doSearch(it)
                }
            }
        }
    }

    fun doneHandlingSearchResults() {
        searchResultsLiveData.value = null
    }

    private suspend fun doSearch(searchString: String) {
        val stringFilter = StringFilter(
            searchString,
            searchTitle.value ?: false,
            searchGametext.value ?: false,
            searchLore.value ?: false
        )

        val results = cardRepository.cardsArray.value?.filter { stringFilter.filter(it) }

        withContext(Dispatchers.Main) {
            searchResultsLiveData.value = results
            stateLiveData.postValue(State.ENTERING_INFO)
        }
    }
}
