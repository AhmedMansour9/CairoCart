package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class RequestAddToCartResponse(
    @SerializedName("cartItem")
    var cartItem: CartItem?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class CartItem(
        @SerializedName("qty")
        var qty: Int,
        @SerializedName("sku")
        var sku: String,
        @SerializedName("quote_id")
        var quote_id: String?
    ) : Parcelable
}