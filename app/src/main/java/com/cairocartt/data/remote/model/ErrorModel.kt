package com.cairocartt.data.remote.model

import com.google.gson.annotations.SerializedName

class ErrorModel {
    @SerializedName("message")
     var errorMessage: String? = null

    @SerializedName("success")
    var success = false

    @SerializedName("error_code")
    var status_code: Int=0

}