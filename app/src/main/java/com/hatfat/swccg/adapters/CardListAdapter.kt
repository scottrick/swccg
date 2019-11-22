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
import java.util.*

class CardListAdapter(
    val context: Context,
    val cardSelectedInterface: CardSelectedInterface
) : RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    var cardList: ArrayList<SWCCGCard> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        val nameTextView: TextView = view.findViewById(R.id.card_name)
        val typeTextView: TextView = view.findViewById(R.id.card_type)
        val imageView: ImageView = view.findViewById(R.id.card_imageview)

        fun bind(card: SWCCGCard) {
            nameTextView.text = card.name
            typeTextView.text = card.type_code

            /* clear old image view */
            imageView.setImageResource(0)

                Glide.with(context).load(card.imageUrlSmall).into(imageView)

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
        return cardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    interface CardSelectedInterface {
        fun onCardSelected(card: SWCCGCard)
    }
}