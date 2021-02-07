package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class RequestShippingMethodResponse(
    @SerializedName("address")
    var address: Address?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Address(
        @SerializedName("country_id")
        var countryId: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("region")
        var region: String?,
        @SerializedName("region_code")
        var regionCode: String?,
        @SerializedName("region_id")
        var regionId: Int?
    ) : Parcelable
}