package com.cairocartt.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.data.remote.model.*
import com.cairocartt.databinding.ActivityPaymentBinding
import com.cairocartt.ui.createorder.CreateOrderViewModel
import com.cairocartt.ui.ordersuccess.OrderSuccessActivity
import com.cairocartt.utils.Status
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.sdk.android.dependancies.base.FortInterfaces
import com.payfort.sdk.android.dependancies.models.FortRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_payment.*
import org.greenrobot.eventbus.EventBus
import webconnect.com.webconnect.WebConnect
import webconnect.com.webconnect.listener.OnWebCallback
import java.security.MessageDigest


@AndroidEntryPoint
class PaymentActivity : BaseActivity<ActivityPaymentBinding>(),OnWebCallback  {

    override var idLayoutRes: Int = R.layout.activity_payment
    var fortCallback: FortCallBackManager? = null
    val TAG = "payTag"
    lateinit var deviceId: String
    lateinit var price: String
    lateinit var email: String
    lateinit var order_id: String
    val mViewModel: CreateOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        price=intent.getStringExtra("price")!!
        mViewDataBinding.price.text=price+" "+resources.getString(R.string.currency)
        email=intent.getStringExtra("email")!!
        order_id=intent.getStringExtra("order_id")!!
        subscribeConfirmPayment()
        /**
         * get device id using there SDK.
         * */
        deviceId = FortSdk.getDeviceId(this)

        /**
         * initialize Fort callback
         * */
        initFortCallback()

