package com.cairocartt.ui.editprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cairocartt.R
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.ProfileResponse
import com.cairocartt.databinding.FragmentEditProfileBinding
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(),EditProfileNavigator {

    override var idLayoutRes: Int=R.layout.fragment_edit_profile
    private val mViewModel: EditProfileViewModel by viewModels()
    private var token: String? = String()
    private var data: SharedData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = SharedData(requireContext())
        mViewDataBinding.registerViewModel = mViewModel
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        mViewModel.navigator = this
        setupObserver()
        setupObserverProfile()
        lifecycleScope.launch {
            mViewModel.getProfile(token!!)
        }
    }

    private fun setupObserver() {
        mViewModel.editProfileResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), resources.getString(R.string.updated), Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })
    }
    fun setData(profile:ProfileResponse){
        mViewDataBinding.EName.setText(profile.data?.customer?.firstname.toString())
        mViewDataBinding.ELastname.setText(profile.data?.customer?.lastname.toString())
        mViewDataBinding.EEmail.setText(profile.data?.customer?.email.toString())

    }

    private fun setupObserverProfile() {
        mViewModel.getProfileResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    it.data?.let { it1 -> setData(it1) }
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })
    }
    override fun onClickSaveChanges() {
        mViewModel.editProfile(token!!)

    }


}