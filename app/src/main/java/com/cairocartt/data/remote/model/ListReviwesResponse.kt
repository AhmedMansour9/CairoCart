package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ListReviwesResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("customer_id")
        var customerId: Int?,
        @SerializedName("detail")
        var detail: String?,
        @SerializedName("entity_pk_value")
        var entityPkValue: Int?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("nickname")
        var nickname: String?,
        @SerializedName("ratings")
        var ratings: List<Rating>?,
        @SerializedName("review_entity")
        var reviewEntity: String?,
        @SerializedName("review_status")
        var reviewStatus: Int?,
        @SerializedName("review_type")
        var reviewType: Int?,
        @SerializedName("store_id")
        var storeId: Int?,
        @SerializedName("stores")
        var stores: List<Int?>?,
        @SerializedName("title")
        var title: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Rating(
            @SerializedName("percent")
            var percent: Int?,
            @SerializedName("rating_id")
            var ratingId: Int?,
            @SerializedName("rating_name")
            var ratingName: String,
            @SerializedName("value")
            var value: Int,
            @SerializedName("vote_id")
            var voteId: Int?
        ) : Parcelable
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