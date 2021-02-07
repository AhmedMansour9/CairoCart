package com.cairocartt.ui.contactus

import android.R.bool
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.cairocartt.R
import com.cairocartt.base.BaseFragment
import com.cairocartt.databinding.FragmentContactUsBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {

    override var idLayoutRes: Int = R.layout.fragment_contact_us
    lateinit var Link:String
    var bundle=Bundle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()

    }

    private fun getData() {
       bundle=this.requireArguments()
        Link= bundle.getString("data")!!
        mViewDataBinding.WebView.loadUrl(Link)
        mViewDataBinding.WebView.settings.javaScriptEnabled=true
        mViewDataBinding.WebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

    }
}