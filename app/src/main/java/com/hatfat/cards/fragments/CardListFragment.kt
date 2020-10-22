package com.hatfat.cards.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.adapters.CardListAdapter
import com.hatfat.cards.app.InjectionGraph
import com.hatfat.cards.app.CardsApplication
import com.hatfat.cards.data.SWCCGCard
import com.hatfat.cards.data.CardsConfig
import com.hatfat.cards.repo.CardRepository
import com.hatfat.cards.repo.MetaDataRepository
import com.hatfat.cards.viewmodels.CardListViewModel
import com.hatfat.cards.viewmodels.SWCCGViewModelFactory
import javax.inject.Inject

class CardListFragment : Fragment() {

    @Inject
    lateinit var swccgApplication: CardsApplication

    @Inject
    lateinit var config: CardsConfig

    @Inject
    lateinit var swccgViewModelFactory: SWCCGViewModelFactory

    @Inject
    lateinit var metaDataRepository: MetaDataRepository

    @Inject
    lateinit var cardRepository: CardRepository

    private lateinit var viewModel: CardListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<CardListFragmentArgs>().value

        viewModel = ViewModelProvider(this, swccgViewModelFactory)[CardListViewModel::class.java]
        viewModel.setCardIdList(args.cardIdList)
        viewModel.navigateToSingleCard.observe(this, Observer {
            it?.let { card ->
                viewModel.cardIdList.value?.let {
                    findNavController().navigate(
                        CardListFragmentDirections.actionCardListFragmentToSwipeCardListFragment(
                            it, args.cardIdList.cardIds.indexOf(card.id)
                        )
                    )
                    viewModel.doneNavigating()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        val cardListAdapter =
            CardListAdapter(
                swccgApplication,
                config,
                object : CardListAdapter.CardSelectedInterface {
                    override fun onCardSelected(card: SWCCGCard) {
                        viewModel.navigateTo(card)
                    }
                },
                cardRepository
            )

        view?.findViewById<RecyclerView>(R.id.card_recyclerview)?.apply {
            this.layoutManager = LinearLayoutManager(this.context)
            this.adapter = cardListAdapter
        }

        viewModel.cardIdList.observe(viewLifecycleOwner, Observer {
            cardListAdapter.cardIdList = it
        })

        return view
    }

    override fun onAttach(context: Context) {
        InjectionGraph.component.inject(this)
        super.onAttach(context)
    }
}
