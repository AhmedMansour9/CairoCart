package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ShippingAddressResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("payment_methods")
        var paymentMethods: List<PaymentMethod?>?,
        @SerializedName("totals")
        var totals: Totals?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class PaymentMethod(
            @SerializedName("code")
            var code: String?,
            @SerializedName("title")
            var title: String?
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Totals(
            @SerializedName("base_currency_code")
            var baseCurrencyCode: String?,
            @SerializedName("base_discount_amount")
            var baseDiscountAmount: Double?,
            @SerializedName("base_grand_total")
            var baseGrandTotal: Double?,
            @SerializedName("base_shipping_amount")
            var baseShippingAmount: Double?,
            @SerializedName("base_shipping_discount_amount")
            var baseShippingDiscountAmount: Double?,
            @SerializedName("base_shipping_incl_tax")
            var baseShippingInclTax: Double?,
            @SerializedName("base_shipping_tax_amount")
            var baseShippingTaxAmount: Double?,
            @SerializedName("base_subtotal")
            var baseSubtotal: Double?,
            @SerializedName("base_subtotal_with_discount")
            var baseSubtotalWithDiscount: Double?,
            @SerializedName("base_tax_amount")
            var baseTaxAmount: Double?,
            @SerializedName("discount_amount")
            var discountAmount: Double?,
            @SerializedName("grand_total")
            var grandTotal: Double?,
            @SerializedName("items")
            var items: List<Item?>?,
            @SerializedName("items_qty")
            var itemsQty: Int?,
            @SerializedName("quote_currency_code")
            var quoteCurrencyCode: String?,
            @SerializedName("shipping_amount")
            var shippingAmount: Double?,
            @SerializedName("shipping_discount_amount")
            var shippingDiscountAmount: Double?,
            @SerializedName("shipping_incl_tax")
            var shippingInclTax: Double?,
            @SerializedName("shipping_tax_amount")
            var shippingTaxAmount: Double?,
            @SerializedName("subtotal")
            var subtotal: Double?,
            @SerializedName("subtotal_incl_tax")
            var subtotalInclTax: Double?,
            @SerializedName("subtotal_with_discount")
            var subtotalWithDiscount: Double?,
            @SerializedName("tax_amount")
            var taxAmount: Double?
//            @SerializedName("total_segments")
//            var totalSegments: List<TotalSegment?>?
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class Item(
                @SerializedName("base_discount_amount")
                var baseDiscountAmount: Double?,
                @SerializedName("base_price")
                var basePrice: Double?,
                @SerializedName("base_price_incl_tax")
                var basePriceInclTax: Double?,
                @SerializedName("base_row_total")
                var baseRowTotal: Double?,
                @SerializedName("base_row_total_incl_tax")
                var baseRowTotalInclTax: Double?,
                @SerializedName("base_tax_amount")
                var baseTaxAmount: Double?,
                @SerializedName("discount_amount")
                var discountAmount: Double?,
                @SerializedName("discount_percent")
                var discountPercent: Double?,
                @SerializedName("item_id")
                var itemId: Int?,
                @SerializedName("name")
                var name: String?,
                @SerializedName("options")
                var options: String?,
                @SerializedName("price")
                var price: Double?,
                @SerializedName("price_incl_tax")
                var priceInclTax: Double?,
                @SerializedName("qty")
                var qty: Int?,
                @SerializedName("row_total")
                var rowTotal: Double?,
                @SerializedName("row_total_incl_tax")
                var rowTotalInclTax: Double?,
                @SerializedName("row_total_with_discount")
                var rowTotalWithDiscount: Double?,
                @SerializedName("tax_amount")
                var taxAmount: Double?,
                @SerializedName("tax_percent")
                var taxPercent: Int?
            ) : Parcelable

            @SuppressLint("ParcelCreator")
            @Parcelize
            data class TotalSegment(
                @SerializedName("area")
                var area: String?,
                @SerializedName("code")
                var code: String?,
                @SerializedName("title")
                var title: String?,
                @SerializedName("value")
                var value: Int?
            ) : Parcelable {

            }
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