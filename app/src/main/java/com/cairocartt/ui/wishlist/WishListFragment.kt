package com.cairocartt.ui.wishlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cairocartt.R
import com.cairocartt.adapter.FavouritProductsAdapter
import com.cairocartt.adapter.LoadStateViewHolder
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.FragmentWishListBinding
import com.cairocartt.ui.detailsproduct.DetailsProductViewModel
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
 * Use the [WishListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class WishListFragment : BaseFragment<FragmentWishListBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    override var idLayoutRes: Int = R.layout.fragment_wish_list

    var linearView = true
    var bundle = Bundle()
    private var searchJob: Job? = null
    private val productReviwesViewModel: DetailsProductViewModel by viewModels()
    private var data: SharedData? = null
    private var token: String? = String()
    private lateinit var detailsProduct: ProductsResponse.Data

    private val productsGridAdapter = FavouritProductsAdapter(productData = object :
        FavouritProductsAdapter.ProductItemListener {
        override fun itemClicked(productData: ProductsResponse.Data?) {
            val bundle2 = Bundle()
            bundle2.putParcelable("item", productData)
            Navigation.findNavController(mViewDataBinding.root)
                .navigate(R.id.action_wishListFragment_to_detailsProductFragment, bundle2);
        }

        override fun itemFavourit(productData: ProductsResponse.Data) {
            detailsProduct = productData
            token = data?.getValue(SharedData.ReturnValue.STRING, "token")
            checkIsFavourit()
        }
    })


    val mViewModel: WishListViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initGridUI()
        setupView()
        subscriberemoveFavourit()


    }


    private fun init() {
        mViewDataBinding.productsViewModel = mViewModel
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)
        data = SharedData(requireContext())
        mViewModel.token.value=data?.getValue(SharedData.ReturnValue.STRING, "token")
    }


    private fun setupView() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            mViewModel.listData.collect {
                productsGridAdapter.submitData(it)
            }
        }


    }

    private fun initGridUI() {

        mViewDataBinding.recyclerProductsGrid.setLayoutManager(
            GridLayoutManager(
                requireContext(), 2
            )
        )
        mViewDataBinding.recyclerProductsGrid.adapter = productsGridAdapter
//        productsGridAdapter.type = 1
        mViewDataBinding.recyclerProductsGrid.adapter =
            productsGridAdapter.withLoadStateFooter(footer = LoadStateViewHolder.LoadingStateAdapter {
                productsGridAdapter.retry()
            })

        productsGridAdapter.addLoadStateListener {
            val status = it.source.refresh is LoadState.Loading
            if (status) {
                hideEmptyPage()
                showShimmer()
            } else {
                hideShimmer()
                checkEmpryProduct(it)

            }

        }

    }
    private fun checkEmpryProduct(it : CombinedLoadStates){
        if (it.source.refresh is LoadState.NotLoading && productsGridAdapter.itemCount==0){
            showEmptyPage()
        }
    }
    fun showEmptyPage(){
        mViewDataBinding.RelaEmpty.visibility= View.VISIBLE
    }
    fun hideEmptyPage(){
        mViewDataBinding.RelaEmpty.visibility= View.INVISIBLE
    }

    fun showShimmer() {
        hideEmptyPage()
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible = true
    }

    fun hideShimmer() {
        mViewDataBinding.shimmerLayout.stopShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible = false
    }


    override fun onStop() {
        super.onStop()
        hideShimmer()
        productsGridAdapter.type = 0
    }

    override fun onRefresh() {
        mViewDataBinding.SwipCategories.isRefreshing = false
        productsGridAdapter.refresh()
    }


    private fun subscriberemoveFavourit() {
        productReviwesViewModel.removeFavouritResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.remove_favourit),
                        Toast.LENGTH_SHORT
                    ).show()
                    dismissLoading()
                    productsGridAdapter.refresh()
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

    fun checkIsFavourit() {
            token?.let { it1 ->
                productReviwesViewModel.removeFavourit(
                    detailsProduct.id.toString(),
                    it1
                )
            }
    }


}