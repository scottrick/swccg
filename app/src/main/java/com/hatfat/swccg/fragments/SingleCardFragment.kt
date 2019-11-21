package com.hatfat.swccg.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hatfat.swccg.R
import com.hatfat.swccg.app.InjectionGraph
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.viewmodels.SingleCardViewModel

class SingleCardFragment : Fragment() {

    private lateinit var viewModel: SingleCardViewModel

    private lateinit var cardImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[SingleCardViewModel::class.java]

        val args = navArgs<SingleCardFragmentArgs>().value
        viewModel.setCard(args.card)
    }

    override fun onAttach(context: Context) {
        InjectionGraph.component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_single_card, container, false)
        cardImageView = view.findViewById(R.id.card_imageview)

        viewModel.cardCode.observe(viewLifecycleOwner, Observer<SWCCGCard> {
            Glide.with(this).load(it.imageUrlLarge).into(cardImageView)
        })

        return view;
    }
}