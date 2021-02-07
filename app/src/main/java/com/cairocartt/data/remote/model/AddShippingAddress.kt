package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddShippingAddress(
    @SerializedName("address_information")
    var addressInformation: AddressInformation?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class AddressInformation(
        @SerializedName("shipping_address")
        var shippingAddress: ShippingAddress?,
        @SerializedName("shipping_carrier_code")
        var shippingCarrierCode: String?,
        @SerializedName("shipping_method_code")
        var shippingMethodCode: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class ShippingAddress(
            @SerializedName("country_id")
            var countryId: String?,
            @SerializedName("region")
            var region: String?,
            @SerializedName("region_code")
            var regionCode: String?,
            @SerializedName("region_id")
            var regionId: Int?,
            @SerializedName("street")
            var street: List<String?>?
        ) : Parcelable
    }
}