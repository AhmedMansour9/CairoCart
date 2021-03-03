package com.cairocartt.data.remote.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PayFortData {

    var paymentResponse = ""

    //Response Params
    @SerializedName("access_code")
    @Expose
    var accessCode: String? = null

    @SerializedName("sdk_token")
    @Expose
    var sdkToken: String? = null

    @SerializedName("response_message")
    @Expose
    var responseMessage: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("response_code")
    @Expose
    var responseCode: String? = null

    @SerializedName("device_id")
    @Expose
    var deviceId: String? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("service_command")
    @Expose
    var serviceCommand: String? = null

    @SerializedName("signature")
    @Expose
    var signature: String? = null

    @SerializedName("merchant_identifier")
    @Expose
    var merchantIdentifier: String? = null


    @SerializedName("eci")
    @Expose
    var eci: String? = null

    @SerializedName("card_number")
    @Expose
    var cardNumber: String? = null

    @SerializedName("fort_id")
    @Expose
    var fortId: String? = null

    @SerializedName("customer_email")
    @Expose
    var customerEmail: String? = null

    @SerializedName("customer_ip")
    @Expose
    var customerIp: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("amount")
    @Expose
    var amount: String? = null

    @SerializedName("merchant_reference")
    @Expose
    var merchantReference: String? = null

    @SerializedName("command")
    @Expose
    var command: String? = null

    @SerializedName("payment_option")
    @Expose
    var paymentOption: String? = null

    @SerializedName("expiry_date")
    @Expose
    var expiryDate: String? = null

    @SerializedName("authorization_code")
    @Expose
    var authorizationCode: String? = null
}