package com.zumer.direct4mechallenge.uiLayer.enum

enum class Country(val countryName: String, val countryCode: String) {
    SLOVENIA("Slovenija", "si"),
    ITALY("Italia", "it"),
    GERMANY("Deutschland", "de"),
    GREAT_BRITAIN("Great Britain", "gb");

    override fun toString(): String {
        return countryName
    }
}