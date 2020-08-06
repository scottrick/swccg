package com.hatfat.swccg.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.data.SWCCGConfig
import com.hatfat.swccg.repo.CardRepository

class CardListAdapter(
    val context: Context,
    val config: SWCCGConfig,
    val cardSelectedInterface: CardSelectedInterface,
    val cardRepository: CardRepository
) : RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    var cardIdList = SWCCGCardIdList(emptyList())
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.card_name)
        val typeTextView: TextView = view.findViewById(R.id.card_type)
        val rarityTextView: TextView = view.findViewById(R.id.card_rarity)
        val imageView: ImageView = view.findViewById(R.id.card_imageview)

        fun bind(card: SWCCGCard) {
            rarityTextView.text = card.rarity

            /* clear old image view */
            imageView.setImageResource(0)

            val frontSide = card.front
            nameTextView.text = frontSide.title
            typeTextView.text = frontSide.type

            if (config.shouldUsePlaystoreImages) {
                Glide.with(context).load(frontSide.imageUrl).override(16, 22)
                    .placeholder(R.mipmap.loading_small).into(imageView)
            } else {
                Glide.with(context).load(frontSide.imageUrl).placeholder(R.mipmap.loading_small)
                    .into(imageView)
            }

            view.setOnClickListener {
                cardSelectedInterface.onCardSelected(card)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_list_card, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardIdList.cardIds.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cardRepository.cardsMap.value?.get(cardIdList.cardIds[position])?.let {
            holder.bind(it)
        }
    }

    interface CardSelectedInterface {
        fun onCardSelected(card: SWCCGCard)
    }
}