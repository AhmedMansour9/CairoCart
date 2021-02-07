package com.cairocartt.ui.cart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.*
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.myaccount.MyAccountNavigator
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback
import kotlin.collections.HashMap

class CartViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<MyAccountNavigator>(dataCenterManager) {

    private val _cartResponse = MutableLiveData<Resource<ListCartResponse>>()
    val cartResponse: LiveData<Resource<ListCartResponse>>
        get() = _cartResponse


    private val _editCartResponse = MutableLiveData<Resource<AddToCartResponse>>()
    val editCartResponse: LiveData<Resource<AddToCartResponse>>
        get() = _editCartResponse

    private val _deleteCartResponse = MutableLiveData<Resource<AddToCartResponse>>()
    val deleteCartResponse: LiveData<Resource<AddToCartResponse>>
        get() = _deleteCartResponse


    fun getCart(token: String?,quta_id:String?) {
        var hashMap=HashMap<String,String>()
        if (!quta_id .isNullOrEmpty()) {
            hashMap.put("cart_id", quta_id)
        }
        _cartResponse.postValue(Resource.loading(null))
        dataCenterManager.getListCart(Locale.getDefault().getLanguage() + "/", "Bearer $token",hashMap
            ,
            BaseActivity.token)
            .enqueue(object :
                Callback, retrofit2.Callback<ListCartResponse> {
                override fun onResponse(
                    call: Call<ListCartResponse>,
                    response: Response<ListCartResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _cartResponse.postValue(Resource.success(response.body()))
                        } else {
                            _cartResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.code.toString(),
                                    null
                                )
                            )
                        }
                    } else
                        _cartResponse.postValue(Resource.error(response.message(), null))
                }
                override fun onFailure(call: Call<ListCartResponse>, t: Throwable) {
                    _cartResponse.postValue(Resource.error(t.message.toString(), null))
                }
            })
    }


    fun editCart(id: String, token: String?, request: RequestAddToCartResponse) {
        _editCartResponse.postValue(Resource.loading(null))

        dataCenterManager.editCart(id, "Bearer $token", request,BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<AddToCartResponse> {
                override fun onResponse(
                    call: Call<AddToCartResponse>,
                    response: Response<AddToCartResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _editCartResponse.postValue(Resource.success(response.body()))
                        } else {
                            _editCartResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _editCartResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                    _editCartResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )

    }



    fun deleteCart(itemId:String,id:String?,token: String?) {
        _deleteCartResponse.postValue(Resource.loading(null))
        val hashMap=HashMap<String,String>()
        if(!id.isNullOrEmpty()){
            hashMap.put("cart_id",id)
        }
        dataCenterManager.deleteCart(itemId,hashMap,"Bearer $token",BaseActivity.token).enqueue(
            object : Callback, retrofit2.Callback<AddToCartResponse> {
                override fun onResponse(
                    call: Call<AddToCartResponse>,
                    response: Response<AddToCartResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status?.code == 200) {
                            _editCartResponse.postValue(Resource.success(response.body()))
                        } else {
                            _deleteCartResponse.postValue(
                                Resource.error(
                                    response.body()?.status?.message.toString(),
                                    null
                                )
                            )
                        }
                    } else _deleteCartResponse.postValue(
                        Resource.error(
                            response.errorBody().toString(), null
                        )
                    )

                }

                override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                    _deleteCartResponse.postValue(Resource.error(t.message.toString(), null))
                }
            }
        )

    }



}