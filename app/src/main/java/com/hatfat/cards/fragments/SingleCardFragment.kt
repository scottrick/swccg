package com.hatfat.cards.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hatfat.cards.R
import com.hatfat.cards.app.InjectionGraph
import com.hatfat.cards.data.CardsConfig
import com.hatfat.cards.viewmodels.SingleCardViewModel
import javax.inject.Inject

class SingleCardFragment : Fragment() {

    private lateinit var viewModel: SingleCardViewModel

    @Inject
    lateinit var config: CardsConfig

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

        val cardImageView = view.findViewById<ImageView>(R.id.card_imageview)
        val rotateButton = view.findViewById<Button>(R.id.rotate_button)
        val flipButton = view.findViewById<Button>(R.id.flip_button)

        viewModel.cardUrl.observe(viewLifecycleOwner, Observer<String> {
            if (config.shouldUsePlayStoreImages) {
                Glide.with(this).load(it).override(31, 44).placeholder(R.mipmap.loading_large)
                    .into(cardImageView)
            } else {
                Glide.with(this).load(it).placeholder(R.mipmap.loading_large)
                    .into(cardImageView)
            }
        })

        viewModel.isRotated.observe(viewLifecycleOwner, Observer {
            cardImageView.rotation = if (it) 180.0f else 0.0f
        })

        viewModel.isFlippable.observe(viewLifecycleOwner, Observer {
            flipButton.visibility = if (it) VISIBLE else GONE
        })

        rotateButton.setOnClickListener { viewModel.rotate() }
        flipButton.setOnClickListener { viewModel.flip() }

        return view;
    }
}
