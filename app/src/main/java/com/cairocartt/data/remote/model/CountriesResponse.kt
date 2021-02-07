package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CountriesResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("available_regions")
        var availableRegions: List<AvailableRegion>?,
        @SerializedName("full_name_english")
        var fullNameEnglish: String?,
        @SerializedName("full_name_locale")
        var fullNameLocale: String,
        @SerializedName("id")
        var id: String?,
        @SerializedName("three_letter_abbreviation")
        var threeLetterAbbreviation: String?,
        @SerializedName("two_letter_abbreviation")
        var twoLetterAbbreviation: String?

    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class AvailableRegion(
            @SerializedName("code")
            var code: String?,
            @SerializedName("id")
            var id: String?,
            @SerializedName("name")
            var name: String

        ) : Parcelable {
            override fun toString(): String {
                return name
            }
        }

        override fun toString(): String {
            return fullNameLocale
        }
    }

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Status(
        @SerializedName("code")
        var code: Int?,
        @SerializedName("message")
        var message: String?
    ) : Parcelable
}