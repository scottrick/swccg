package com.hatfat.swccg.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hatfat.swccg.R
import com.hatfat.swccg.app.InjectionGraph
import com.hatfat.swccg.app.SWCCGApplication
import com.hatfat.swccg.data.SWCCGCardList
import com.hatfat.swccg.util.hideKeyboard
import com.hatfat.swccg.viewmodels.SWCCGViewModelFactory
import com.hatfat.swccg.viewmodels.SearchViewModel
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var swccgApplication: SWCCGApplication

    @Inject
    lateinit var swccgViewModelFactory: SWCCGViewModelFactory

    private lateinit var viewModel: SearchViewModel

    override fun onAttach(context: Context) {
        InjectionGraph.component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider(this, swccgViewModelFactory)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchStringEditText = view.findViewById<TextView>(R.id.search_edittext)
        val searchTitleCheckBox = view.findViewById<CheckBox>(R.id.search_title_checkbox)
        val searchGametextCheckBox = view.findViewById<CheckBox>(R.id.search_gametext_checkbox)
        val searchLoreCheckBox = view.findViewById<CheckBox>(R.id.search_lore_checkbox)
        val progressView = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val searchScrollView = view.findViewById<ScrollView>(R.id.search_scrollview)
        val searchButton = view.findViewById<Button>(R.id.search_button)

        searchStringEditText.text = viewModel.searchString.value
        searchTitleCheckBox.isChecked = viewModel.searchTitle.value ?: false
        searchGametextCheckBox.isChecked = viewModel.searchGametext.value ?: false
        searchLoreCheckBox.isChecked = viewModel.searchLore.value ?: false

        searchTitleCheckBox.setOnCheckedChangeListener { _, b -> viewModel.titleToggled(b) }
        searchGametextCheckBox.setOnCheckedChangeListener { _, b -> viewModel.gametextToggled(b) }
        searchLoreCheckBox.setOnCheckedChangeListener { _, b -> viewModel.loreToggled(b) }
        searchButton.setOnClickListener { viewModel.searchPressed(searchStringEditText.text.toString()) }

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                SearchViewModel.State.ENTERING_INFO -> {
                    progressView.visibility = GONE
                    searchScrollView.visibility = VISIBLE
                }
                SearchViewModel.State.SEARCHING -> {
                    progressView.visibility = VISIBLE
                    searchScrollView.visibility = GONE
                    dismissKeyboard()
                }
                else -> {
                    Log.e("SearchFragment", "Handling invalid state: $it")
                    progressView.visibility = GONE
                    searchScrollView.visibility = VISIBLE
                }
            }
        })

        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    Toast.makeText(
                        this.context,
                        R.string.search_no_results_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToCardListFragment(
                            SWCCGCardList(it)
                        )
                    )
                }

                viewModel.doneHandlingSearchResults()
            }
        })

        return view;
    }

    override fun onPause() {
        super.onPause()
        dismissKeyboard()
    }

    private fun dismissKeyboard() {
        view?.hideKeyboard()
    }
}