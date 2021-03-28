package com.cairocartt.data.remote.repository

import com.cairocartt.data.remote.api.ApiService
import com.cairocartt.data.remote.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.QueryMap
import javax.inject.Inject


class ApiRepository @Inject constructor(private val apiService: ApiService) : ApiService {

    override fun userRegister(registerRequest: RegisterRequest,firebase:String?): Call<AccountResponse> =
        apiService.userRegister(registerRequest,firebase)

    override fun editAccount(registerRequest: RegisterRequest,token: String,firebase:String?): Call<AccountResponse> =
        apiService.editAccount(registerRequest,token,firebase)

    override fun getProfile(token: String,firebase:String?): Call<ProfileResponse> =
        apiService.getProfile(token,firebase)

    override fun getBrand(language: String, token: String,firebase:String?): Call<Brands_Response> =
        apiService.getBrand(language, token,firebase)



    override  fun userLogin(loginRequest: LoginRequest,firebase:String?): Call<AccountResponse> =
        apiService.userLogin(loginRequest,firebase)

    override  fun loginFacebook(map: Map<String, String>,firebase:String?): Call<AccountResponse> =
        apiService.loginFacebook(map,firebase)

    override  fun forgetPassword(
        map: Map<String, String>,
        token_firebase: String?
    ): Call<ForgetPasswordResponse> = apiService.forgetPassword(map,token_firebase)


    override fun fetchCategories(language: String, token: String,firebase:String?): Call<CategoriesResponse> =
        apiService.fetchCategories(language, token,firebase)

    override fun fetchHome(language: String, token: String,firebase:String?): Call<HomeResponse> = apiService.fetchHome(language,token,firebase)


    override suspend fun fetchProductsById(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Response<ProductsResponse> =
        apiService.fetchProductsById(language, token, userId, map,firebase)

    override  fun fetchFilterData(
        language: String,
        map: Map<String, String>
    ): Call<FilterResponse> = apiService.fetchFilterData(language, map)

    override fun fetchDetailsProducts(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Call<ProductsResponse> = apiService.fetchDetailsProducts(language,token,userId,map,firebase)

    override suspend fun fetchProductsRelated(
        language: String,
        id: String,
        token: String,firebase:String?
        ): Response<ProductsResponse> =apiService.fetchProductsRelated(language,id,token,firebase)

    override suspend fun  searchProducts(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Response<ProductsResponse> =
        apiService.searchProducts(language, token, userId, map,firebase)

    override fun addReview(
        token: String,
        review: RequestAddReviewResponse,firebase:String?
    ): Call<AddReviewResponse> = apiService.addReview(token, review,firebase)

    override fun getReviews(sku: String, token: String,firebase:String?): Call<ListReviwesResponse> =
        apiService.getReviews(sku, token,firebase)

    override fun addFavourit(id: String, token: String,firebase:String?): Call<AddToFavouritResponse> =
        apiService.addFavourit(id, token,firebase)

    override fun removeFavourit(id: String, token: String,firebase:String?): Call<AddToFavouritResponse> =
        apiService.removeFavourit(id, token,firebase)

    override suspend fun getWishList(lang: String, token: String,firebase:String?): Response<ListFavouritResponse> =
        apiService.getWishList(lang,token,firebase)

    override fun addToCart(
        token: String,
        registerRequest: RequestAddToCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse> =
    apiService.addToCart(token,registerRequest,firebase)

    override  fun getlistCart(lang: String, token: String,@QueryMap map: Map<String, String>
                              ,firebase:String?): Call<ListCartResponse> =
        apiService.getlistCart(lang,token,map,firebase)

    override fun editCart(
        id: String,
        token: String,
        registerRequest: RequestAddToCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse> =
        apiService.editCart(id,token,registerRequest,firebase)

    override fun deleteCart(
        id: String,
        @QueryMap map: Map<String, String>,
        token: String?
        ,firebase:String?
    ): Call<AddToCartResponse> =
        apiService.deleteCart(id,map,token,firebase)

    override fun createCart(firebase:String?): Call<CreateCartResponse> =
        apiService.createCart(firebase)

    override fun addGustCart(registerRequest: AddGustCartResponse,firebase:String?): Call<AddToCartResponse> =
        apiService.addGustCart(registerRequest,firebase)

    override fun getCountries(firebase:String?): Call<CountriesResponse> = apiService.getCountries(firebase)

    override fun getShippingMethod(request: RequestShippingMethodResponse,token: String?,@QueryMap map: Map<String, String>,firebase:String?): Call<ListShippingMethod> =
        apiService.getShippingMethod(request,token,map,firebase)

    override fun setShippingAddress(
        request: RequestShippingAddressModel,
        token: String?,@QueryMap map: Map<String, String>
        ,firebase:String?
    ): Call<ShippingAddressResponse> = apiService.setShippingAddress(request,token,map,firebase)

    override fun createOrder(
        request: RequestCreateOrder,
        token: String?,
        map: Map<String, String>
        ,firebase:String?
    ): Call<CreateOrderResponse> =  apiService.createOrder(request,token,map,firebase)

    override fun getMyOrders(language: String,@Header("Authorization")token:String,firebase:String?):
            Call<MyOrdersResponse> =  apiService.getMyOrders(language,token,firebase)

    override fun confirmPayment(request: RequestConfirmPayment): Call<ConfirmPaymentResponse> =
        apiService.confirmPayment(request)


}