package com.cairocartt.base

import android.annotation.TargetApi
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure
import com.cairocartt.R
import com.cairocartt.utils.SharedData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.kaopiz.kprogresshud.KProgressHUD


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    abstract var idLayoutRes: Int

    open var colorStatusBar: Int = 0
    companion object {
        var token: String=""
    }
    lateinit var mViewDataBinding: T

    private var dailog: Dialog? = null

    private  lateinit var hud: KProgressHUD

    private var data: SharedData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        getTokenFirebase()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (colorStatusBar != 0)
                window.statusBarColor = colorStatusBar
        }
        mViewDataBinding = setContentView(this, idLayoutRes)
        mViewDataBinding.executePendingBindings()

       initFirebase()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().subscribeToTopic("android_topic")
            .addOnSuccessListener {
            }
    }

    private fun getTokenFirebase() {
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(object : OnCompleteListener<String> {
                override fun onComplete(@NonNull task: Task<String>) {
                    if (!task.isSuccessful()) {
                        return
                    }

                    // Get new FCM registration token
                    val tokenn: String? = task.getResult()
                    token=tokenn!!
                    data?.putValue("access_firebase",tokenn)
                }
            })

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun error(title:String,msg:String){
        AwesomeErrorDialog(this)
            .setTitle(title)
            .setMessage(msg)
            .setColoredCircle(R.color.dialogErrorBackgroundColor)
            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
            .setButtonText(getString(R.string.dialog_ok_button))
                  .setErrorButtonClick( Closure() {
                fun exec() {
                    finish()
                }
            })
            .show();
    }

    fun showLoading() {
        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setBackgroundColor(resources.getColor(R.color.blue))
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()

    }

    fun dismissLoading() {
        if( this::hud.isInitialized){
            if(hud.isShowing){
                hud.dismiss()
            }
        }    }

}