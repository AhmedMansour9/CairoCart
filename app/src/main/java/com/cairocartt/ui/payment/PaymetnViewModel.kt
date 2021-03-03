package com.cairocartt.ui.payment

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.cairocartt.data.remote.model.PayFortData
import com.cairocartt.utils.IPaymentRequestCallBack
import com.cairocartt.utils.Status
import com.google.gson.Gson
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.sdk.android.dependancies.base.FortInterfaces
import com.payfort.sdk.android.dependancies.base.FortInterfaces.OnTnxProcessed
import com.payfort.sdk.android.dependancies.models.FortRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.HashMap


//class PaymetnViewModel (
//    val context: LifecycleOwner,
//    val activity: Activity,
//    val mAmount: String,
//    val vm: PaymentActivity,
//    val fortCallback: FortCallBackManager,
//    val payFortIteraction: PayFortIteraction,
//    val viewPayFortIteraction: ViewPayFortIteraction
//    ) {
//        fun getTokenSdk(deviceId: String, pref: SharedPref) {
//            if (BuildUtil.isDebug()) Log.e("processing ", "get signature...")
//
//            val param = linkedMapOf<String, String>()
//            param["access_code"] = PayFortConstValue.ACCESS_CODE
//            param["device_id"] = deviceId
//            param["language"] = pref.lang
//            param["merchant_identifier"] = BuildConfig.PAYFORT_SDK_TOKEN
//            param["service_command"] = PayFortConstValue.SERVICE_COMMAND
//
//            val sb = StringBuilder()
//            sb.append(PayFortConstValue.SHA_RQUEST_PHRASE)
//            param.forEach { (k, v) ->
//                sb.append("$k=$v")
//            }
//            sb.append(PayFortConstValue.SHA_RQUEST_PHRASE)
//
//            val params = Params(
//                access_code = PayFortConstValue.ACCESS_CODE,
//                device_id = deviceId,
//                language = pref.lang,
//                merchant_identifier = BuildConfig.PAYFORT_SDK_TOKEN,
//                service_command = PayFortConstValue.SERVICE_COMMAND,
//                signature = getHashString(sb.toString())
//            )
//
//            val baseUrl =
//                if (BuildUtil.isDebug()) PayFortConstValue.TEST_URL else PayFortConstValue.PRODUCTION_URL
//            vm.getTokenSdk(baseUrl, params)
//
//            vm.responsePay.observe(context, Observer { resource ->
//                when (resource.status) {
//                    Status.COMPLETE -> {
//
//                        viewPayFortIteraction.onShowLoading()
//                    }
//                    Status.SUCCESS -> {
//                        checkResponse(resource.data!!)
//                    }
//                    Status.ERROR -> {
//                        viewPayFortIteraction.onShowError(resource.message!!)
//
//                    }
//                    Status.LOADING -> {
//                    }
//                    Status.OFFLINE -> {
//
//                        viewPayFortIteraction.onShowOffline()
//                    }
//                }
//            })
//        }
//
//        private fun checkResponse(responsePay: ResponsePay) {
//            if (responsePay.sdkToken?.isNotEmpty()!!) {
//                startPayFortSdk(responsePay.sdkToken!!)
//            }
//        }
//
//        fun startPayFortSdk(sign: String) {
//            val fortrequest = FortRequest().apply {
//                requestMap = prepareRequestMap(sign)
//                isShowResponsePage = false
//            }
//            callSdk(fortrequest)
//
//        }
//
//        private fun prepareRequestMap(sign: String): MutableMap<String, Any>? {
//            val requestMap: MutableMap<String, Any> = mutableMapOf()
//            requestMap["command"] = PURCHASE
//            requestMap["customer_email"] = CUSTOMER_EMAIL
//            requestMap["currency"] = CURRENCY
//            requestMap["amount"] = mAmount
//            requestMap["language"] = vm.pref.lang
//            requestMap["merchant_reference"] = "MSNo-" + System.currentTimeMillis().toString()
//            //optional
//            requestMap["customer_name"] = ""
//            requestMap["customer_ip"] = ""
//            requestMap["eci"] = ECOMMERCE
//            requestMap["remember_me"] = "NO"
//            requestMap["sdk_token"] = sign
//
//            return requestMap
//        }
//
//        private fun callSdk(fortrequest: FortRequest) {
//            try {
//                FortSdk.getInstance().registerCallback(
//                    activity,
//                    fortrequest,
//                    FortSdk.ENVIRONMENT.TEST,
//                    PAYFORT_RC,
//                    fortCallback,
//                    true,
//                    object : FortInterfaces.OnTnxProcessed {
//                        override fun onCancel(
//                            requestParamsMap: Map<String, Any>,
//                            responseMap: Map<String, Any>
//                        ) {
//                            payFortIteraction.onCancel(responseMap.toString())
//                        }
//
//                        override fun onSuccess(
//                            requestParamsMap: Map<String, Any>,
//                            fortResponseMap: Map<String, Any>
//                        ) {
//                            payFortIteraction.onSuccess(fortResponseMap.toString())
//                        }
//
//                        override fun onFailure(
//                            requestParamsMap: Map<String, Any>,
//                            fortResponseMap: Map<String, Any>
//                        ) {
//                            payFortIteraction.onFailure(fortResponseMap.toString())
//                        }
//                    })
//            } catch (e: Exception) {
//                if (BuildUtil.isDebug()) Log.e("execute Payment", "call FortSdk", e)
//            }
//        }

