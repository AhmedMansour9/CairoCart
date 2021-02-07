package com.cairocartt.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.LoginFragmentBinding
import com.cairocartt.ui.forgetpassword.ForgetPasswordActivity
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.ui.register.RegisterActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.util.*


@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginFragmentBinding>(), LoginNavigator {

    override var idLayoutRes: Int = R.layout.login_fragment
    private val mViewModel: LoginViewModel by viewModels()
    private var data: SharedData? = null
    lateinit var mAuth: FirebaseAuth
    lateinit var googleApiClient: GoogleApiClient
    var RequestSignInCode:Int=7
    lateinit var googleSignInOptions: GoogleSignInOptions
    private var callbackManager: CallbackManager? = null
    var socialid:String?= String()
    var email:String?=String()
    var name:String?=String()
    var type:String?=String()

    companion object {
        var token: String? = String()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.navigator = this
        mAuth = FirebaseAuth.getInstance();
        mViewDataBinding.loginViewModel = mViewModel
        data = SharedData(this)
        GoogleSignOpition();

        setupObserver()

    }

    private fun setupObserver() {
        mViewModel.accountResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    data?.putValue("quta_id",null)
                    data?.putValue("token", it.data?.data?.token)
                    data?.putValue("id", it.data?.data?.customer?.id.toString())

                    EventBus.getDefault().postSticky(MessageEvent("login"))
                    finish()
                }
                Status.LOADING -> {
                    showLoading()
                }

                Status.ERROR -> {
                    dismissLoading()
                    error(resources.getString(R.string.error),it.message.toString())
                }
            }
        })
    }

    override fun loginClick() {
        if(!this.isConnected){
            startActivity(Intent(this, NoInternertActivity::class.java))
        }
        mViewModel.login()
    }

    override fun createAccoutClick() {
        startActivity(Intent(this,RegisterActivity::class.java))
        finish()
    }

    override fun forgetPasswordClick() {
        startActivity(Intent(this,ForgetPasswordActivity::class.java))

    }

    override fun loginGoogleClick() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RequestSignInCode)

    }

    override fun loginFacebookClick() {
        // Login
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                        if (`object`.has("email")) {
                            email =`object`.get("email").toString()
                        }
                        if (`object`.has("id")) {
                            socialid =`object`.get("id").toString()
                        }
                        if (`object`.has("name")) {
                            name =`object`.get("name").toString()
                        }
                        type="facebook"
                        LoginFaceBooks(socialid,email,name)
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "name,email,id,picture.type(large)")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(applicationContext,error.toString(), Toast.LENGTH_LONG).show()
                }
            })



    }

    private fun GoogleSignOpition() {

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
        googleApiClient =  GoogleApiClient.Builder(applicationContext)
//                .enableAutoManage(applicationContext)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build();
    }






    fun LoginFaceBooks(id:String?,email:String? ,name:String?){
        mViewModel.loginSocial(type!!,id,email,name)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RequestSignInCode){
            var googleSignInResult: GoogleSignInResult =
                Auth.GoogleSignInApi.getSignInResultFromIntent(data)!!
            if (googleSignInResult.isSuccess()) {
                var googleSignInAccount: GoogleSignInAccount = googleSignInResult.signInAccount!!;
                FirebaseUserAuth(googleSignInAccount);
            }

        }

    }
    private fun FirebaseUserAuth(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    type="google"
                    LoginFaceBooks(
                        mAuth.currentUser!!.uid,
                        mAuth.currentUser!!.email,
                        mAuth.currentUser!!.displayName
                    )

                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }


}