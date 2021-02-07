package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import android.text.TextUtils

@SuppressLint("ParcelCreator")
@Parcelize
data class RequestAddReviewResponse(
    @SerializedName("review")
    var review: Review?=null
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Review(
        @SerializedName("customer_id")
        var customerId: String="",
        @SerializedName("detail")
        var detail: String="",
        @SerializedName("entity_pk_value")
        var entityPkValue: Int=0,
        @SerializedName("nickname")
        var nickname: String="",
        @SerializedName("ratings")
        var ratings: MutableList<Rating>?=null,
        @SerializedName("review_entity")
        var reviewEntity: String="",
        @SerializedName("review_status")
        var reviewStatus: Int=0,
        @SerializedName("title")
        var title: String=""
    ) : Parcelable {
        fun empty() = TextUtils.isEmpty(detail) && TextUtils.isEmpty(nickname) && TextUtils.isEmpty(title)

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Rating(
            @SerializedName("rating_name")
            var ratingName: String?,
            @SerializedName("value")
            var value: Int?
        ) : Parcelable
    }

    fun empty() =
         review?.empty()!!
}