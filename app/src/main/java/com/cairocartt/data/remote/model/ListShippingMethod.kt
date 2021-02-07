package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ListShippingMethod(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("amount")
        var amount: Int?,
        @SerializedName("available")
        var available: Boolean?,
        @SerializedName("base_amount")
        var baseAmount: Int?,
        @SerializedName("carrier_code")
        var carrierCode: String?,
        @SerializedName("carrier_title")
        var carrierTitle: String?,
        @SerializedName("error_message")
        var errorMessage: String?,
        @SerializedName("method_code")
        var methodCode: String?,
        @SerializedName("price_excl_tax")
        var priceExclTax: Int?,
        @SerializedName("price_incl_tax")
        var priceInclTax: Int?
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