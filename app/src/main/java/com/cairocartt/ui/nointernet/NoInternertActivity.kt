package com.cairocartt.ui.nointernet

import android.os.Bundle
import android.view.Window
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.ActivityNointernetBinding
import com.cairocartt.utils.isConnected
import org.greenrobot.eventbus.EventBus

class NoInternertActivity : BaseActivity<ActivityNointernetBinding>() {

    override var idLayoutRes: Int = R.layout.activity_nointernet

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        mViewDataBinding.BtnTryAgain.setOnClickListener(){
            if(this.isConnected) {
                EventBus.getDefault().postSticky(MessageEvent("network"))
                finish()
            }
        }

    }
}