package com.cairocartt.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure
import com.cairocartt.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kaopiz.kprogresshud.KProgressHUD


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentBottomSheetDialogFull.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseDialogFragment<T : ViewDataBinding>() : BottomSheetDialogFragment() {
    private var mBehavior: BottomSheetBehavior<*>? = null
    private lateinit var app_bar_layout: AppBarLayout
    private lateinit var lyt_profile: LinearLayout

    private lateinit var hud: KProgressHUD

    abstract var idLayoutRes: Int
    lateinit var mViewDataBinding: T

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog);
        }
        mViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            idLayoutRes,
            null,
            false
        )
        dialog.setContentView(mViewDataBinding.root)


        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupDataBinding();

    }


    private fun setupDataBinding() {
        mViewDataBinding.executePendingBindings()
    }

    fun setupHight(bottomsheet: View) {
        val layoutParams = bottomsheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomsheet.layoutParams = layoutParams
    }

    override fun onStart() {
        super.onStart()
//        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun showLoading() {
        if (this::hud.isInitialized) {
            if (!hud.isShowing) {
                load()
            }
        } else {
            load()
        }

    }

    fun load() {
        hud = KProgressHUD.create(requireActivity())
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setBackgroundColor(resources.getColor(R.color.blue))
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
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
            .setErrorButtonClick( Closure() {
                fun exec() {
                    dismiss()
                }
            })
            .show();
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(dialog: Dialog) {
        val window: Window? = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics)
            val dimDrawable = GradientDrawable()
            // ...customize your dim effect here
            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Color.WHITE)
            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)
            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)
            window.setBackgroundDrawable(windowBackground)
        }
    }

}