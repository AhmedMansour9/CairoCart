package com.cairocartt.data

import com.cairocartt.data.remote.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.QueryMap

interface DataCenterManager {

     fun newAccount(registerRequest: RegisterRequest,firebase:String?): Call<AccountResponse>

    fun editAccount(registerRequest: RegisterRequest,token:String,firebase:String?): Call<AccountResponse>

     fun loginAccount(loginRequest: LoginRequest,firebase:String?): Call<AccountResponse>

     fun loginFacebook(map: Map<String, String>,firebase:String?): Call<AccountResponse>

    fun forgetPassword(
        map: Map<String, String>,
        token_firebase: String?
    ): Call<ForgetPasswordResponse>

     fun getCategories(language: String
    ,token:String,firebase:String?): Call<CategoriesResponse>

    suspend fun getProductsById(
        lang:String,
        token:String,
        userId:String,
        map: Map<String, String>
        ,firebase:String?
    ): Response<ProductsResponse>

     fun addReviewProduct(
        token:String,
        review:RequestAddReviewResponse
        ,firebase:String?
    ): Call<AddReviewResponse>


    fun getReviewProduct(
        sku:String,
        token:String
        ,firebase:String?
    ): Call<ListReviwesResponse>

   fun  addFavourit(id: String, token: String,firebase:String?): Call<AddToFavouritResponse>

    fun  removeFavourit(id: String, token: String,firebase:String?): Call<AddToFavouritResponse>


    suspend fun getWishList(
        lang:String,
        token:String
        ,firebase:String?
    ): Response<ListFavouritResponse>

    fun addToCart(
        token: String,
        registerRequest: RequestAddToCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse>

     fun getListCart(
        lang:String,
        token:String,
        @QueryMap map: Map<String, String>,firebase:String?
     ): Call<ListCartResponse>


     fun editCart(
        id: String,
        token: String,
        registerRequest: RequestAddToCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse>

    fun deleteCart(
        id: String,
        @QueryMap map: Map<String, String>
       ,token:String?
        ,firebase:String?
    ): Call<AddToCartResponse>


    fun createCart(firebase:String?): Call<CreateCartResponse>

    fun addGustCart(
        registerRequest: AddGustCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse>

    fun getProfile(token:String,firebase:String?): Call<ProfileResponse>

    fun getBrand(language: String, token: String,firebase:String?): Call<Brands_Response>

    fun getCountries(firebase:String?): Call<CountriesResponse>

    fun getShippingMethod(request: RequestShippingMethodResponse,token: String?,@QueryMap map: Map<String, String>,firebase:String?): Call<ListShippingMethod>

    fun setShippingAddress(request: RequestShippingAddressModel,token: String?,@QueryMap map: Map<String, String>,firebase:String?): Call<ShippingAddressResponse>

    fun createOrder(request: RequestCreateOrder,token: String?,@QueryMap map: Map<String, String>,firebase:String?): Call<CreateOrderResponse>

    fun getOrders(
        language: String,
        token:String
        ,firebase:String?
    ): Call<MyOrdersResponse>

    suspend fun searchProducts(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Response<ProductsResponse>

     fun fetchHome(language: String, token: String,firebase:String?): Call<HomeResponse>

     suspend fun fetchProductsRelated(
        language: String,
        id: String,
        token: String
        ,firebase:String?
        ): Response<ProductsResponse>

     fun fetchDetailsProducts(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Call<ProductsResponse>

}