package com.cairocartt.base

//import com.github.ybq.android.spinkit.style.FoldingCube
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog
import com.cairocartt.R
import com.kaopiz.kprogresshud.KProgressHUD


abstract class BaseFragment<T : ViewDataBinding>() :
    Fragment() {


    abstract var idLayoutRes: Int
    lateinit var mViewDataBinding: T
    private var dailog: Dialog? = null

    //    private var foldingCube: FoldingCube? = null
    private lateinit var hud: KProgressHUD


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, idLayoutRes, container, false)
        return mViewDataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupDataBinding();

    }


    private fun setupDataBinding() {
        mViewDataBinding.executePendingBindings()
    }


    fun showLoading() {
        if (!this::hud.isInitialized) {
            hud = KProgressHUD.create(requireActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(resources.getColor(R.color.blue))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        } else {
            if (!hud.isShowing) {
                hud = KProgressHUD.create(requireActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setBackgroundColor(resources.getColor(R.color.blue))
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show()
            }
        }

    }

    fun dismissLoading() {
        if (this::hud.isInitialized) {
            if (hud.isShowing) {
                hud.dismiss()
            }
        }

    }


    fun error(title: String, msg: String) {
        AwesomeErrorDialog(requireContext())
            .setTitle(title)
            .setMessage(msg)
            .setColoredCircle(R.color.dialogErrorBackgroundColor)
            .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
            .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
            .setButtonText(getString(R.string.dialog_ok_button))
            .show();
    }


}