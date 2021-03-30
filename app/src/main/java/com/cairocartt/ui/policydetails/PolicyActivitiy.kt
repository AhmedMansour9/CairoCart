package com.cairocartt.ui.policydetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.databinding.ActivityPolicyActivitiyBinding

class PolicyActivitiy : BaseActivity<ActivityPolicyActivitiyBinding>() {

    override var idLayoutRes: Int
        get() = R.layout.activity_policy_activitiy
        set(value) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var Link= intent.getStringExtra("data")
        Link?.let { mViewDataBinding.WebView.loadUrl(it) }
        mViewDataBinding.WebView.settings.javaScriptEnabled=true


    }


}