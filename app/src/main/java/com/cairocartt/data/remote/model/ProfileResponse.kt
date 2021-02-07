package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ProfileResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("account_token")
        var accountToken: String?,
        @SerializedName("customer")
        var customer: Customer?,
        @SerializedName("token")
        var token: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Customer(
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("created_in")
            var createdIn: String?,
            @SerializedName("disable_auto_group_change")
            var disableAutoGroupChange: Int?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("extension_attributes")
            var extensionAttributes: ExtensionAttributes?,
            @SerializedName("firstname")
            var firstname: String?,
            @SerializedName("group_id")
            var groupId: Int?,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("lastname")
            var lastname: String?,
            @SerializedName("store_id")
            var storeId: Int?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("website_id")
            var websiteId: Int?
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class ExtensionAttributes(
                @SerializedName("is_subscribed")
                var isSubscribed: Boolean?
            ) : Parcelable
        }
    }

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Status(
        @SerializedName("code")
        var code: Int?,
        @SerializedName("message")
        var message: String?
    ) : Parcelable
}