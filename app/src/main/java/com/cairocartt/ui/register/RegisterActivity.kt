package com.cairocartt.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.RegisterFragmentBinding
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class RegisterActivity : BaseActivity<RegisterFragmentBinding>(), RegisterNavigator {

    override var idLayoutRes: Int = R.layout.register_fragment

    private val mViewModel: RegisterViewModel by viewModels()

    private var data: SharedData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = SharedData(this)
        mViewDataBinding.registerViewModel = mViewModel
        mViewModel.navigator = this
        setupObserver()
    }

    override fun backLoginClick() {
       startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun sigupClick() {
        if (!this.isConnected) {
            startActivity(Intent(this, NoInternertActivity::class.java))
        }
        mViewModel.register()
    }

    private fun setupObserver() {
        mViewModel.accountResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    data?.putValue("token", it?.data?.data?.token)
                    data?.putValue("id", it?.data?.data?.customer?.id.toString())
                    data?.putValue("quta_id",null)

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

}