package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class RequestCreateOrder(
    @SerializedName("email")
    var email: String?,
    @SerializedName("paymentMethod")
    var paymentMethod: PaymentMethod?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class PaymentMethod(
        @SerializedName("method")
        var method: String?
    ) : Parcelable
}