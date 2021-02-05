package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class HomeResponse(
    @SerializedName("data")
    var `data`: MutableList<Data>,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("cards")
        var cards: MutableList<Card>?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("is_active")
        var isActive: Boolean?,
        @SerializedName("show_label")
        var showLabel: Boolean?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("type")
        var type: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Card(
            @SerializedName("id")
            var id: Int?,
            @SerializedName("is_active")
            var isActive: Boolean?,
            @SerializedName("reference_id")
            var referenceId: Int?,
            @SerializedName("reference_type")
            var referenceType: String,
            @SerializedName("thumbnail")
            var thumbnail: String,
            @SerializedName("title")
            var title: String?
        ) : Parcelable
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