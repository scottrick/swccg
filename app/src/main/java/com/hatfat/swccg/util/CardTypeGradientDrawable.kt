package com.hatfat.swccg.util

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import com.hatfat.swccg.R
import com.hatfat.swccg.data.SWCCGCard

class CardTypeGradientDrawable(
    card: SWCCGCard,
    resources: Resources
) : GradientDrawable(
    Orientation.LEFT_RIGHT,
    intArrayOf(colorForCard(card, resources), 0xffffffff.toInt()))

private fun colorForCard(card: SWCCGCard, resources: Resources) : Int {
    if (card.side == "Light") {
        when (card.front.type) {
            "Effect" -> return resources.getColor(R.color.card_type_ls_effect, null)
        }
    }
    else if (card.side == "Dark") {
        when (card.front.type) {
            "Effect" -> return resources.getColor(R.color.card_type_ds_effect, null)
        }
    }

    //fallback to white
    return 0xffffffff.toInt()
}
