package com.hatfat.swccg.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.hatfat.swccg.R
import com.hatfat.swccg.adapters.FullCardListAdapter
import com.hatfat.swccg.app.InjectionGraph
import com.hatfat.swccg.app.SWCCGApplication
import com.hatfat.swccg.data.SWCCGConfig
import com.hatfat.swccg.repo.CardRepository
import com.hatfat.swccg.repo.MetaDataRepository
import com.hatfat.swccg.viewmodels.SWCCGViewModelFactory
import com.hatfat.swccg.viewmodels.SwipeCardListViewModel
import javax.inject.Inject

class SwipeCardListFragment : Fragment() {

    @Inject
    lateinit var swccgApplication: SWCCGApplication

    @Inject
    lateinit var config: SWCCGConfig

    @Inject
    lateinit var swccgViewModelFactory: SWCCGViewModelFactory

    @Inject
    lateinit var metaDataRepository: MetaDataRepository

    @Inject
    lateinit var cardRepository: CardRepository

    private lateinit var viewModel: SwipeCardListViewModel

    private lateinit var topRecyclerView: RecyclerView
    private lateinit var bottomRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<SwipeCardListFragmentArgs>().value

        viewModel = ViewModelProvider(this, swccgViewModelFactory)[SwipeCardListViewModel::class.java]
        viewModel.setCardIdList(args.cardIdList)
        viewModel.setCurrentCardIndex(args.selectedIndex)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swipe_card_list, container, false)

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val orientation = if (isLandscape) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL

        val smallCardListAdapter =
            FullCardListAdapter(
                swccgApplication,
                config,
                false,
                isLandscape,
                object : FullCardListAdapter.IndexSelectedInterface {
                    override fun onIndexSelected(index: Int) {
                        indexSelected(index)
                    }
                },
                cardRepository
            )

        val bigCardListAdapter =
            FullCardListAdapter(
                swccgApplication,
                config,
                true,
                isLandscape,
                object : FullCardListAdapter.IndexSelectedInterface {
                    override fun onIndexSelected(index: Int) {
                        indexSelected(index)
                    }
                },
                cardRepository
            )

        view?.findViewById<RecyclerView>(R.id.top_recycler_view)?.apply {
            topRecyclerView = this
            this.layoutManager = LinearLayoutManager(context, orientation, false)
//            this.layoutManager = CardLayoutManager()
            this.adapter = smallCardListAdapter
        }

        view?.findViewById<RecyclerView>(R.id.bottom_recycler_view)?.apply {
            bottomRecyclerView = this
            this.layoutManager = LinearLayoutManager(context, orientation, false)
            this.adapter = bigCardListAdapter

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(this)

            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        SCROLL_STATE_IDLE -> {
                            val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            indexSelected(firstPosition)
                        }
                    }
                }
            })
        }

        viewModel.cardIdList.observe(viewLifecycleOwner, Observer {
            smallCardListAdapter.cardIdList = it
            bigCardListAdapter.cardIdList = it
        })

        viewModel.currentCardIndex.value?.let {
            topRecyclerView.scrollToPosition(it)
            bottomRecyclerView.scrollToPosition(it)
        }

        viewModel.isRotated.observe(viewLifecycleOwner, Observer {
            bigCardListAdapter.rotated = it
        })

        viewModel.isFlipped.observe(viewLifecycleOwner, Observer {
            bigCardListAdapter.flipped = it
        })

        viewModel.isFlippable.observe(viewLifecycleOwner, Observer {
            view?.findViewById<AppCompatButton>(R.id.flip_button)?.visibility = if (it) VISIBLE else GONE
        })

        view?.findViewById<AppCompatButton>(R.id.rotate_button)?.setOnClickListener {
            viewModel.rotate()
        }

        view?.findViewById<AppCompatButton>(R.id.flip_button)?.setOnClickListener {
            viewModel.flip()
        }

        return view
    }

    private fun indexSelected(index: Int) {
        topRecyclerView.smoothScrollToPosition(index)
        bottomRecyclerView.smoothScrollToPosition(index)
        viewModel.setCurrentCardIndex(index)
    }

    override fun onAttach(context: Context) {
        InjectionGraph.component.inject(this)
        super.onAttach(context)
    }
}
