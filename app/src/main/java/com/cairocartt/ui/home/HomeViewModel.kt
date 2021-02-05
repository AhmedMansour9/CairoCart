package com.cairocartt.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.HomeResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class HomeViewModel  @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<Any?>(dataCenterManager) {

    private val _homeResponse = MutableLiveData<Resource<HomeResponse>>()
    val homeResponse: LiveData<Resource<HomeResponse>>
        get() = _homeResponse

    private val _productResponse = MutableLiveData<Resource<ProductsResponse>>()
    val productResponse: LiveData<Resource<ProductsResponse>>
        get() = _productResponse

    var Lang = MutableLiveData<String>()
    var token = MutableLiveData<String>()
    var userId = MutableLiveData<String>()

    init {

        if (homeResponse.value == null)
            getHome()
    }
    fun getLang():String{
        val currentLang: String = Locale.getDefault().getLanguage()
        return currentLang
    }

    fun getHome() {
        _homeResponse.postValue(Resource.loading(null))
        dataCenterManager.fetchHome(getLang(), "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl",
            BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<HomeResponse> {
                override fun onResponse(
                    call: Call<HomeResponse>,
                    response: Response<HomeResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _homeResponse.postValue(Resource.success(response.body()))
                        } else {
                            _homeResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _homeResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                    _homeResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }


    fun getDetailsProduct(id:String) {
        _productResponse.postValue(Resource.loading(null))
        var hashMap = HashMap<String, String>()

        hashMap.put("searchCriteria[filterGroups][0][filters][0][value]", id)
        hashMap.put("searchCriteria[filterGroups][0][filters][0][field]", "entity_id")


        dataCenterManager.fetchDetailsProducts(   getLang(), "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl", userId.value.toString(), hashMap
            ,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<ProductsResponse> {
                override fun onResponse(
                    call: Call<ProductsResponse>,
                    response: Response<ProductsResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _productResponse.postValue(Resource.success(response.body()))
                        } else {
                            _productResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _productResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                    _productResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }
}