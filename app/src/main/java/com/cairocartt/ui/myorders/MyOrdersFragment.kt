package com.cairocartt.ui.myorders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cairocartt.R
import com.cairocartt.adapter.CartAdapter
import com.cairocartt.adapter.MyOrdersAdapter
import com.cairocartt.adapter.ProductsGridByIdAdapter
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.MyOrdersResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.data.remote.model.RequestAddToCartResponse
import com.cairocartt.databinding.FragmentMyOrders2Binding
import com.cairocartt.ui.detailsproduct.DetailsProductViewModel
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.ui.orderdetails.MyOrderDetails
import com.cairocartt.ui.resultfilter.ResultFitertionViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyOrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MyOrdersFragment : BaseFragment<FragmentMyOrders2Binding>(), SwipeRefreshLayout.OnRefreshListener {

    override var idLayoutRes: Int = R.layout.fragment_my_orders2
    var bundle = Bundle()
    private var data: SharedData? = null
    private var token: String? = String()
    private lateinit var detailsProduct: ProductsResponse.Data
    private lateinit var myordersAdapter : MyOrdersAdapter


    val mViewModel: MyOrdersViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initAdapter()
        initRecycle()
        getOrders()
        subscribeMyOrder()

    }

    private fun initAdapter() {
        myordersAdapter = MyOrdersAdapter(requireContext(),object : MyOrdersAdapter.CartItemListner {
            override fun onclick(list: MyOrdersResponse.Data) {
                val newDialogFragment = MyOrderDetails()
                var bundle=Bundle()
                bundle.putParcelable("list",list)
                newDialogFragment.arguments=bundle
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                newDialogFragment.show(transaction, "New_Dialog_Fragment")
            }
        })
    }


    fun getOrders() {
        token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        mViewModel.getOrders(token)


    }

    private fun init() {
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        data = SharedData(requireContext())
    }

    private fun initRecycle() {
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)
        mViewDataBinding.recyclerProductsGrid.setHasFixedSize(true)
        mViewDataBinding.recyclerProductsGrid.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.recyclerProductsGrid.adapter = myordersAdapter
    }

    private fun subscribeMyOrder() {
        mViewModel.ordersResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    hideShimmer()
                    it.data?.let { it1 -> setData(it1) }
                }
                Status.LOADING -> {
                    showShimmer()
                }
                Status.ERROR -> {
                    hideShimmer()
                }
            }
        })
    }

    private fun setData(it1: MyOrdersResponse) {
        if(it1.data.size>0){
            myordersAdapter.setList(it1.data)
        }else {
            mViewDataBinding.RelaEmpty.visibility=View.VISIBLE
        }


    }


    fun showShimmer() {
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible = true
    }

    fun hideShimmer() {
        mViewDataBinding.shimmerLayout.stopShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible = false
    }

    override fun onRefresh() {
        mViewDataBinding.SwipCategories.isRefreshing = false
        mViewModel.getOrders(token)
    }
}