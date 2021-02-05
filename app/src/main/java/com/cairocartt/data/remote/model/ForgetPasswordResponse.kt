package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ForgetPasswordResponse(
    @SerializedName("status")
    var status: Status
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Status(
        @SerializedName("code")
        var code: Int,
        @SerializedName("message")
        var message: String?
    ) : Parcelable
}