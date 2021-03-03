package com.cairocartt.data.remote.model

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.sdk.android.dependancies.base.FortInterfaces.OnTnxProcessed
import com.payfort.sdk.android.dependancies.models.FortRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.HttpURLConnection
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class PayFortPayment {


}