package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class FilterResponse(
    @SerializedName("data")
    var `data`: MutableList<Data>,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("field")
        var `field`: String,
        @SerializedName("label")
        var label: String?,
        @SerializedName("values")
        var values: MutableList<Value>
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Value(
            @SerializedName("checked")
            var checked: Boolean?,
            @SerializedName("count")
            var count: Int?,
            @SerializedName("label")
            var label: String?,
            @SerializedName("value")
            var value: String?
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