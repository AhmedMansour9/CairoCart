package com.cairocartt.ui.editprofile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cairocartt.base.BaseActivity
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager
import com.cairocartt.data.remote.model.AccountResponse
import com.cairocartt.data.remote.model.ProfileResponse
import com.cairocartt.data.remote.model.RegisterRequest
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.utils.Resource
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class EditProfileViewModel  @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<EditProfileNavigator>(dataCenterManager) {


    var customer: RegisterRequest.Customer = RegisterRequest.Customer()
    var registerRequest: RegisterRequest = RegisterRequest()
    private val _accountResponse = MutableLiveData<Resource<AccountResponse>>()
    val editProfileResponse: LiveData<Resource<AccountResponse>>
        get() = _accountResponse

    private val _profileResponse = MutableLiveData<Resource<ProfileResponse>>()
    val getProfileResponse: LiveData<Resource<ProfileResponse>>
        get() = _profileResponse

    fun editProfile(token:String) {
        registerRequest.customer = customer
        if (!registerRequest.empty()) {
            _accountResponse.postValue(Resource.loading(null))
//            viewModelScope.launch {
            dataCenterManager.editAccount(registerRequest,"Bearer $token", BaseActivity.token)
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
    fun getProfile(token:String) {
        _profileResponse.postValue(Resource.loading(null))
//            viewModelScope.launch {
            dataCenterManager.getProfile("Bearer $token",BaseActivity.token)
                .enqueue(object : Callback, retrofit2.Callback<ProfileResponse> {
                    override fun onResponse(
                        call: Call<ProfileResponse>,
                        response: Response<ProfileResponse>
                    ) {
                        if(response.isSuccessful){
                            if (response.code() == 200) {
                                _profileResponse.postValue(Resource.success(response.body()))

                            } else {
                                _profileResponse.postValue(Resource.error(response?.errorBody().toString(), null))
                            }
                        }else
                            _profileResponse.postValue(Resource.error(response.message(), null))
                    }

                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        _profileResponse.postValue(Resource.error(t.message.toString(), null))
                    }
                })
        }

}