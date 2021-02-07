package com.cairocartt.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.AccountResponse
import com.cairocartt.data.remote.model.RegisterRequest
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class RegisterViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<RegisterNavigator>(dataCenterManager) {


    var customer: RegisterRequest.Customer = RegisterRequest.Customer()
    var registerRequest: RegisterRequest = RegisterRequest()
    private val _accountResponse = MutableLiveData<Resource<AccountResponse>>()
    val accountResponse: LiveData<Resource<AccountResponse>>
        get() = _accountResponse

    fun register() {
        registerRequest.customer = customer
        if (!registerRequest.empty()) {
                _accountResponse.postValue(Resource.loading(null))
//            viewModelScope.launch {
                dataCenterManager.newAccount(registerRequest, BaseActivity.token)
                    .enqueue(object : Callback, retrofit2.Callback<AccountResponse> {
                        override fun onResponse(
                            call: Call<AccountResponse>,
                            response: Response<AccountResponse>
                        ) {
                            if(response.isSuccessful){
                                if (response.body()?.status?.code == 200) {
                                    _accountResponse.postValue(Resource.success(response.body()))

                                } else {
                                    _accountResponse.postValue(Resource.error(response.body()?.status?.message.toString(), null))
                                }
                            }else
                                _accountResponse.postValue(Resource.error(response.message(), null))



                        }

                        override fun onFailure(call: Call<AccountResponse>, t: Throwable) {
                            _accountResponse.postValue(Resource.error(t.message.toString(), null))
                        }
                    })
        }
    }

}

