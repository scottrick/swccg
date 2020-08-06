package com.hatfat.swccg.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hatfat.swccg.R
import com.hatfat.swccg.app.InjectionGraph
import com.hatfat.swccg.app.SWCCGApplication
import com.hatfat.swccg.data.SWCCGFormat
import com.hatfat.swccg.util.hideKeyboard
import com.hatfat.swccg.viewmodels.SWCCGViewModelFactory
import com.hatfat.swccg.viewmodels.SearchViewModel
import javax.inject.Inject

class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {

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

        viewModel = ViewModelProvider(this, swccgViewModelFactory)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchStringEditText = view.findViewById<EditText>(R.id.search_edittext)
        val searchTitleCheckBox = view.findViewById<CheckBox>(R.id.search_title_checkbox)
        val searchGametextCheckBox = view.findViewById<CheckBox>(R.id.search_gametext_checkbox)
        val searchLoreCheckBox = view.findViewById<CheckBox>(R.id.search_lore_checkbox)
        val progressView = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val searchScrollView = view.findViewById<ScrollView>(R.id.search_scrollview)
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        val searchButton = view.findViewById<Button>(R.id.search_button)
        val setSpinner = view.findViewById<Spinner>(R.id.set_spinner)
        val sideSpinner = view.findViewById<Spinner>(R.id.side_spinner)
        val typeSpinner = view.findViewById<Spinner>(R.id.type_spinner)
        val subtypeSpinner = view.findViewById<Spinner>(R.id.subtype_spinner)
        val formatSpinner = view.findViewById<Spinner>(R.id.format_spinner)

        val setAdapter =
            ArrayAdapter<String>(view.context, R.layout.view_item, ArrayList<String>())
        setAdapter.setDropDownViewResource(R.layout.view_item)
        setSpinner.adapter = setAdapter

        val sideAdapter =
            ArrayAdapter<String>(view.context, R.layout.view_item, ArrayList<String>())
        sideAdapter.setDropDownViewResource(R.layout.view_item)
        sideSpinner.adapter = sideAdapter

        val typeAdapter =
            ArrayAdapter<String>(
                view.context,
                R.layout.view_item,
                ArrayList<String>()
            )
        typeAdapter.setDropDownViewResource(R.layout.view_item)
        typeSpinner.adapter = typeAdapter

        val subtypeAdapter =
            ArrayAdapter<String>(
                view.context,
                R.layout.view_item,
                ArrayList<String>()
            )
        subtypeAdapter.setDropDownViewResource(R.layout.view_item)
        subtypeSpinner.adapter = subtypeAdapter

        val formatAdapter =
            ArrayAdapter<SWCCGFormat>(
                view.context,
                R.layout.view_item,
                ArrayList<SWCCGFormat>()
            )
        formatAdapter.setDropDownViewResource(R.layout.view_item)
        formatSpinner.adapter = formatAdapter

        viewModel.sets.observe(viewLifecycleOwner, Observer {
            setAdapter.clear()
            setAdapter.addAll(it)

            /* set the initially selected position */
            setSpinner.setSelection(setAdapter.getPosition(viewModel.selectedSet.value))
        })

        viewModel.selectedSet.observe(viewLifecycleOwner, Observer {
            /* set the initially selected position */
            setSpinner.setSelection(setAdapter.getPosition(it))
        })

        viewModel.sides.observe(viewLifecycleOwner, Observer {
            sideAdapter.clear()
            sideAdapter.addAll(it)

            /* set the initially selected position */
            sideSpinner.setSelection(sideAdapter.getPosition(viewModel.selectedSide.value))
        })

        viewModel.selectedSide.observe(viewLifecycleOwner, Observer {
            /* set the initially selected position */
            sideSpinner.setSelection(sideAdapter.getPosition(it))
        })

        viewModel.cardTypes.observe(viewLifecycleOwner, Observer {
            typeAdapter.clear()
            typeAdapter.addAll(it)

            /* set the initially selected position */
            typeSpinner.setSelection(typeAdapter.getPosition(viewModel.selectedCardType.value))
        })

