package com.cairocartt.data.remote.api

import com.cairocartt.data.remote.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("en" + "/rest/V1/restapi/register")
     fun userRegister(@Body registerRequest: RegisterRequest
    ,  @Header("Access-Firebase")token_firebase:String?): Call<AccountResponse>

    @POST("en/rest/V1/restapi/login")
     fun userLogin(@Body loginRequest: LoginRequest,@Header("Access-Firebase")token_firebase:String?): Call<AccountResponse>

    @POST("en/rest/V1/restapi/login/social")
     fun loginFacebook(@Body map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?): Call<AccountResponse>

    @PUT("rest/V1/customers/password")
     fun forgetPassword(@Body map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?): Call<ForgetPasswordResponse>

    @GET("{language}" + "/rest/V1/mstore/categories")
     fun fetchCategories(
        @Path(
            value = "language",
            encoded = true
        ) language: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Call<CategoriesResponse>

    @GET("{language}" + "/rest/V1/mobilehomepage/rows")
    fun fetchHome(
        @Path(
            value = "language",
            encoded = true
        ) language: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Call<HomeResponse>


    @GET("{language}" + "/rest/V1/restapi/products")
    suspend fun fetchProductsById(
        @Path(value = "language", encoded = true) language: String,
        @Header("Authorization")token:String,
        @Header("Customer-Id")userId:String,
        @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?
    ): Response<ProductsResponse>

    @GET("{language}" + "/rest/V1/restapi/products/filters")
     fun fetchFilterData(
        @Path(value = "language", encoded = true) language: String,
        @QueryMap map: Map<String, String>
    ): Call<FilterResponse>

    @GET("{language}" + "/rest/V1/restapi/products")
     fun fetchDetailsProducts(
        @Path(value = "language", encoded = true) language: String,
        @Header("Authorization")token:String,
        @Header("Customer-Id")userId:String,
        @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?
    ): Call<ProductsResponse>

    @GET("{language}" + "rest/V1/restapi/products/"+"{id}"+"/links/related")
    suspend fun fetchProductsRelated(
        @Path(value = "language", encoded = true) language: String,
        @Path(value = "id", encoded = true) id: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
        ): Response<ProductsResponse>

    @GET("{language}" + "/rest/V1/restapi/search")
    suspend  fun searchProducts(
        @Path(value = "language", encoded = true) language: String,
        @Header("Authorization")token:String,
        @Header("Customer-Id")userId:String,
        @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?
    ): Response<ProductsResponse>

    @POST( "/rest/V1/reviews")
     fun addReview(
        @Header("Authorization")token:String,
        @Body review:RequestAddReviewResponse,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddReviewResponse>

    @GET( "/rest/V1/products/"+"{sku}"+"/reviews")
    fun getReviews(
        @Path(value = "sku", encoded = false) sku: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Call<ListReviwesResponse>

    @POST( "rest/V1/wishlist/add/"+"{id}")
    fun addFavourit(
        @Path(value = "id", encoded = true) id: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddToFavouritResponse>

    @DELETE( "rest/V1/wishlist/delete/"+"{id}")
    fun removeFavourit(
        @Path(value = "id", encoded = true) id: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddToFavouritResponse>


    @GET( "{lang}"+"rest/V1/wishlist/items")
    suspend fun getWishList(
        @Path(value = "lang", encoded = true) lang: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Response<ListFavouritResponse>


    @POST( "rest/V1/restapi/quote/cart/items/add")
    fun addToCart(
        @Header("Authorization")token:String,
        @Body registerRequest: RequestAddToCartResponse,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddToCartResponse>

    @GET( "{lang}"+"rest/V1/restapi/quote/cart/items")
     fun getlistCart(
        @Path(value = "lang", encoded = true) lang: String,
        @Header("Authorization")token:String,
        @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?
    ): Call<ListCartResponse>

    @PUT( "rest/V1/restapi/quote/cart/items/"+"{id}")
    fun editCart(
        @Path(value = "id", encoded = true) id: String,
        @Header("Authorization")token:String,
        @Body registerRequest: RequestAddToCartResponse,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddToCartResponse>


    @DELETE( "rest/V1/restapi/quote/cart/items/"+"{id}")
    fun deleteCart(
        @Path(value = "id", encoded = true) id: String,
        @QueryMap map: Map<String, String>,
        @Header("Authorization")token:String?,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddToCartResponse>

    @POST( "rest/V1/restapi/quote/cart")
    fun createCart(@Header("access_firebase")token_firebase:String?): Call<CreateCartResponse>


    @POST( "rest/V1/restapi/quote/cart/items/add")
    fun addGustCart(
        @Body registerRequest: AddGustCartResponse,@Header("Access-Firebase")token_firebase:String?
    ): Call<AddToCartResponse>

    @PUT("rest/V1/restapi/customer/me")
    fun editAccount(@Body registerRequest: RegisterRequest, @Header("Authorization")token:String
                    ,@Header("access_firebase")token_firebase:String?): Call<AccountResponse>

    @GET("/rest/V1/restapi/customer/me")
    fun getProfile(@Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?): Call<ProfileResponse>

    @GET("{language}" + "/rest/V1/restapi/brand")
    fun getBrand(
        @Path(
            value = "language",
            encoded = true
        ) language: String,
        @Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?
    ): Call<Brands_Response>

    @GET( "/rest/V1/directory/countries")
    fun getCountries(@Header("Access-Firebase")token_firebase:String?): Call<CountriesResponse>

    @POST( "/rest/V1/restapi/carts/shipping-methods")
    fun getShippingMethod(@Body request: RequestShippingMethodResponse, @Header("Authorization")token:String?,
                          @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?): Call<ListShippingMethod>

    @POST( "/rest/V1/restapi/carts/shipping-address")
    fun setShippingAddress(@Body request: RequestShippingAddressModel, @Header("Authorization")token:String?,
                           @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?): Call<ShippingAddressResponse>

    @POST( "/rest/V1/restapi/carts/payment-method/place-order")
    fun createOrder(@Body request: RequestCreateOrder, @Header("Authorization")token:String?,
                    @QueryMap map: Map<String, String>,@Header("Access-Firebase")token_firebase:String?): Call<CreateOrderResponse>


    @GET( "{language}" +"/rest/V1/restapi/orders/me")
    fun getMyOrders(  @Path(
        value = "language",
        encoded = true
    ) language: String,@Header("Authorization")token:String,@Header("Access-Firebase")token_firebase:String?): Call<MyOrdersResponse>

    @POST( "/rest/V1/restapi/order/payment/callback")
    fun confirmPayment(@Body request: RequestConfirmPayment ): Call<ConfirmPaymentResponse>

}