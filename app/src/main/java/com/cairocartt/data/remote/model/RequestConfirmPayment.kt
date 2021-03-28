package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class RequestConfirmPayment(
    @SerializedName("paymentCallback")
    var paymentCallback: PaymentCallback?=null
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class PaymentCallback(
        @SerializedName("authorization_code")
        var authorizationCode: String?=null,
        @SerializedName("card_number")
        var cardNumber: String?=null,
        @SerializedName("command")
        var command: String?=null,
        @SerializedName("customer_ip")
        var customerIp: String?=null,
        @SerializedName("eci")
        var eci: String?=null,
        @SerializedName("expiry_date")
        var expiryDate: String?=null,
        @SerializedName("fort_id")
        var fortId: String?=null,
        @SerializedName("language")
        var language: String?=null,
        @SerializedName("merchant_reference")
        var merchantReference: String?=null,
        @SerializedName("payment_option")
        var paymentOption: String?=null,
        @SerializedName("response_code")
        var responseCode: String?=null,
        @SerializedName("response_message")
        var responseMessage: String?=null,
        @SerializedName("sdk_token")
        var sdkToken: String?=null
    ) : Parcelable
}