package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class AddToCartResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("status")
    var status: Status
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("base_grand_total")
        var baseGrandTotal: Double?,
        @SerializedName("billing_address")
        var billingAddress: BillingAddress?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("currency")
        var currency: Currency?,
        @SerializedName("customer")
        var customer: Customer?,
        @SerializedName("customer_is_guest")
        var customerIsGuest: Boolean?,
        @SerializedName("customer_note_notify")
        var customerNoteNotify: Boolean?,
        @SerializedName("customer_tax_class_id")
        var customerTaxClassId: Int?,
        @SerializedName("extension_attributes")
        var extensionAttributes: ExtensionAttributes?,
        @SerializedName("grand_total")
        var grandTotal: Double?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("is_active")
        var isActive: Boolean?,
        @SerializedName("is_virtual")
        var isVirtual: Boolean?,
        @SerializedName("items")
        var items: List<Item?>?,
        @SerializedName("items_count")
        var itemsCount: Int?,
        @SerializedName("items_qty")
        var itemsQty: Int?,
        @SerializedName("orig_order_id")
        var origOrderId: Int?,
        @SerializedName("store_id")
        var storeId: Int?,
        @SerializedName("subtotal")
        var subtotal: Int?,
        @SerializedName("updated_at")
        var updatedAt: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class BillingAddress(

            @SerializedName("same_as_billing")
            var sameAsBilling: Int?,
            @SerializedName("save_in_address_book")
            var saveInAddressBook: Int?,
            @SerializedName("street")
            var street: List<String?>?
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Currency(
            @SerializedName("base_currency_code")
            var baseCurrencyCode: String?,
            @SerializedName("base_to_global_rate")
            var baseToGlobalRate: Int?,
            @SerializedName("base_to_quote_rate")
            var baseToQuoteRate: Int?,
            @SerializedName("global_currency_code")
            var globalCurrencyCode: String?,
            @SerializedName("quote_currency_code")
            var quoteCurrencyCode: String?,
            @SerializedName("store_currency_code")
            var storeCurrencyCode: String?,
            @SerializedName("store_to_base_rate")
            var storeToBaseRate: Int?,
            @SerializedName("store_to_quote_rate")
            var storeToQuoteRate: Int?
        ) : Parcelable

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

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class ExtensionAttributes(
            @SerializedName("shipping_assignments")
            var shippingAssignments: List<ShippingAssignment?>?
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class ShippingAssignment(
                @SerializedName("items")
                var items: List<Item?>?,
                @SerializedName("shipping")
                var shipping: Shipping?
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Item(
                    @SerializedName("item_id")
                    var itemId: Int?,
                    @SerializedName("name")
                    var name: String?,
                    @SerializedName("price")
                    var price: Int?,
                    @SerializedName("product_type")
                    var productType: String?,
                    @SerializedName("qty")
                    var qty: Int?,
                    @SerializedName("quote_id")
                    var quoteId: String?,
                    @SerializedName("sku")
                    var sku: String?
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Shipping(
                    @SerializedName("address")
                    var address: Address?

                ) : Parcelable {
                    @SuppressLint("ParcelCreator")
                    @Parcelize
                    data class Address(
                        @SerializedName("customer_id")
                        var customerId: Int?,
                        @SerializedName("email")
                        var email: String?,
                        @SerializedName("same_as_billing")
                        var sameAsBilling: Int?,
                        @SerializedName("save_in_address_book")
                        var saveInAddressBook: Int?,
                        @SerializedName("street")
                        var street: List<String?>?

                    ) : Parcelable
                }
            }
        }

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Item(
            @SerializedName("image")
            var image: String?,
            @SerializedName("item_id")
            var itemId: Int?,
            @SerializedName("max_qty")
            var maxQty: Int?,
            @SerializedName("min_qty")
            var minQty: Int?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("price")
            var price: Int?,
            @SerializedName("product_type")
            var productType: String?,
            @SerializedName("qty")
            var qty: Int?,
            @SerializedName("quote_id")
            var quoteId: String?,
            @SerializedName("sku")
            var sku: String?
        ) : Parcelable
    }

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Status(
        @SerializedName("code")
        var code: Int?,
        @SerializedName("message")
        var message: String
    ) : Parcelable
}