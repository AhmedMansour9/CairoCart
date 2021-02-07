package com.cairocartt.data.remote.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.text.TextUtils
import android.util.Patterns.*
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class RegisterRequest(
    @SerializedName("customer")
    var customer: Customer? = null,
    @SerializedName("password")
    var password: String = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Customer(
        @SerializedName("email")
        var email: String = "",
        @SerializedName("firstname")
        var firstname: String = "",
        @SerializedName("lastname")
        var lastname: String = ""
    ) : Parcelable {
        fun empty() =
            TextUtils.isEmpty(email) && TextUtils.isEmpty(firstname) && TextUtils.isEmpty(lastname) && !EMAIL_ADDRESS.matcher(email).matches()
    }
    fun empty() =
        TextUtils.isEmpty(password) && customer?.empty()!!

}