//    //Request key for response
//    val RESPONSE_GET_TOKEN = 111
//    val RESPONSE_PURCHASE = 222
//    val RESPONSE_PURCHASE_CANCEL = 333
//    val RESPONSE_PURCHASE_SUCCESS = 444
//    val RESPONSE_PURCHASE_FAILURE = 555
//
//    //WS params
//    private val KEY_MERCHANT_IDENTIFIER = "merchant_identifier"
//    private val KEY_SERVICE_COMMAND = "service_command"
//    private val KEY_DEVICE_ID = "device_id"
//    private val KEY_LANGUAGE = "language"
//    private val KEY_ACCESS_CODE = "access_code"
//    private val KEY_SIGNATURE = "signature"
//
//    //Commands
//    val AUTHORIZATION = "AUTHORIZATION"
//    val PURCHASE = "PURCHASE"
//    private val SDK_TOKEN = "SDK_TOKEN"
//
//    //Test token url
//    private val TEST_TOKEN_URL =
//        "https://sbpaymentservices.payfort.com/FortAPI/paymentApi"
//
//    //Live token url
//    val LIVE_TOKEN_URL = "https://paymentservices.payfort.com/FortAPI/paymentApi"
//
//    //Make a change for live or test token url from WS_GET_TOKEN variable
//    private val WS_GET_TOKEN = TEST_TOKEN_URL
//
//    //Statics
//    private val MERCHANT_IDENTIFIER = "VZYfCwhg"
//    private val ACCESS_CODE = "qeoqGtJrSmvtd8xZ3UBC"
//    private val SHA_TYPE = "SHA-256"
//    private val SHA_REQUEST_PHRASE = "TESTSHAIN"
//    val SHA_RESPONSE_PHRASE = "TESTSHAIN"
//    val CURRENCY_TYPE = "EGP"
//    val LANGUAGE_TYPE = "en" //Arabic - ar //English - en
//
//
//    private var gson: Gson? = null
//    private var context: Activity? = null
//    private lateinit var iPaymentRequestCallBack: IPaymentRequestCallBack
//    private var fortCallback: FortCallBackManager? = null
//    private var progressDialog: ProgressDialog? = null
//    private var sdkToken: String? = null
//    private var payFortData: PayFortData? = null
//
//    fun PayFortPayment(
//        context: Activity?,
//        fortCallback: FortCallBackManager?,
//        iPaymentRequestCallBack: IPaymentRequestCallBack?
//    ) {
//        this.context = context
//        this.fortCallback = fortCallback
//        if (iPaymentRequestCallBack != null) {
//            this.iPaymentRequestCallBack = iPaymentRequestCallBack
//        }
//        progressDialog = ProgressDialog(context)
//        progressDialog?.setMessage("Processing your payment\nPlease wait...")
//        progressDialog?.setCancelable(false)
//        sdkToken = ""
//        gson = Gson()
//    }
//
//    fun requestForPayment(payFortData: PayFortData?) {
//        this.payFortData = payFortData
//        GetTokenFromServer().execute(WS_GET_TOKEN)
//    }
//
//    private fun requestPurchase() {
//        try {
//            FortSdk.getInstance().registerCallback(context,
//                getPurchaseFortRequest(),
//                FortSdk.ENVIRONMENT.TEST,
//                RESPONSE_PURCHASE,
//                fortCallback,
//                object : OnTnxProcessed {
//                    override fun onSuccess(
//                        requestParamsMap: Map<String, Any>?,
//                        responseMap: MutableMap<String, Any>?
//                    ) {
//                        val response = JSONObject(responseMap as Map<*, *>)
//                        val payFortData =
//                            gson!!.fromJson(response.toString(), PayFortData::class.java)
//                        payFortData.paymentResponse = response.toString()
//                        Log.e("Cancel Response", response.toString())
//                        if (iPaymentRequestCallBack != null) {
//                            iPaymentRequestCallBack.onPaymentRequestResponse(
//                                RESPONSE_PURCHASE_CANCEL,
//                                payFortData
//                            )
//                        }
//                    }
//
//                    override fun onFailure(
//                        p0: MutableMap<String, Any>?,
//                        p1: MutableMap<String, Any>?
//                    ) {
//
//                    }
//
//                    override fun onCancel(
//                        p0: MutableMap<String, Any>?,
//                        p1: MutableMap<String, Any>?
//                    ) {
//
//                    }
////                override  fun onCancel(
////                        requestParamsMap: kotlin.collections.Map<String?, String>,
////                        responseMap: kotlin.collections.Map<String?, String>
////                    ) {
////                        val response = JSONObject(responseMap)
////                        val payFortData =
////                            gson!!.fromJson(response.toString(), PayFortData::class.java)
////                        payFortData.paymentResponse = response.toString()
////                        Log.e("Cancel Response", response.toString())
////                        if (iPaymentRequestCallBack != null) {
////                            iPaymentRequestCallBack.onPaymentRequestResponse(
////                                RESPONSE_PURCHASE_CANCEL,
////                                payFortData
////                            )
////                        }
////                    }
//
////                    override  fun onSuccess(
////                        requestParamsMap: kotlin.collections.Map<String, String>,
////                        fortResponseMap: kotlin.collections.Map<String, String>
////                    ) {
////                        val response = JSONObject(fortResponseMap)
////                        val payFortData =
////                            gson!!.fromJson(response.toString(), PayFortData::class.java)
////                        payFortData.paymentResponse = response.toString()
////                        Log.e("Success Response", response.toString())
////                        if (iPaymentRequestCallBack != null) {
////                            iPaymentRequestCallBack.onPaymentRequestResponse(
////                                RESPONSE_PURCHASE_SUCCESS,
////                                payFortData
////                            )
////                        }
////                    }
////
////                    override fun onFailure(
////                        requestParamsMap: kotlin.collections.Map<String, String>,
////                        fortResponseMap: kotlin.collections.Map<String, String>
////                    ) {
////                        val response = JSONObject(fortResponseMap)
////                        val payFortData =
////                            gson!!.fromJson(response.toString(), PayFortData::class.java)
////                        payFortData.paymentResponse = response.toString()
////                        Log.e("Failure Response", response.toString())
////                        if (iPaymentRequestCallBack != null) {
////                            iPaymentRequestCallBack.onPaymentRequestResponse(
////                                RESPONSE_PURCHASE_FAILURE,
////                                payFortData
////                            )
////                        }
////                    }
//                })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun getPurchaseFortRequest(): FortRequest? {
//        val fortRequest = FortRequest()
//        if (payFortData != null) {
//            val parameters = HashMap<String, Any?> ()
//            parameters["amount"] = payFortData?.amount.toString()
//            parameters["command"] = payFortData?.command
//            parameters["currency"] = payFortData?.currency
//            parameters["customer_email"] = payFortData?.customerEmail
//            parameters["language"] = payFortData?.language
//            parameters["merchant_reference"] = payFortData?.merchantReference
//            parameters["sdk_token"] = sdkToken
//            fortRequest.requestMap = parameters
//        }
//        return fortRequest
//    }
//
//    inner class GetTokenFromServer :
//        AsyncTask<String?, String?, String>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDialog?.show()
//        }
//
//        protected override fun doInBackground(vararg postParams: String?): String {
//            var response = ""
//            try {
//                val conn: HttpURLConnection
//                val url = URL(postParams[0]?.replace(" ", "%20"))
//                conn = url.openConnection() as HttpURLConnection
//                conn.requestMethod = "POST"
//                conn.setRequestProperty("content-type", "application/json")
//                val str: String? = getTokenParams()
//                val outputInBytes = str?.toByteArray(charset("UTF-8"))
//                val os = conn.outputStream
//                os.write(outputInBytes)
//                os.close()
//                conn.connect()
//                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
//                    val inputStream = conn.inputStream
//                    response = convertStreamToString(inputStream)!!
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            return response
//        }
//
//        override fun onPostExecute(response: String) {
//            super.onPostExecute(response)
//            progressDialog?.hide()
//            Log.e("Response", response + "")
//            try {
//                val payFortData: PayFortData = gson!!.fromJson(response, PayFortData::class.java)
//                if (!TextUtils.isEmpty(payFortData.sdkToken)) {
//                    sdkToken = payFortData.sdkToken
//                    requestPurchase()
//                } else {
//                    payFortData.paymentResponse = response
//                    iPaymentRequestCallBack.onPaymentRequestResponse(
//                        RESPONSE_GET_TOKEN,
//                        payFortData
//                    )
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//
//    }
//
//    fun getTokenParams(): String? {
//        val jsonObject = JSONObject()
//        try {
//            val device_id = FortSdk.getDeviceId(context)
//            val concatenatedString = SHA_REQUEST_PHRASE +
//                    KEY_ACCESS_CODE + "=" + ACCESS_CODE +
//                    KEY_DEVICE_ID + "=" + device_id +
//                    KEY_LANGUAGE + "=" + LANGUAGE_TYPE +
//                    KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
//                    KEY_SERVICE_COMMAND + "=" + SDK_TOKEN +
//                    SHA_REQUEST_PHRASE
//            jsonObject.put(KEY_SERVICE_COMMAND, SDK_TOKEN)
//            jsonObject.put(KEY_MERCHANT_IDENTIFIER, MERCHANT_IDENTIFIER)
//            jsonObject.put(KEY_ACCESS_CODE, ACCESS_CODE)
//            val signature = getSignatureSHA256(concatenatedString)
//            jsonObject.put(KEY_SIGNATURE, signature)
//            jsonObject.put(KEY_DEVICE_ID, device_id)
//            jsonObject.put(KEY_LANGUAGE, LANGUAGE_TYPE)
//            Log.e("concatenatedString", concatenatedString)
//            Log.e("signature", signature)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        Log.e("JsonString", jsonObject.toString())
//        return jsonObject.toString()
//    }
//
//    private fun convertStreamToString(inputStream: InputStream?): String? {
//        if (inputStream == null) return null
//        val sb = StringBuilder()
//        try {
//            val r =
//                BufferedReader(InputStreamReader(inputStream), 1024)
//            var line: String?
//            while (r.readLine().also { line = it } != null) {
//                sb.append(line)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return sb.toString()
//    }
//
//    private fun getSignatureSHA256(s: String): String {
//        try {
//            // Create MD5 Hash
//            val digest =
//                MessageDigest.getInstance(SHA_TYPE)
//            digest.update(s.toByteArray())
//            val messageDigest = digest.digest()
//            return String.format(
//                "%0" + messageDigest.size * 2 + 'x',
//                BigInteger(1, messageDigest)
//            )
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        }
//        return ""
//    }
//}