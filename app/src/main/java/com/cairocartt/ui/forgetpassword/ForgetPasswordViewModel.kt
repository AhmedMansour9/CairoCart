package com.cairocartt.ui.forgetpassword

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.AccountResponse
import com.cairocartt.data.remote.model.ForgetPasswordResponse
import com.cairocartt.data.remote.model.LoginRequest
import com.cairocartt.ui.login.LoginNavigator
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ForgetPasswordViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<LoginNavigator>(dataCenterManager) {


    private val _accountResponse = MutableLiveData<Resource<ForgetPasswordResponse>>()
    val accountResponse: LiveData<Resource<ForgetPasswordResponse>>
        get() = _accountResponse

    fun forgetPassword(email:String) {
            _accountResponse.postValue(Resource.loading(null))
             var map=HashMap<String,String>()
             map.put("email",email)
             map.put("template","email_reset")

        dataCenterManager.forgetPassword(map, BaseActivity.token).enqueue(object :
                Callback,retrofit2.Callback<ForgetPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
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


                override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                    _accountResponse.postValue(Resource.error(t.message.toString(), null))

                }
            })


    }

}