package com.cairocartt.data.remote.model

import android.util.Log
import com.cairocartt.utils.Constants
import webconnect.com.webconnect.listener.OnWebCallback

class WebCallback(var callback:OnWebCallback) : OnWebCallback
{
    override fun <T : Any?> onSuccess(`object`: T?, taskId: Int) {
        if (callback != null) {
            Log.d("api", "onSuccess resopnse: $`object`")
            callback.onSuccess(`object`, taskId)
        }    }

    override fun <T : Any?> onError(`object`: T?, error: String?, taskId: Int) {
        if (`object` is ErrorModel && ((`object` as ErrorModel).status_code === Constants.EXPIRE_TOKEN_STATUS_CODE || (`object` as ErrorModel).status_code === Constants.INVALID_TOKEN_STATUS_CODE || (`object` as ErrorModel).status_code === Constants.INVALID_JWT_TOKEN_STATUS_CODE || (`object` as ErrorModel).status_code === Constants.TOKEN_INVALID_TOKEN_STATUS_CODE || (`object` as ErrorModel).status_code === Constants.UNAUTHORIZED_TOKEN_STATUS_CODE || (`object` as ErrorModel).status_code === Constants.AUTHORIZED_ERROR_TOKEN_STATUS_CODE)
        ) {
//            showSessionExpireDialog(`object` as ErrorModel?)
        }
        if (`object` is Throwable) {
            val model = ErrorModel()
            model.errorMessage= error
//            `object` = model as T
        }
        if (callback != null) {
            callback.onError(`object`, error, taskId)
        }
    }

}