package com.hatfat.swccg.data

import java.io.Serializable

data class SWCCGCard(
    val id: Int

    /*
      "id": 308,
      "side": "Dark",
      "rarity": "R",
      "set": "Dagobah",
      "front": {
        "title": "•Bossk",
        "imageUrl": "https://res.starwarsccg.org/cards/Images-HT/starwars/Dagobah-Dark/large/bossk.gif?raw=true",
        "type": "Character",
        "subType": "Alien",
        "uniqueness": "*",
        "destiny": 1,
        "power": 4,
        "ability": 2,
        "deploy": 4,
        "forfeit": 3,
        "icons": [
          "Pilot",
          "Warrior"
        ],
        "characteristics": [
          "Black Sun agent",
          "bounty hunter"
        ],
        "gametext": "Adds 2 to power of anything he pilots. When piloting Hound's Tooth, draws one battle destiny if not able to otherwise. Adds 1 to attrition against opponent in battles at same site. While present, may reduce Chewie's forfeit to zero while here.",
        "lore": "Male Trandoshan bounty hunter. Strong but clumsy. Extremely proud and arrogant. Suffered a humiliating defeat at the hands of Chewbacca and his partner Han Solo."
      },
      "pulledBy": [
        "Double Back"
      ],
      "combo": [
        "Bossk + Luke? Luuuuke! + any Chewbacca Makes Chewbacca Lost. (Indiana Jones Jedi Knight)"
      ],
      "matching": [
        "Hound's Tooth"
      ],
      "matchingWeapon": [
        "Bossk's Mortar Gun"
      ],
      "legacy": false
     */





//    val ability: Int?,
//    var armor: Int?,
//    var characteristics: String?,
//    var clone_army: Boolean?,
//    var code: String?,
//    var deploy: Int?,
//    var destiny: Int?,
//    var episode_1: Boolean?,
//    var episode_7: Boolean?,
//    var force_aptitude: String?,
//    var forfeit: Int?,
//    var gametext: String?,
//    var has_errata: Boolean?,
//    var image_url: String?,
//    var image_url_2: String?,
//    var lore: String?,
//    var maneuver: Int?,
//    var model_type: String?,
//    var name: String?,
//    var nav_computer: Boolean?,
//    var permanent_weapon: Boolean?,
//    var pilot: Boolean?,
//    var politics: Int?,
//    var position: Int?,
//    var power: Int?,
//    var presence: Boolean?,
//    var rarity_code: String?,
//    var republic: Boolean?,
//    var separatist: Boolean?,
//    var set_code: String?,
//    var side_code: String?,
//    var subtype_code: String?,
//    var type_code: String?,
//    var uniqueness: String?,
//    var warrior: Boolean?
) : Serializable, Comparable<SWCCGCard> {

    val imageUrlLarge: String
        get() = IMAGE_URL_PREFIX + image_url

    val imageUrlSmall: String
        get() = IMAGE_URL_PREFIX + image_url?.replace("large/", "t_")

    val imageUrl2Large: String
        get() = IMAGE_URL_PREFIX + image_url_2

    val imageUrl2Small: String
        get() = IMAGE_URL_PREFIX + image_url_2?.replace("large/", "t_")

    val hasImageUrl2: Boolean
        get() = !image_url_2.isNullOrBlank()

    companion object {
        val IMAGE_URL_PREFIX =
            "https://www.starwarsccg.org/wp/wp-content/plugins/card-search/cards/starwars/"
    }

    override fun compareTo(other: SWCCGCard): Int {
        val thisName = name
        val otherName = other.name

        if (thisName.isNullOrBlank() && otherName.isNullOrBlank()) {
            return 0;
        }

        if (thisName.isNullOrBlank()) {
            return -1
        }

        if (otherName.isNullOrBlank()) {
            return 1
        }

        return thisName.compareTo(otherName)
    }
}

