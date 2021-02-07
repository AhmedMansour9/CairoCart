package com.cairocartt.ui.filter

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.Brands_Response
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class FiltertionViewModel  @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<FiltertionNavigator?>(dataCenterManager) {


    private val _categoryResponse = MutableLiveData<Resource<CategoriesResponse>>()
    val categoryResponse: LiveData<Resource<CategoriesResponse>>
        get() = _categoryResponse

    private val _brandResponse = MutableLiveData<Resource<Brands_Response>>()
    val brandResponse: LiveData<Resource<Brands_Response>>
        get() = _brandResponse

    var Lang = MutableLiveData<String>()
    var token = MutableLiveData<String>()

    init {
        Log.d("CategoryFragment", "ESLAM: ")

        if (categoryResponse.value == null){
            getCategory()
        }


        if (brandResponse.value == null){
            getBrand()
        }


    }
    fun getLang():String{
        val currentLang: String = Locale.getDefault().getLanguage()
        return currentLang
    }

    fun getCategory() {
//        viewModelScope.launch {
        _categoryResponse.postValue(Resource.loading(null))
        dataCenterManager.getCategories(getLang(), "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl",BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<CategoriesResponse> {
                override fun onResponse(
                    call: Call<CategoriesResponse>,
                    response: Response<CategoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _categoryResponse.postValue(Resource.success(response.body()))
                        } else {
                            _categoryResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _categoryResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                    _categoryResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }


    fun getBrand() {
        _brandResponse.postValue(Resource.loading(null))
        dataCenterManager.getBrand(getLang(), "Bearer 5u1forfnoiuok9qtdaftqxtyd399bcsl",
            BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<Brands_Response> {
                override fun onResponse(
                    call: Call<Brands_Response>,
                    response: Response<Brands_Response>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _brandResponse.postValue(Resource.success(response.body()))
                        } else {
                            _brandResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _brandResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<Brands_Response>, t: Throwable) {
                    _brandResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }

}