package com.cairocartt.ui.detailsproduct

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.*
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.HashMap

class DetailsProductViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<DetailsProductNavigtor>(dataCenterManager) {

    private val _reviewResponse = MutableLiveData<Resource<ListReviwesResponse>>()
    val reviewResponse: LiveData<Resource<ListReviwesResponse>>
        get() = _reviewResponse
    var sku = MutableLiveData<String>()

    private val _addFavouritResponse = MutableLiveData<Resource<AddToFavouritResponse>>()
    val addFavouritResponse: LiveData<Resource<AddToFavouritResponse>>
        get() = _addFavouritResponse

    private val _removeFavouritResponse = MutableLiveData<Resource<AddToFavouritResponse>>()
    val removeFavouritResponse: LiveData<Resource<AddToFavouritResponse>>
        get() = _removeFavouritResponse


    private val _addToCartResponse = MutableLiveData<Resource<AddToCartResponse>>()
    val addCartResponse: LiveData<Resource<AddToCartResponse>>
        get() = _addToCartResponse


    private val _createCartResponse = MutableLiveData<Resource<CreateCartResponse>>()
    val createCartResponse: LiveData<Resource<CreateCartResponse>>
        get() = _createCartResponse

    private val _addGustCartResponse = MutableLiveData<Resource<AddToCartResponse>>()
    val addGustCarttResponse: LiveData<Resource<AddToCartResponse>>
        get() = _addGustCartResponse


    fun getReviwes(sku:String) {
        _reviewResponse.postValue(Resource.loading(null))
        dataCenterManager.getReviewProduct(sku, "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl",
            BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<ListReviwesResponse> {
                override fun onResponse(
                    call: Call<ListReviwesResponse>,
                    response: Response<ListReviwesResponse>
                ) {


                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _reviewResponse.postValue(Resource.success(response.body()))
                        } else {
                            _reviewResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _reviewResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<ListReviwesResponse>, t: Throwable) {
                    _reviewResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }


    fun addFavourit(id:String,token:String) {
        _addFavouritResponse.postValue(Resource.loading(null))
        dataCenterManager.addFavourit(id, "Bearer $token",BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<AddToFavouritResponse> {
                override fun onResponse(
                    call: Call<AddToFavouritResponse>,
                    response: Response<AddToFavouritResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _addFavouritResponse.postValue(Resource.success(response.body()))
                        } else {
                            _addFavouritResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _addFavouritResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<AddToFavouritResponse>, t: Throwable) {
                    _reviewResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )

    }


    fun removeFavourit(id:String,token:String) {
        _removeFavouritResponse.postValue(Resource.loading(null))
        dataCenterManager.removeFavourit(id, "Bearer $token",BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<AddToFavouritResponse> {
                override fun onResponse(
                    call: Call<AddToFavouritResponse>,
                    response: Response<AddToFavouritResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _removeFavouritResponse.postValue(Resource.success(response.body()))
                        } else {
                            _removeFavouritResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _removeFavouritResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<AddToFavouritResponse>, t: Throwable) {
                    _removeFavouritResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )

    }





    fun addToCart(token:String,request:RequestAddToCartResponse) {
        _addToCartResponse.postValue(Resource.loading(null))
        dataCenterManager.addToCart("Bearer $token",request,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<AddToCartResponse> {
                override fun onResponse(
                    call: Call<AddToCartResponse>,
                    response: Response<AddToCartResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _addToCartResponse.postValue(Resource.success(response.body()))
                        } else {
                            _addToCartResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _addToCartResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                    _addToCartResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }

    fun createCart() {
        _createCartResponse.postValue(Resource.loading(null))
        dataCenterManager.createCart(BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<CreateCartResponse> {
                override fun onResponse(
                    call: Call<CreateCartResponse>,
                    response: Response<CreateCartResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _createCartResponse.postValue(Resource.success(response.body()))
                        } else {
                            _createCartResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _createCartResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )
                }
                override fun onFailure(call: Call<CreateCartResponse>, t: Throwable) {
                    _createCartResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }


    fun addGustCart(request:AddGustCartResponse) {
        _addGustCartResponse.postValue(Resource.loading(null))
        dataCenterManager.addGustCart(request,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<AddToCartResponse> {
                override fun onResponse(
                    call: Call<AddToCartResponse>,
                    response: Response<AddToCartResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _addGustCartResponse.postValue(Resource.success(response.body()))
                        } else {
                            _addGustCartResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _addGustCartResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                    _addGustCartResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }

    var userId = MutableLiveData<String>()
    var produtId = MutableLiveData<String>()


    var listData = Pager(PagingConfig(pageSize = 10)) {
        ProductsPagination(dataCenterManager,produtId.value.toString(),userId.value.toString())
    }.flow
}
class ProductsPagination constructor(
    dataCenterManager: DataCenterManager, produtId:String,userId:String
) :
    PagingSource<Int, ProductsResponse.Data>() {
    var dataCenterManager: DataCenterManager
    var produtId :String
    var userId :String

    init {
        this.dataCenterManager = dataCenterManager
        this.produtId=produtId
        this.userId=userId
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductsResponse.Data> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            var hashMap = HashMap<String, String>()


            val response = dataCenterManager.fetchProductsRelated(Locale.getDefault().getLanguage()+"/",produtId,
                "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl",BaseActivity.token)

            val responseData = mutableListOf<ProductsResponse.Data>()
            val data = response.body()!!.data
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}