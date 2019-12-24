package com.hatfat.swccg.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGConfig
import java.util.*

class FullCardListAdapter(val context: Context,
                          val config: SWCCGConfig,
                          val isFullscreen: Boolean,
                          val isLandscape: Boolean,
                          val indexSelectedInterface: IndexSelectedInterface
) : RecyclerView.Adapter<FullCardListAdapter.ViewHolder>() {

    var cardList: ArrayList<SWCCGCard> = ArrayList()
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
        return cardList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    inner class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.view_full_card_imageview)

        fun bind(card: SWCCGCard) {
            /* clear old image view */
            imageView.setImageResource(0)
            imageView.rotation = if (rotated) 180.0f else 0.0f

            val imageUrlSmall = if (flipped && card.hasImageUrl2) card.imageUrl2Small else card.imageUrlSmall
            val imageUrlLarge = if (flipped && card.hasImageUrl2) card.imageUrl2Large else card.imageUrlLarge

            var largeImageRequest = Glide.with(context).load(imageUrlLarge)
                .placeholder(R.mipmap.loading_large)
                .error(R.mipmap.loading_large)

            val smallImageRequest = Glide.with(context).load(imageUrlSmall)
                .placeholder(R.mipmap.loading_small)
                .error(R.mipmap.loading_small)

            largeImageRequest = largeImageRequest.thumbnail(smallImageRequest)

            var imageRequest = if (isFullscreen) largeImageRequest else smallImageRequest

            if (config.shouldUsePlaystoreImages) {
                imageRequest = imageRequest.override(16, 22)
            }

            imageRequest.into(imageView)

            view.setOnClickListener {
                indexSelectedInterface.onIndexSelected(cardList.indexOf(card))
            }
        }
    }

    interface IndexSelectedInterface {
        fun onIndexSelected(index: Int)
    }
}