package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGCard(
    val ability: Int,
    var armor: Int,
    var characteristics: String,
    var clone_army: Boolean,
    var code: String,
    var deploy: Int,
    var destiny: Int,
    var episode_1: Boolean,
    var episode_7: Boolean,
    var force_aptitude: String,
    var forfeit: Int,
    var gametext: String,
    var has_errata: Boolean,
    var image_url: String,
    var lore: String,
    var maneuver: Int,
    var model_type: String,
    var name: String,
    var nav_computer: Boolean,
    var permanent_weapon: Boolean,
    var pilot: Boolean,
    var politics: Int,
    var position: Int,
    var power: Int,
    var presence: Boolean,
    var rarity_code: String,
    var republic: Boolean,
    var separatist: Boolean,
    var set_code: String,
    var side_code: String,
    var subtype_code: String,
    var type_code: String,
    var uniqueness: String,
    var warrior: Boolean
) : Serializable, Comparable<SWCCGCard> {

    val imageUrlLarge: String
        get() = IMAGE_URL_PREFIX + image_url

    val imageUrlSmall: String
        get() = IMAGE_URL_PREFIX + image_url.replace("large/", "t_")

    companion object {
        val IMAGE_URL_PREFIX =
            "https://www.starwarsccg.org/wp/wp-content/plugins/card-search/cards/starwars/"
    }

    override fun compareTo(other: SWCCGCard): Int {
        return name.compareTo(other.name)
    }
}

