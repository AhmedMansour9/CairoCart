package com.cairocartt.data.remote.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class MyOrdersResponse(
    @SerializedName("data")
    var `data`: MutableList<Data>,
    @SerializedName("status")
    var status: Status?
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Data(
        @SerializedName("applied_rule_ids")
        var appliedRuleIds: String?,
        @SerializedName("base_currency_code")
        var baseCurrencyCode: String?,
        @SerializedName("base_discount_amount")
        var baseDiscountAmount: Double?,
        @SerializedName("base_discount_tax_compensation_amount")
        var baseDiscountTaxCompensationAmount: Int?,
        @SerializedName("base_grand_total")
        var baseGrandTotal: Double?,
        @SerializedName("base_shipping_amount")
        var baseShippingAmount: Int?,
        @SerializedName("base_shipping_discount_amount")
        var baseShippingDiscountAmount: Int?,
        @SerializedName("base_shipping_discount_tax_compensation_amnt")
        var baseShippingDiscountTaxCompensationAmnt: Int?,
        @SerializedName("base_shipping_incl_tax")
        var baseShippingInclTax: Int?,
        @SerializedName("base_shipping_tax_amount")
        var baseShippingTaxAmount: Int?,
        @SerializedName("base_subtotal")
        var baseSubtotal: Int?,
        @SerializedName("base_subtotal_incl_tax")
        var baseSubtotalInclTax: Int?,
        @SerializedName("base_tax_amount")
        var baseTaxAmount: Int?,
        @SerializedName("base_to_global_rate")
        var baseToGlobalRate: Int?,
        @SerializedName("base_to_order_rate")
        var baseToOrderRate: Int?,
        @SerializedName("base_total_due")
        var baseTotalDue: Double?,
        @SerializedName("billing_address")
        var billingAddress: BillingAddress?,
        @SerializedName("billing_address_id")
        var billingAddressId: Int?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("customer_email")
        var customerEmail: String?,
        @SerializedName("customer_firstname")
        var customerFirstname: String?,
        @SerializedName("customer_group_id")
        var customerGroupId: Int?,
        @SerializedName("customer_id")
        var customerId: Int?,
        @SerializedName("customer_is_guest")
        var customerIsGuest: Int?,
        @SerializedName("customer_lastname")
        var customerLastname: String?,
        @SerializedName("customer_note_notify")
        var customerNoteNotify: Int?,
        @SerializedName("discount_amount")
        var discountAmount: Double?,
        @SerializedName("discount_description")
        var discountDescription: String?,
        @SerializedName("discount_tax_compensation_amount")
        var discountTaxCompensationAmount: Int?,
        @SerializedName("email_sent")
        var emailSent: Int?,
        @SerializedName("entity_id")
        var entityId: Int?,
        @SerializedName("global_currency_code")
        var globalCurrencyCode: String?,
        @SerializedName("grand_total")
        var grandTotal: Double?,
        @SerializedName("increment_id")
        var incrementId: String?,
        @SerializedName("is_virtual")
        var isVirtual: Int?,
        @SerializedName("items")
        var items: List<Item?>?,
        @SerializedName("mobile_items")
        var mobileItems: MutableList<MobileItem>,
        @SerializedName("order_currency_code")
        var orderCurrencyCode: String?,
        @SerializedName("payment")
        var payment: Payment?,
        @SerializedName("protect_code")
        var protectCode: String?,
        @SerializedName("quote_id")
        var quoteId: Int?,
        @SerializedName("remote_ip")
        var remoteIp: String?,
        @SerializedName("shipping_amount")
        var shippingAmount: Int?,
        @SerializedName("shipping_description")
        var shippingDescription: String?,
        @SerializedName("shipping_discount_amount")
        var shippingDiscountAmount: Int?,
        @SerializedName("shipping_discount_tax_compensation_amount")
        var shippingDiscountTaxCompensationAmount: Int?,
        @SerializedName("shipping_incl_tax")
        var shippingInclTax: Int?,
        @SerializedName("shipping_tax_amount")
        var shippingTaxAmount: Int?,
        @SerializedName("state")
        var state: String?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("store_currency_code")
        var storeCurrencyCode: String?,
        @SerializedName("store_id")
        var storeId: Int?,
        @SerializedName("store_name")
        var storeName: String?,
        @SerializedName("store_to_base_rate")
        var storeToBaseRate: Int?,
        @SerializedName("store_to_order_rate")
        var storeToOrderRate: Int?,
        @SerializedName("subtotal")
        var subtotal: Int?,
        @SerializedName("subtotal_incl_tax")
        var subtotalInclTax: Int?,
        @SerializedName("summery")
        var summery: List<Summery?>?,
        @SerializedName("tax_amount")
        var taxAmount: Int?,
        @SerializedName("total_due")
        var totalDue: Double?,
        @SerializedName("total_item_count")
        var totalItemCount: Int?,
        @SerializedName("total_qty_ordered")
        var totalQtyOrdered: Int?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("weight")
        var weight: Int?,
        @SerializedName("x_forwarded_for")
        var xForwardedFor: String?
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class BillingAddress(
            @SerializedName("address_type")
            var addressType: String?,
            @SerializedName("city")
            var city: String?,
            @SerializedName("company")
            var company: String?,
            @SerializedName("country_id")
            var countryId: String?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("entity_id")
            var entityId: Int?,
            @SerializedName("fax")
            var fax: String?,
            @SerializedName("firstname")
            var firstname: String?,
            @SerializedName("lastname")
            var lastname: String?,
            @SerializedName("middlename")
            var middlename: String?,
            @SerializedName("parent_id")
            var parentId: Int?,
            @SerializedName("postcode")
            var postcode: String?,
            @SerializedName("prefix")
            var prefix: String?,
            @SerializedName("region")
            var region: String?,
            @SerializedName("region_code")
            var regionCode: String?,
            @SerializedName("region_id")
            var regionId: Int?,
            @SerializedName("street")
            var street: List<String?>?,
            @SerializedName("suffix")
            var suffix: String?,
            @SerializedName("telephone")
            var telephone: String?,
            @SerializedName("vat_id")
            var vatId: String?
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Item(
            @SerializedName("amount_refunded")
            var amountRefunded: Int?,
            @SerializedName("applied_rule_ids")
            var appliedRuleIds: String?,
            @SerializedName("base_amount_refunded")
            var baseAmountRefunded: Int?,
            @SerializedName("base_discount_amount")
            var baseDiscountAmount: Double?,
            @SerializedName("base_discount_invoiced")
            var baseDiscountInvoiced: Int?,
            @SerializedName("base_discount_tax_compensation_amount")
            var baseDiscountTaxCompensationAmount: Int?,
            @SerializedName("base_original_price")
            var baseOriginalPrice: Int?,
            @SerializedName("base_price")
            var basePrice: Int?,
            @SerializedName("base_price_incl_tax")
            var basePriceInclTax: Int?,
            @SerializedName("base_row_invoiced")
            var baseRowInvoiced: Int?,
            @SerializedName("base_row_total")
            var baseRowTotal: Double?,
            @SerializedName("base_row_total_incl_tax")
            var baseRowTotalInclTax: Double?,
            @SerializedName("base_tax_amount")
            var baseTaxAmount: Int?,
            @SerializedName("base_tax_invoiced")
            var baseTaxInvoiced: Int?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("discount_amount")
            var discountAmount: Double?,
            @SerializedName("discount_invoiced")
            var discountInvoiced: Int?,
            @SerializedName("discount_percent")
            var discountPercent: Int?,
            @SerializedName("discount_tax_compensation_amount")
            var discountTaxCompensationAmount: Int?,
            @SerializedName("free_shipping")
            var freeShipping: Int?,
            @SerializedName("is_qty_decimal")
            var isQtyDecimal: Int?,
            @SerializedName("is_virtual")
            var isVirtual: Int?,
            @SerializedName("item_id")
            var itemId: Int?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("no_discount")
            var noDiscount: Int?,
            @SerializedName("order_id")
            var orderId: Int?,
            @SerializedName("original_price")
            var originalPrice: Int?,
            @SerializedName("price")
            var price: Int?,
            @SerializedName("price_incl_tax")
            var priceInclTax: Int?,
            @SerializedName("product_id")
            var productId: Int?,
            @SerializedName("product_type")
            var productType: String?,
            @SerializedName("qty_canceled")
            var qtyCanceled: Int?,
            @SerializedName("qty_invoiced")
            var qtyInvoiced: Int?,
            @SerializedName("qty_ordered")
            var qtyOrdered: Int?,
            @SerializedName("qty_refunded")
            var qtyRefunded: Int?,
            @SerializedName("qty_shipped")
            var qtyShipped: Int?,
            @SerializedName("quote_item_id")
            var quoteItemId: Int?,
            @SerializedName("row_invoiced")
            var rowInvoiced: Int?,
            @SerializedName("row_total")
            var rowTotal: Double?,
            @SerializedName("row_total_incl_tax")
            var rowTotalInclTax: Double?,
            @SerializedName("row_weight")
            var rowWeight: Int?,
            @SerializedName("sku")
            var sku: String?,
            @SerializedName("store_id")
            var storeId: Int?,
            @SerializedName("tax_amount")
            var taxAmount: Int?,
            @SerializedName("tax_invoiced")
            var taxInvoiced: Int?,
            @SerializedName("tax_percent")
            var taxPercent: Int?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("weight")
            var weight: Int?
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class MobileItem(
            @SerializedName("amount_refunded")
            var amountRefunded: Int?,
            @SerializedName("applied_rule_ids")
            var appliedRuleIds: String?,
            @SerializedName("base_amount_refunded")
            var baseAmountRefunded: Int?,
            @SerializedName("base_discount_amount")
            var baseDiscountAmount: Double?,
            @SerializedName("base_discount_invoiced")
            var baseDiscountInvoiced: Int?,
            @SerializedName("base_discount_tax_compensation_amount")
            var baseDiscountTaxCompensationAmount: Int?,
            @SerializedName("base_original_price")
            var baseOriginalPrice: Int?,
            @SerializedName("base_price")
            var basePrice: Int?,
            @SerializedName("base_price_incl_tax")
            var basePriceInclTax: Int?,
            @SerializedName("base_row_invoiced")
            var baseRowInvoiced: Int?,
            @SerializedName("base_row_total")
            var baseRowTotal: Double?,
            @SerializedName("base_row_total_incl_tax")
            var baseRowTotalInclTax: Double?,
            @SerializedName("base_tax_amount")
            var baseTaxAmount: Int?,
            @SerializedName("base_tax_invoiced")
            var baseTaxInvoiced: Int?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("discount_amount")
            var discountAmount: Double?,
            @SerializedName("discount_invoiced")
            var discountInvoiced: Int?,
            @SerializedName("discount_percent")
            var discountPercent: Int?,
            @SerializedName("discount_tax_compensation_amount")
            var discountTaxCompensationAmount: Int?,
            @SerializedName("free_shipping")
            var freeShipping: Int?,
            @SerializedName("is_qty_decimal")
            var isQtyDecimal: Int?,
            @SerializedName("is_virtual")
            var isVirtual: Int?,
            @SerializedName("item_id")
            var itemId: Int?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("no_discount")
            var noDiscount: Int?,
            @SerializedName("order_id")
            var orderId: Int?,
            @SerializedName("original_price")
            var originalPrice: Int?,
            @SerializedName("price")
            var price: Int?,
            @SerializedName("price_incl_tax")
            var priceInclTax: Int?,
            @SerializedName("product_id")
            var productId: Int?,
            @SerializedName("product_type")
            var productType: String?,
            @SerializedName("qty_canceled")
            var qtyCanceled: Int?,
            @SerializedName("qty_invoiced")
            var qtyInvoiced: Int?,
            @SerializedName("qty_ordered")
            var qtyOrdered: Int?,
            @SerializedName("qty_refunded")
            var qtyRefunded: Int?,
            @SerializedName("qty_shipped")
            var qtyShipped: Int?,
            @SerializedName("quote_item_id")
            var quoteItemId: Int?,
            @SerializedName("row_invoiced")
            var rowInvoiced: Int?,
            @SerializedName("row_total")
            var rowTotal: Double?,
            @SerializedName("row_total_incl_tax")
            var rowTotalInclTax: Double?,
            @SerializedName("row_weight")
            var rowWeight: Int?,
            @SerializedName("sku")
            var sku: String?,
            @SerializedName("store_id")
            var storeId: Int?,
            @SerializedName("tax_amount")
            var taxAmount: Int?,
            @SerializedName("tax_invoiced")
            var taxInvoiced: Int?,
            @SerializedName("tax_percent")
            var taxPercent: Int?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("weight")
            var weight: Int?
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Payment(
            @SerializedName("additional_information")
            var additionalInformation: List<String?>?,
            @SerializedName("amount_ordered")
            var amountOrdered: Double?,
            @SerializedName("base_amount_ordered")
            var baseAmountOrdered: Double?,
            @SerializedName("base_shipping_amount")
            var baseShippingAmount: Int?,
            @SerializedName("cc_exp_year")
            var ccExpYear: String?,
            @SerializedName("cc_ss_start_month")
            var ccSsStartMonth: String?,
            @SerializedName("cc_ss_start_year")
            var ccSsStartYear: String?,
            @SerializedName("entity_id")
            var entityId: Int?,
            @SerializedName("method")
            var method: String?,
            @SerializedName("method_instance")
            var methodInstance: MethodInstance?,
            @SerializedName("parent_id")
            var parentId: Int?,
            @SerializedName("shipping_amount")
            var shippingAmount: Int?
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class MethodInstance(
                @SerializedName("code")
                var code: String?,
                @SerializedName("gateway")
                var gateway: Boolean?,
                @SerializedName("offline")
                var offline: Boolean?,
                @SerializedName("title")
                var title: String?
            ) : Parcelable
        }

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Summery(
            @SerializedName("code")
            var code: String?,
            @SerializedName("is_total")
            var isTotal: Boolean?,
            @SerializedName("title")
            var title: String?,
            @SerializedName("value")
            var value: String?
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