package com.cairocartt.ui.forgetpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cairocartt.R
import com.cairocartt.base.BaseActivity
import com.cairocartt.databinding.ActivityForgetPasswordBinding
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_forget_password.*
import java.util.regex.Pattern

@AndroidEntryPoint
class ForgetPasswordActivity : BaseActivity<ActivityForgetPasswordBinding>() {
    override var idLayoutRes: Int = R.layout.activity_forget_password
    private val mViewModel: ForgetPasswordViewModel by viewModels()
    var EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
        forgetPassword()
    }


    private fun setupObserver() {
        mViewModel.accountResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                   mViewDataBinding.Progress.visibility= View.GONE
                    it.data?.status?.code?.let { it1 -> checkData(it1) }
                }
                Status.LOADING -> {
                    mViewDataBinding.Progress.visibility= View.VISIBLE

                }

                Status.ERROR -> {
                    mViewDataBinding.Progress.visibility= View.GONE
                    error(resources.getString(R.string.error),resources.getString(R.string.wrong_email))
                }
            }
        })
    }
    private fun checkData(code:Int){
        mViewDataBinding.EEmail.setText("")
        if(code==200){
            Toast.makeText(this, resources.getString(R.string.email_sentsucces), Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this, resources.getString(R.string.wrong_email), Toast.LENGTH_LONG).show()

        }
    }

     fun forgetPassword() {
         mViewDataBinding.Confirm.setOnClickListener(){
           if(validateEmail()){
               if(!this.isConnected){
                   startActivity(Intent(this, NoInternertActivity::class.java))
               }
               mViewModel.forgetPassword(mViewDataBinding.EEmail.text.toString())

           }

         }
    }

    fun validateEmail(): Boolean {
      var  Email = mViewDataBinding.EEmail.text.toString()
        if (!EMAIL_ADDRESS_PATTERN.matcher(Email).matches()) {
            Toast.makeText(
                this,
                resources.getString(R.string.validateEmail),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true

    }
}