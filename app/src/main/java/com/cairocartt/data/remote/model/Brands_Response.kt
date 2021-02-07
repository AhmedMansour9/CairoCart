package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Brands_Response(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("cover")
        var cover: String?,
        @SerializedName("is_active")
        var isActive: Boolean?,
        @SerializedName("option_id")
        var optionId: Int?,
        @SerializedName("thumbnail")
        var thumbnail: String?,
        @SerializedName("title")
        var title: String?
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Status(
        @SerializedName("code")
        var code: Int?,
        @SerializedName("message")
        var message: String?
    ) : Parcelable
}