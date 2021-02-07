package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class RequestShippingAddressModel(
    @SerializedName("address_information")
    var addressInformation: AddressInformation?=null
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class AddressInformation(
        @SerializedName("shipping_address")
        var shippingAddress: ShippingAddress?=null,
        @SerializedName("billing_address")
        var billingAddress: BillingAddress?,
        @SerializedName("shipping_carrier_code")
        var shippingCarrierCode: String?=null,
        @SerializedName("shipping_method_code")
        var shippingMethodCode: String?=null
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class ShippingAddress(
            @SerializedName("city")
            var city: String?,
            @SerializedName("country_id")
            var countryId: String?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("firstname")
            var firstname: String?,
            @SerializedName("lastname")
            var lastname: String?,
            @SerializedName("region")
            var region: String?,
            @SerializedName("region_code")
            var regionCode: String?,
            @SerializedName("region_id")
            var regionId: Int?,
            @SerializedName("telephone")
            var telephone: String?,
            @SerializedName("street")
            var street: MutableList<String>?
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class BillingAddress(
            @SerializedName("city")
            var city: String?,
            @SerializedName("country_id")
            var countryId: String?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("firstname")
            var firstname: String?,
            @SerializedName("lastname")
            var lastname: String?,
            @SerializedName("region")
            var region: String?,
            @SerializedName("region_code")
            var regionCode: String?,
            @SerializedName("region_id")
            var regionId: Int?,
            @SerializedName("telephone")
            var telephone: String?,
            @SerializedName("street")
            var street: MutableList<String>?
        ) : Parcelable
    }
}