

package com.cairocartt.ui.addreview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.cairocartt.R
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.FragmentAddReviewBinding
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import dagger.hilt.android.AndroidEntryPoint


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class AddReviewFragment: DialogFragment() ,AddReviewNavigator{
    lateinit var mViewDataBinding:FragmentAddReviewBinding
    private lateinit var details: ProductsResponse.Data
    private var bundle = Bundle()
    private var data: SharedData? = null
    val mViewModel: AddReviewViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_review,
            container,
            false
        )
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mViewDataBinding.reviewViewModel=mViewModel
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.navigator=this
        data = SharedData(requireContext())
        getData()
        setupObserver()

    }

    private fun setupObserver() {
        mViewModel.reviewResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    dismiss()
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.success_addreview),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.LOADING -> {
                    showLoadingg()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showLoadingg() {
        mViewDataBinding.Progress.visibility=View.VISIBLE
    }

    private fun dismissLoading() {
        mViewDataBinding.Progress.visibility=View.GONE
    }

    private fun getData() {
        bundle = this.requireArguments()
        details = bundle.getParcelable("item")!!
        mViewModel.addReview.entityPkValue= details.id!!
        mViewModel.addReview.reviewEntity="product"
        mViewModel.addReview.reviewStatus=2
        mViewModel.addReview.customerId = data?.getValue(SharedData.ReturnValue.STRING, "id")!!

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onClickReview() {
     mViewModel.raddReview("5u1forfnoiuok9qtdaftqxtyd399bcsl")
    }
}