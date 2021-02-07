package com.cairocartt.ui.createorder

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.*
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.detailsproduct.DetailsProductNavigtor
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class CreateOrderViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<CreateOrderNavigator>(dataCenterManager) {

    var itemPositionCountry = MutableLiveData<Int>()
    var itemPositionCity = MutableLiveData<Int>()

    private val _countriesResponse = MutableLiveData<Resource<CountriesResponse>>()
    val countriesResponse: LiveData<Resource<CountriesResponse>>
        get() = _countriesResponse

    private val _shippingResponse = MutableLiveData<Resource<ListShippingMethod>>()
    val shippingResponse: LiveData<Resource<ListShippingMethod>>
        get() = _shippingResponse

    private val _shippingAddressResponse = MutableLiveData<Resource<ShippingAddressResponse>>()
    val shippingAddressResponse: LiveData<Resource<ShippingAddressResponse>>
        get() = _shippingAddressResponse


    private val _createOrderResponse = MutableLiveData<Resource<CreateOrderResponse>>()
    val createOrderResponse: LiveData<Resource<CreateOrderResponse>>
        get() = _createOrderResponse


    init {
        if (countriesResponse.value == null)
            getCountries()
    }

    fun getCountries() {
        _countriesResponse.postValue(Resource.loading(null))
        dataCenterManager.getCountries(BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<CountriesResponse> {
                override fun onResponse(
                    call: Call<CountriesResponse>,
                    response: Response<CountriesResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _countriesResponse.postValue(Resource.success(response.body()))
                        } else {
                            _countriesResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _countriesResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<CountriesResponse>, t: Throwable) {
                    _countriesResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }


    fun getShippingMethod(request:RequestShippingMethodResponse,token:String?,cart_id:String?) {
        var hashMap=HashMap<String,String>()
        if (cart_id != null) {
            hashMap.put("cart_id",cart_id)
        }
        _shippingResponse.postValue(Resource.loading(null))
        dataCenterManager.getShippingMethod(request,"Bearer $token",hashMap,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<ListShippingMethod> {
                override fun onResponse(
                    call: Call<ListShippingMethod>,
                    response: Response<ListShippingMethod>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _shippingResponse.postValue(Resource.success(response.body()))
                        } else {
                            _shippingResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _shippingResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )
                }
                override fun onFailure(call: Call<ListShippingMethod>, t: Throwable) {
                    _shippingResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }

    fun setShippingAddress(request:RequestShippingAddressModel,token:String?,cart_id:String?) {
        _shippingAddressResponse.postValue(Resource.loading(null))
        var hashMap=HashMap<String,String>()
        if (cart_id != null) {
            hashMap.put("cart_id",cart_id)
        }
        dataCenterManager.setShippingAddress(request,"Bearer $token",hashMap,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<ShippingAddressResponse> {
                override fun onResponse(
                    call: Call<ShippingAddressResponse>,
                    response: Response<ShippingAddressResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _shippingAddressResponse.postValue(Resource.success(response.body()))
                        } else {
                            _shippingAddressResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _shippingAddressResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }
                override fun onFailure(call: Call<ShippingAddressResponse>, t: Throwable) {
                    _shippingAddressResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }

    fun createOrder(request:RequestCreateOrder,token:String?,cart_id:String?) {
        _createOrderResponse.postValue(Resource.loading(null))
        var hashMap=HashMap<String,String>()
        if (cart_id != null) {
            hashMap.put("cart_id",cart_id)
        }
        dataCenterManager.createOrder(request,"Bearer $token",hashMap,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<CreateOrderResponse> {
                override fun onResponse(
                    call: Call<CreateOrderResponse>,
                    response: Response<CreateOrderResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _createOrderResponse.postValue(Resource.success(response.body()))
                        } else {
                            _createOrderResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null))
                        }
                    } else _shippingAddressResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )
                }
                override fun onFailure(call: Call<CreateOrderResponse>, t: Throwable) {
                    _createOrderResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )
    }

}