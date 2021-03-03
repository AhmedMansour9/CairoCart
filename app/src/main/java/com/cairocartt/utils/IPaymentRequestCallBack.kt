package com.cairocartt.utils

import com.cairocartt.data.remote.model.PayFortData




interface IPaymentRequestCallBack {

    fun onPaymentRequestResponse(responseType: Int, responseData: PayFortData?)

}