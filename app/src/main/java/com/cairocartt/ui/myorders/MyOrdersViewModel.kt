package com.cairocartt.ui.myorders

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.data.remote.model.MyOrdersResponse
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.myaccount.MyAccountNavigator
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.HashMap

class MyOrdersViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<MyAccountNavigator>(dataCenterManager) {

    private val _ordersResponse = MutableLiveData<Resource<MyOrdersResponse>>()
    val ordersResponse: LiveData<Resource<MyOrdersResponse>>
        get() = _ordersResponse


    fun getLang():String{
        val currentLang: String = Locale.getDefault().getLanguage()
        return currentLang
    }

    fun getOrders(token: String?) {

        _ordersResponse.postValue(Resource.loading(null))
        dataCenterManager.getOrders(getLang() , "Bearer $token",
            BaseActivity.token)
            .enqueue(object :
                Callback, retrofit2.Callback<MyOrdersResponse> {
                override fun onResponse(
                    call: Call<MyOrdersResponse>,
                    response: Response<MyOrdersResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _ordersResponse.postValue(Resource.success(response.body()))
                        } else {
                            _ordersResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.code.toString(),
                                    null
                                )
                            )
                        }
                    } else
                        _ordersResponse.postValue(Resource.error(response.message(), null))
                }
                override fun onFailure(call: Call<MyOrdersResponse>, t: Throwable) {
                    _ordersResponse.postValue(Resource.error(t.message.toString(), null))
                }
            })
    }


}