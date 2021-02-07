package com.cairocartt.ui.myaccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.cairocartt.R
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.FragmentMyAccountBinding
import com.cairocartt.databinding.FragmentProductsByIdBinding
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.isConnected
import com.cutz.ui.languag.Language
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass.
 * Use the [MyAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>(), MyAccountNavigator {

    override var idLayoutRes: Int = R.layout.fragment_my_account
    private val mViewModel: MyAccountViewModel by viewModels()
    private var token: String? = String()
    private var data: SharedData? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
        mViewModel.navigator = this
        data = SharedData(requireContext())
        visablityData()
    }

    fun visablityData() {
        if (checkToken()) {
            mViewDataBinding.cardView.visibility = View.VISIBLE
            mViewDataBinding.cardLogin.visibility = View.INVISIBLE
        } else {
            mViewDataBinding.cardView.visibility = View.INVISIBLE
            mViewDataBinding.cardLogin.visibility = View.VISIBLE
            mViewDataBinding.ImgLogout.visibility = View.INVISIBLE
            mViewDataBinding.TLogout.visibility = View.INVISIBLE
        }
    }

    override fun onClickFavourit() {
        Navigation.findNavController(mViewDataBinding.root)
            .navigate(R.id.action_myAccountFragment_to_wishListFragment);
    }

    override fun onClickLogin() {
        startActivity(Intent(requireActivity(),LoginActivity::class.java))
    }

    override fun onClickProfile() {
        Navigation.findNavController(mViewDataBinding.root)
            .navigate(R.id.action_myAccountFragment_to_editProfileFragment);
    }

    override fun onClickLogOut() {
        data?.putValue("token", "")
        data?.putValue("id", "")
        data?.putValue("quta_id","")
        val intent=Intent(requireContext(), BottomNavigateFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    override fun onClickContactUs() {
        if(requireContext().isConnected){
            var bundle=Bundle()
            bundle.putString("data","https://cairocart.com/en/contact/")
            Navigation.findNavController(mViewDataBinding.root)
                .navigate(R.id.action_myAccountFragment_to_contactUsFragment,bundle);
        }else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }

    }

    override fun onClickAboutUs() {
        if(requireContext().isConnected){
            var bundle=Bundle()
            bundle.putString("data","https://cairocart.com/en/about-us/")
            Navigation.findNavController(mViewDataBinding.root)
                .navigate(R.id.action_myAccountFragment_to_contactUsFragment,bundle);
        }else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }

    }

    override fun onClickOrders() {
        Navigation.findNavController(mViewDataBinding.root)
            .navigate(R.id.action_myAccountFragment_to_myOrdersFragment);

    }

    override fun onClickLanguage() {
        startActivity(Intent(requireContext(), Language::class.java))

    }


    fun checkToken(): Boolean {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")

        if (token.isNullOrEmpty()) {
            return false
        }
        return true
    }


    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messsg: MessageEvent) {/* Do something */
        if(messsg.Message.equals("login")){
            token = data?.getValue(SharedData.ReturnValue.STRING, "token")
            visablityData()
        }
    };
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }
}