        /**
         * set onClick listener
         * */
        btn.setOnClickListener {
            /**
             * start purchase operation by getting access_token first
             * */
            getToknSdk()
        }

    }

    private fun subscribeConfirmPayment() {
        mViewModel.confirmPaymentResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    successOrder(it.data)
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun successOrder(data: ConfirmPaymentResponse?) {
        var intent = Intent(this, OrderSuccessActivity::class.java)
        intent.putExtra("id",order_id)
        startActivity(intent)


    }

    /**
     * return hashing of any string
     * note: we use SHA-256 Cryptographic Hash Algorithm
     *  but you should use same one selected in your payfort account.
     * */
    fun getHashString(t: String): String {
        val bytes = t.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashSt = digest.fold("", { str, it -> str + "%02x".format(it) })
        return hashSt
    }


    /**
     * get access token from payfort server for each purchase operation
     *  here I'm using retrofit library to call api,, you can use whatEver you are comfortable with it.
     * */
    private fun getToknSdk() {
//        textOne.text = "get signature..."
        val param = linkedMapOf<String, String>()

        /**
         * You should make one string with no space e.g key1=value1key2=value2
         * you should include all params
         * you should order them ASC
         * you should put  SHA request phrase ' in this example => Hello' at the beginning  e.g  Hellokey1=value1key2=value2
         * you should put  SHA response phrase ' in this example => Hello' at the end  e.g  Hellokey1=value1key2=value2Hellow
         *  check Docs for more details
         * */

        /**
         * you should get 'access_code' from your payfort account
         * */
        param.put("access_code", "ib5cglj92w5ti4THMJ37")
        param.put("device_id", deviceId)
        param.put("language", "en")
        /**
         * you should get 'merchant_identifier' from your payfort account
         * */
        param.put("merchant_identifier", "vmHrhBGr")
        param.put("service_command", "SDK_TOKEN")


        val sb = StringBuilder()
        /**
         * change 'Hello' by what you got from payFort.
        you will find it in your account*/
        sb.append("@2y@10@BoiB7lNl2")
        param.forEach { (k, v) ->
            sb.append("$k=$v")
//            sb.append(k)
//            sb.append("=")
//            sb.append(v)
        }
        sb.append("@2y@10@BoiB7lNl2")
        param.put("signature", getHashString(sb.toString()))

        /**
         * calling api https://sbpaymentservices.payfort.com/FortAPI/paymentApi
         *
         * */
        WebConnect.with(this, "paymentApi")
            .post()
            /**
             * put url depending on the Environment you targeting
             * */
            .baseUrl("https://sbpaymentservices.payfort.com/FortAPI/")
            .bodyParam(param)
            .taskId(11)
            .callback(WebCallback(this), ResponsePay::class.java, ErrorModel::class.java)
            .connect()

    }

    //region apis callback
    override fun <T : Any?> onSuccess(`object`: T?, taskId: Int) {
//        textOne.text = "TRY"

        if (`object` is ResponsePay) {
            if (`object`.sdk_token.isEmpty()) {
                Toast.makeText(this, "Error: ${`object`.response_message}", Toast.LENGTH_LONG).show()
            } else {
                startPayFortSdk(`object`.sdk_token)
            }
        }


    }

    override fun <T : Any?> onError(`object`: T?, error: String?, taskId: Int) {
//        textOne.text = "TRY"
        Toast.makeText(this, "onError $error", Toast.LENGTH_SHORT).show()

    }

    /**
     * start buy operation via SDK
     * put required keys and there values
     * */
    fun startPayFortSdk(sign: String) {
        val model = FortRequest()
        /**
         * to see response in log
         * */
        model.isShowResponsePage = true
        val hash = hashMapOf<String, String>()

//        getHashString.put("command", "PURCHASE")
        hash.put("command", "AUTHORIZATION")
        hash.put("customer_email", email)
        hash.put("currency", "EGP")

        /**
        = 10000 => 100, should be multi by some value depending on your currency, check payfort Docs fore more detail
         */
        var total:Int=price.toDouble().toInt() * 100
        hash.put("amount", total.toString())
        hash.put("language", "en")
        /**
         * merchant_reference represented purchase id, it should be unique
         * here we let user to entered as our test requirement.
         * */
//        val x = editOne.text.toString()
        hash.put("merchant_reference",order_id)
        /**
         * you can also add any option key-value pairs
         * */
//        getHashString.put("customer_name", "Sam")
//        getHashString.put("customer_ip", "172.150.16.10")
//        getHashString.put("payment_option", "VISA")
//        getHashString.put("eci", "ECOMMERCE")
//        getHashString.put("order_description", "DESCRIPTION")
        hash.put("sdk_token", sign)


        model.requestMap = hash.toMap()



        /**
         * start SDK
         *
         * user will input his info e.g card number...
         * then will  receive the result in callbacks bellow
         * */
        FortSdk
            .getInstance()
            .registerCallback(this, model,
                FortSdk.ENVIRONMENT.TEST, 5,
                fortCallback, true, object : FortInterfaces.OnTnxProcessed {
                    override fun onSuccess(p0: MutableMap<String, Any>?, hashMap: MutableMap<String, Any>?) {
                        Log.d(TAG, "onSuccess")
//                        Log.d(TAG, "onSuccess"+ p1.toString())
                        Toast.makeText(this@PaymentActivity, ""+hashMap?.get("authorization_code").toString(), Toast.LENGTH_SHORT).show()
                       var confirm =RequestConfirmPayment()
                        var request=RequestConfirmPayment.PaymentCallback()
                        request.authorizationCode=hashMap?.get("authorization_code") as String
                        request.cardNumber=hashMap?.get("card_number") as String
                        request.command=hashMap?.get("command") as String
                        request.customerIp=hashMap?.get("customer_ip") as String
                        request.expiryDate=hashMap?.get("expiry_date") as String
                        request.fortId=hashMap?.get("fort_id") as String
                        request.eci=hashMap?.get("eci") as String
                        request.language=hashMap?.get("language") as String
                        request.merchantReference=hashMap?.get("merchant_reference") as String
                        request.paymentOption=hashMap?.get("payment_option") as String
                        request.responseCode=hashMap?.get("response_code") as String
                        request.responseMessage=hashMap?.get("response_message") as String
                        request.sdkToken=hashMap?.get("sdk_token") as String
                        confirm.paymentCallback=request
                        mViewModel.confirmPayment(confirm)

                    }

                    override fun onFailure(p0: MutableMap<String, Any>?, p1: MutableMap<String, Any>?) {
                        Log.d(TAG, "onFailure")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                        Toast.makeText(this@PaymentActivity, "Error: ${p1?.get("response_message")}", Toast.LENGTH_LONG).show()

                        error(resources.getString(R.string.error), p1?.get("response_message").toString())
                    }

                    override fun onCancel(p0: MutableMap<String, Any>?, p1: MutableMap<String, Any>?) {
                        Log.d(TAG, "onCancel")
                        Log.d(TAG, p0.toString())
                        Log.d(TAG, p1.toString())
                        error(resources.getString(R.string.error), p1?.get("response_message").toString())

                    }

                })
    }

    fun initFortCallback() {
        fortCallback = FortCallBackManager.Factory.create();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        fortCallback!!.onActivityResult(requestCode, resultCode, data);
    }


}