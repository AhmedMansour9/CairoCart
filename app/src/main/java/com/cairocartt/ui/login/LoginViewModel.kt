package com.cairocartt.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.AccountResponse
import com.cairocartt.data.remote.model.LoginRequest
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<LoginNavigator>(dataCenterManager) {

    var loginRequest: LoginRequest = LoginRequest()

    private val _accountResponse = MutableLiveData<Resource<AccountResponse>>()
    val accountResponse: LiveData<Resource<AccountResponse>>
        get() = _accountResponse

    fun login() {
        if (!loginRequest.empty()) {
//            viewModelScope.launch {
                _accountResponse.postValue(Resource.loading(null))
                dataCenterManager.loginAccount(loginRequest, BaseActivity.token).enqueue(object :Callback,retrofit2.Callback<AccountResponse> {
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



    fun loginSocial(type:String,social_id: String?, email: String?, full_name: String?)

    {
            _accountResponse.postValue(Resource.loading(null))

        var map = HashMap<String, String>()
        if (social_id != null) {
            map.put("token", social_id)
        }
        if (email != null) {
            map.put("email", email)
        }
        if (full_name != null) {
            map.put("name", full_name)
        }
        map.put("type", type)

        val a="U"
        map.put("auth_token", "0wwF{iGU{%n$$a~$&]?t#l<Fwn_F)jYLI0debZpR2vaOPk:/4bme0RRf|O#r{+ih")

        dataCenterManager.loginFacebook(map, BaseActivity.token).enqueue(object :Callback,retrofit2.Callback<AccountResponse> {

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