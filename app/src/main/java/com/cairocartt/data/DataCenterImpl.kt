package com.cairocartt.data

import com.cairocartt.data.remote.model.*
import com.cairocartt.data.remote.repository.ApiRepository
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.QueryMap
import javax.inject.Inject

class DataCenterImpl @Inject constructor(
    private val apiRepository: ApiRepository
) :
    DataCenterManager {
    override fun newAccount(registerRequest: RegisterRequest,firebase:String?): Call<AccountResponse> =
        apiRepository.userRegister(registerRequest,firebase)

    override fun editAccount(registerRequest: RegisterRequest,token: String,firebase:String?): Call<AccountResponse> =
        apiRepository.editAccount(registerRequest,token,firebase)

    override  fun loginAccount(loginRequest: LoginRequest,firebase:String?): Call<AccountResponse> =
        apiRepository.userLogin(loginRequest,firebase)


    override  fun loginFacebook(map: Map<String, String>,firebase:String?): Call<AccountResponse> =
        apiRepository.loginFacebook(map,firebase)

    override fun forgetPassword(
        map: Map<String, String>,
        token_firebase: String?
    ): Call<ForgetPasswordResponse> =
        apiRepository.forgetPassword(map,token_firebase)

    override fun getCategories(language: String, token: String,firebase:String?): Call<CategoriesResponse> =
        apiRepository.fetchCategories(language, token,firebase)


    override fun addReviewProduct(
        token: String,
        review: RequestAddReviewResponse
        ,firebase:String?
    ): Call<AddReviewResponse> = apiRepository.addReview(token, review,firebase)

    override fun getReviewProduct(sku: String, token: String,firebase:String?): Call<ListReviwesResponse> =
        apiRepository.getReviews(sku, token,firebase)

    override fun addFavourit(id: String, token: String,firebase:String?): Call<AddToFavouritResponse> =
        apiRepository.addFavourit(id, token,firebase)

    override fun removeFavourit(id: String, token: String,firebase:String?): Call<AddToFavouritResponse> =
        apiRepository.removeFavourit(id, token,firebase)


    override suspend fun getProductsById(
        lang: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Response<ProductsResponse> =
        apiRepository.fetchProductsById(lang, token, userId, map,firebase)

    override suspend fun getWishList(lang: String, token: String,firebase:String?): Response<ListFavouritResponse> =
        apiRepository.getWishList(lang, token,firebase)

    override fun addToCart(
        token: String,
        registerRequest: RequestAddToCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse> =
        apiRepository.addToCart(token, registerRequest,firebase)

    override  fun getListCart(lang: String, token: String,@QueryMap map: Map<String, String>,firebase:String?): Call<ListCartResponse> =
        apiRepository.getlistCart(lang, token,map,firebase)

    override fun editCart(
        id: String,
        token: String,
        registerRequest: RequestAddToCartResponse
        ,firebase:String?
    ): Call<AddToCartResponse> =
        apiRepository.editCart(id,token,registerRequest,firebase)

    override fun deleteCart(id: String, @QueryMap map: Map<String, String>,token:String?,firebase:String?): Call<AddToCartResponse> =
        apiRepository.deleteCart(id,map,token,firebase)

    override fun createCart(firebase:String?): Call<CreateCartResponse> =
        apiRepository.createCart(firebase)

    override fun addGustCart(registerRequest: AddGustCartResponse,firebase:String?): Call<AddToCartResponse> =
        apiRepository.addGustCart(registerRequest,firebase)

    override fun getProfile(token: String,firebase:String?): Call<ProfileResponse> =
        apiRepository.getProfile(token,firebase)

    override fun getBrand(language: String, token: String,firebase:String?): Call<Brands_Response> =
        apiRepository.getBrand(language, token,firebase)

    override fun getCountries(firebase:String?): Call<CountriesResponse> = apiRepository.getCountries(firebase)

    override fun getShippingMethod(request: RequestShippingMethodResponse,token: String?,@QueryMap map: Map<String, String>
                                   ,firebase:String?): Call<ListShippingMethod> =
        apiRepository.getShippingMethod(request,token,map,firebase)

    override fun setShippingAddress(
        request: RequestShippingAddressModel,
        token: String?,@QueryMap map: Map<String, String>
        ,firebase:String?
    ): Call<ShippingAddressResponse> = apiRepository.setShippingAddress(request,token,map,firebase)

    override fun createOrder(
        request: RequestCreateOrder,
        token: String?,
        map: Map<String, String>
        ,firebase:String?
    ): Call<CreateOrderResponse> = apiRepository.createOrder(request,token,map,firebase)

    override fun getOrders(language: String,token: String,firebase:String?): Call<MyOrdersResponse> =  apiRepository.getMyOrders(language,token,firebase)
    override suspend fun searchProducts(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Response<ProductsResponse> =  apiRepository.searchProducts(language, token, userId, map,firebase)

    override fun fetchHome(language: String, token: String,firebase:String?): Call<HomeResponse> = apiRepository.fetchHome(language,token,firebase)
    override suspend fun fetchProductsRelated(
        language: String,
        id: String,
        token: String,firebase:String?
        ): Response<ProductsResponse> = apiRepository.fetchProductsRelated(language,id,token,firebase)

    override fun fetchDetailsProducts(
        language: String,
        token: String,
        userId: String,
        map: Map<String, String>
        ,firebase:String?
    ): Call<ProductsResponse> = apiRepository.fetchDetailsProducts(language,token,userId,map,firebase)

}