        viewModel.selectedCardType.observe(viewLifecycleOwner, Observer {
            /* set the initially selected position */
            typeSpinner.setSelection(typeAdapter.getPosition(it))
        })

        viewModel.cardSubTypes.observe(viewLifecycleOwner, Observer {
            subtypeAdapter.clear()
            subtypeAdapter.addAll(it)

            /* set the initially selected position */
            subtypeSpinner.setSelection(subtypeAdapter.getPosition(viewModel.selectedCardSubType.value))
        })

        viewModel.selectedCardSubType.observe(viewLifecycleOwner, Observer {
            /* set the initially selected position */
            subtypeSpinner.setSelection(subtypeAdapter.getPosition(it))
        })

        viewModel.formatTypes.observe(viewLifecycleOwner, Observer {
            formatAdapter.clear()
            formatAdapter.addAll(it)

            /* set the initially selected position */
            formatSpinner.setSelection(formatAdapter.getPosition(viewModel.selectedFormat.value))
        })

        viewModel.selectedFormat.observe(viewLifecycleOwner, Observer {
            /* set the initially selected position */
            formatSpinner.setSelection(formatAdapter.getPosition(it))
        })

        setSpinner.onItemSelectedListener = this
        sideSpinner.onItemSelectedListener = this
        typeSpinner.onItemSelectedListener = this
        subtypeSpinner.onItemSelectedListener = this
        formatSpinner.onItemSelectedListener = this

        viewModel.searchString.observe(viewLifecycleOwner, Observer {
            if (!searchStringEditText.text.toString().equals(it)) {
                searchStringEditText.setText(it)
            }
        })

        viewModel.searchTitle.observe(viewLifecycleOwner, Observer {
            searchTitleCheckBox.isChecked = it
        })

        viewModel.searchGametext.observe(viewLifecycleOwner, Observer {
            searchGametextCheckBox.isChecked = it
        })

        viewModel.searchLore.observe(viewLifecycleOwner, Observer {
            searchLoreCheckBox.isChecked = it
        })

        searchTitleCheckBox.setOnCheckedChangeListener { _, b -> viewModel.titleToggled(b) }
        searchGametextCheckBox.setOnCheckedChangeListener { _, b -> viewModel.gametextToggled(b) }
        searchLoreCheckBox.setOnCheckedChangeListener { _, b -> viewModel.loreToggled(b) }
        resetButton.setOnClickListener { viewModel.resetPressed() }
        searchButton.setOnClickListener { viewModel.searchPressed() }

        viewModel.searchTextEnabled.observe(viewLifecycleOwner, Observer {
            searchStringEditText.isEnabled = it
        })

        searchStringEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchString(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        searchStringEditText.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.searchPressed()
                        return true
                    }
                    else -> return false
                }
            }
        })

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                SearchViewModel.State.ENTERING_INFO -> {
                    progressView.visibility = GONE
                    searchScrollView.visibility = VISIBLE
                }
                SearchViewModel.State.LOADING,
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
                if (it.cardIds.isEmpty()) {
                    Toast.makeText(
                        this.context,
                        R.string.search_no_results_toast,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToCardListFragment(
                            it
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

    override fun onNothingSelected(p0: AdapterView<*>?) {
        /* nothing selected, we don't care */
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.set_spinner -> viewModel.setSelectedSet(parent.getItemAtPosition(position) as String)
            R.id.side_spinner -> viewModel.setSelectedSide(parent.getItemAtPosition(position) as String)
            R.id.type_spinner -> viewModel.setSelectedCardType(
                parent.getItemAtPosition(
                    position
                ) as String
            )
            R.id.subtype_spinner -> viewModel.setSelectedCardSubType(
                parent.getItemAtPosition(
                    position
                ) as String
            )
            R.id.format_spinner -> viewModel.setSelectedFormat(
                parent.getItemAtPosition(
                    position
                ) as SWCCGFormat
            )
        }
    }
}
