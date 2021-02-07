package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddGustCartResponse(
    @SerializedName("cartItem")
    var cartItem: CartItem?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class CartItem(
        @SerializedName("qty")
        var qty: Int?,
        @SerializedName("quote_id")
        var quoteId: String?,
        @SerializedName("sku")
        var sku: String?
    ) : Parcelable
}