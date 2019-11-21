package com.hatfat.swccg.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.swccg.R
import com.hatfat.swccg.adapters.CardListAdapter
import com.hatfat.swccg.app.InjectionGraph
import com.hatfat.swccg.app.SWCCGApplication
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.viewmodels.MasterCardListViewModel
import com.hatfat.swccg.viewmodels.SWCCGViewModelFactory
import java.lang.Appendable
import javax.inject.Inject

class MasterCardListFragment : Fragment() {

    @Inject
    lateinit var swccgApplication: SWCCGApplication

    @Inject
    lateinit var swccgViewModelFactory: SWCCGViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel =
            ViewModelProvider(this, swccgViewModelFactory)[MasterCardListViewModel::class.java]
        viewModel.navigateToSingleCard.observe(this, Observer {
            it?.let {
                findNavController().navigate(MasterCardListFragmentDirections.actionMasterCardListFragmentToViewSingleCardFragment(it))
                viewModel.doneNavigating()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_master_card_list, container, false)
        val viewModel = ViewModelProvider(this)[MasterCardListViewModel::class.java]

        val cardListAdapter = CardListAdapter(swccgApplication, object : CardListAdapter.CardSelectedInterface {
            override fun onCardSelected(card: SWCCGCard) {
                viewModel.navigateTo(card)
            }
        })

        view?.findViewById<RecyclerView>(R.id.card_recyclerview)?.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = cardListAdapter
        }

        viewModel.cards.observe(viewLifecycleOwner, Observer {
            cardListAdapter.cardList = ArrayList(it.values)
        })

        return view
    }

    override fun onAttach(context: Context) {
        InjectionGraph.component.inject(this)
        super.onAttach(context)
    }
}
