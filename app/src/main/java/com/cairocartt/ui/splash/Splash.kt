package com.cairocartt.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.NonNull
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.databinding.ActivitySplash2Binding
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.onboarding.OnBoardingActivity
import com.cairocartt.utils.SharedData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Splash : BaseActivity<ActivitySplash2Binding>() {
    private var data: SharedData? = null

    override var idLayoutRes: Int = R.layout.activity_splash2
    private var cdt: CountDownTimer? = null
    private var checkIntro:String?= String()
    private var Lang:String?= String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = SharedData(this)
        checkIntro=data?.getValue(SharedData.ReturnValue.STRING, "slider")
        Lang=data?.getValue(SharedData.ReturnValue.STRING, "Lann")
        Lang?.let { Lingver.getInstance().setLocale(this, it, "") }
         getTokenFirebase()
        if(!checkIntro.isNullOrEmpty()){
          navigateToHome()
        }else
        {
            navigateToSlider()
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
                    val token: String? = task.getResult()
                    data?.putValue("access_firebase",token)
                }
            })

    }

    private fun navigateToHome() {
        setupCounterDown {
            startActivity(Intent(this, BottomNavigateFragment::class.java))
            finish()
        }

    }

    private fun navigateToSlider() {
        setupCounterDown {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }

    }

    private fun setupCounterDown(action: () -> Unit) {
        cdt = object : CountDownTimer(1000, 3000) {
            override fun onFinish() {
                action()
            }

            override fun onTick(p0: Long) {

            }
        }
        cdt?.start()
    }


    override fun onStop() {
        cdt?.cancel()
        super.onStop()
    }
}