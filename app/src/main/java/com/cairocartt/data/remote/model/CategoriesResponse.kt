package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CategoriesResponse(
    @SerializedName("data")
    var `data`: List<DataCategory>?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class DataCategory(
        @SerializedName("children")
        var childrenData: MutableList<ChildrenDataa>?,
        @SerializedName("content")
        var id: String?,
//        @SerializedName("image")
//        var image: String?,
        @SerializedName("is_active")
        var isActive: Int?,
        @SerializedName("level")
        var level: Int?,
        @SerializedName("title")
        var name: String?,
        @SerializedName("parent_id")
        var parentId: Int?,
        @SerializedName("position")
        var position: Int?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class ChildrenDataa(
            @SerializedName("children")
            var childrenData: MutableList<ChildrenDataa>,
            @SerializedName("menu_id")
            var id: Int?,
//            @SerializedName("image")
//            var image: String?,
            @SerializedName("is_active")
            var isActive: Int?,
            @SerializedName("level")
            var level: Int?,
            @SerializedName("title")
            var name: String?,
            @SerializedName("parent_id")
            var parentId: Int?,
            @SerializedName("position")
            var position: Int?
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