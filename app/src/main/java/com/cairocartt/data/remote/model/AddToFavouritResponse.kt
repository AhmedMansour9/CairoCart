package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddToFavouritResponse(
    @SerializedName("data")
    var `data`: List<Data?>?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("added_at")
        var addedAt: String?,
        @SerializedName("product")
        var product: Product?,
        @SerializedName("product_id")
        var productId: Int?,
        @SerializedName("qty")
        var qty: String?,
        @SerializedName("store_id")
        var storeId: Int?,
        @SerializedName("wishlist_id")
        var wishlistId: Int?,
        @SerializedName("wishlist_item_id")
        var wishlistItemId: Int?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Product(
            @SerializedName("attribute_set_id")
            var attributeSetId: Int?,
            @SerializedName("cart_max_allowed")
            var cartMaxAllowed: Int?,
            @SerializedName("cart_min_allowed")
            var cartMinAllowed: Int?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("custom_attributes")
            var customAttributes: List<CustomAttribute?>?,

            @SerializedName("final_price")
            var finalPrice: Int?,
            @SerializedName("has_cross_sell_products")
            var hasCrossSellProducts: Boolean?,
            @SerializedName("has_related_products")
            var hasRelatedProducts: Boolean?,
            @SerializedName("has_reviews")
            var hasReviews: Boolean?,
            @SerializedName("has_up_sell_products")
            var hasUpSellProducts: Boolean?,
            @SerializedName("id")
            var id: Int?,
            @SerializedName("image_url")
            var imageUrl: String?,
            @SerializedName("is_in_wishlist")
            var isInWishlist: Boolean?,
            @SerializedName("is_salable")
            var isSalable: Boolean?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("price")
            var price: Int?,
            @SerializedName("product_custom_attributes")
            var productCustomAttributes: List<ProductCustomAttribute?>?,
            @SerializedName("qty")
            var qty: Int?,
            @SerializedName("sku")
            var sku: String?,
            @SerializedName("status")
            var status: Int?,
            @SerializedName("type_id")
            var typeId: String?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("visibility")
            var visibility: Int?
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class CustomAttribute(
                @SerializedName("attribute_code")
                var attributeCode: String?,
                @SerializedName("value")
                var value: String?
            ) : Parcelable

            @SuppressLint("ParcelCreator")
            @Parcelize
            data class ProductCustomAttribute(
                @SerializedName("attribute_code")
                var attributeCode: String?,
                @SerializedName("label")
                var label: String?,
                @SerializedName("type")
                var type: String?,
                @SerializedName("value")
                var value: String?,
                @SerializedName("value_text")
                var valueText: String?
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