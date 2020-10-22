package com.hatfat.cards.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hatfat.cards.R
import com.hatfat.cards.data.SWCCGCard
import com.hatfat.cards.data.SWCCGCardIdList
import com.hatfat.cards.data.CardsConfig
import com.hatfat.cards.repo.CardRepository

class FullCardListAdapter(val context: Context,
                          val config: CardsConfig,
                          val isFullscreen: Boolean,
                          val isLandscape: Boolean,
                          val indexSelectedInterface: IndexSelectedInterface,
                          val cardRepository: CardRepository
) : RecyclerView.Adapter<FullCardListAdapter.ViewHolder>() {

    var cardIdList = SWCCGCardIdList(emptyList())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var rotated: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var flipped: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_full_card, parent, false)

        val layoutParams: ViewGroup.LayoutParams

        if (isFullscreen) {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        else {
            if (isLandscape) {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
            else {
                layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }
        }

        view.layoutParams = layoutParams

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardIdList.cardIds.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cardRepository.cardsMap.value?.get(cardIdList.cardIds[position])?.let {
            holder.bind(position, it)
        }
    }

    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.view_full_card_imageview)

        fun bind(position: Int, card: SWCCGCard) {
            /* clear old image view */
            imageView.setImageResource(0)
            imageView.rotation = if (rotated) 180.0f else 0.0f

            val imageUrl = if (flipped && card.isFlippable) card.back?.imageUrl else card.front.imageUrl

            var imageRequest = Glide.with(context).load(imageUrl)
                .placeholder(R.mipmap.loading_large)
                .error(R.mipmap.loading_large)

            if (config.shouldUsePlayStoreImages) {
                imageRequest = imageRequest.override(16, 22)
            }

            imageRequest.into(imageView)

            view.setOnClickListener {
                indexSelectedInterface.onIndexSelected(position)
            }
        }
    }

    interface IndexSelectedInterface {
        fun onIndexSelected(index: Int)
    }
}
