package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CategoriesResponse(
    @SerializedName("data")
    var `data`: DataCategory?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class DataCategory(
        @SerializedName("children_data")
        var childrenData: List<ChildrenDataa?>?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("is_active")
        var isActive: Boolean?,
        @SerializedName("level")
        var level: Int?,
        @SerializedName("name")
        var name: String?,
        @SerializedName("parent_id")
        var parentId: Int?,
        @SerializedName("position")
        var position: Int?,
        @SerializedName("product_count")
        var productCount: Int?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class ChildrenDataa(
            @SerializedName("children_data")
            var childrenData: MutableList<ChildrenDataa>,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("is_active")
            var isActive: Boolean?,
            @SerializedName("level")
            var level: Int?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("parent_id")
            var parentId: Int?,
            @SerializedName("position")
            var position: Int?,
            @SerializedName("product_count")
            var productCount: Int?
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