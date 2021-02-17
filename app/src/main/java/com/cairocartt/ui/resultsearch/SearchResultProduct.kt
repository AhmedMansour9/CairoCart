package com.cairocartt.ui.resultsearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cairocartt.ChangeLanguage
import com.cairocartt.R
import com.cairocartt.adapter.LoadStateViewHolder
import com.cairocartt.adapter.ProductsGridByIdAdapter
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.FragmentResultFiltertionBinding
import com.cairocartt.databinding.FragmentSearchResultProductBinding
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.detailsproduct.DetailsProductFragment
import com.cairocartt.ui.detailsproduct.DetailsProductViewModel
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.ui.resultfilter.ResultFitertionViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchResultProduct.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchResultProduct : BaseFragment<FragmentSearchResultProductBinding>(),
    SwipeRefreshLayout.OnRefreshListener {
    var linearView = true
    var bundle = Bundle()
    private var searchJob: Job? = null
    private val productReviwesViewModel: DetailsProductViewModel by viewModels()
    override var idLayoutRes: Int = R.layout.fragment_search_result_product
    private var data: SharedData? = null
    private var token: String? = String()
    private lateinit var detailsProduct: ProductsResponse.Data
    private lateinit var productsGridAdapter : ProductsGridByIdAdapter


    val mViewModel: SearchResultViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initAdapter()
        getData()
        initGridUI()
        setupView()
        subscribeAddFavourit()
        subscriberemoveFavourit()
        subscribeChangesCatId()
        openHome()
    }
    fun initAdapter(){
        productsGridAdapter = ProductsGridByIdAdapter(requireContext(),productData = object :
            ProductsGridByIdAdapter.ProductItemListener {
            override fun itemClicked(productData: ProductsResponse.Data?) {
                val newDialogFragment = DetailsProductFragment()
                val bundle2 = Bundle()
                bundle2.putParcelable("item", productData)
                newDialogFragment.arguments=bundle2
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                newDialogFragment.show(transaction, "New_Dialog_Fragment")

            }
            override fun itemFavourit(productData: ProductsResponse.Data?) {
                detailsProduct=productData!!
                token = data?.getValue(SharedData.ReturnValue.STRING, "token")
                if (!token.isNullOrEmpty()) {
                    checkIsFavourit(detailsProduct.isInWishlist!!)
                } else {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
            }
        })
    }


    private fun init() {
        mViewDataBinding.productsViewModel = mViewModel
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)
        data = SharedData(requireContext())
    }

    private  val TAG = "ResultFiltertionFragmen"
    private fun setupView() {
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            mViewModel.listData.collect {
                Log.d(TAG, "setupView: ")
                productsGridAdapter.submitData(it)
            }
        }



    }

    private fun getData() {
        bundle = this.requireArguments()
        mViewModel.category_Id.value=bundle.getString("cat_Id")
        mViewModel.search_term.value=bundle.getString("search")
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewModel.userId.value= data?.getValue(SharedData.ReturnValue.STRING, "id")
        productsGridAdapter.token=data?.getValue(SharedData.ReturnValue.STRING, "token")

    }

    private fun initGridUI() {
        mViewDataBinding.recyclerProductsGrid.isMotionEventSplittingEnabled=false
        mViewDataBinding.recyclerProductsGrid.setLayoutManager(LinearLayoutManager(requireContext()))
        mViewDataBinding.recyclerProductsGrid.adapter = productsGridAdapter
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
        if (it.source.refresh is LoadState.Error && productsGridAdapter.itemCount==0){
            showEmptyPage()
        }
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
    fun showEmptyPage(){
        mViewDataBinding.RelaEmpty.visibility=View.VISIBLE
    }
    fun hideEmptyPage(){
        mViewDataBinding.RelaEmpty.visibility=View.INVISIBLE
    }


    private fun changeViewList() {
        if (linearView) {
            mViewDataBinding.recyclerProductsGrid.setLayoutManager(
                LinearLayoutManager(
                    requireContext()
                )
            )
            productsGridAdapter.type = 0
        } else {
            mViewDataBinding.recyclerProductsGrid.setLayoutManager(
                GridLayoutManager(
                    requireContext(), 2
                )
            )
            productsGridAdapter.type = 1
        }
    }

    override fun onStop() {
        super.onStop()
        hideShimmer()
        productsGridAdapter.type = 0
    }

    override fun onRefresh() {
        mViewDataBinding.SwipCategories.isRefreshing = false

        refreshData()
    }


    private fun subscribeAddFavourit() {
        productReviwesViewModel.addFavouritResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.add_favourit),
                        Toast.LENGTH_SHORT
                    ).show()
                    dismissLoading()
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

    private fun subscribeChangesCatId() {
        mViewModel.checkChanges.observe(viewLifecycleOwner, Observer {
            if(it){
                productsGridAdapter.refresh()
            }
        })
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

    fun checkIsFavourit(postion: Boolean) {
        if (postion) {
            token?.let { it1 -> productReviwesViewModel.removeFavourit(detailsProduct.id.toString(), it1) }
        } else {
            token?.let { it1 -> productReviwesViewModel.addFavourit(detailsProduct.id.toString(), it1) }
        }

    }


    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messsg: MessageEvent) {/* Do something */
        if(messsg.Message.equals("login")){
            productsGridAdapter.token=data?.getValue(SharedData.ReturnValue.STRING, "token")
        }
        if(messsg.Message.equals("network")){
            refreshData()
        }

    };
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

    }


    override fun onStart() {
        super.onStart()
        checkInternet()
    }

    fun checkInternet(){
        if(!requireContext().isConnected){
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }
    fun refreshData(){
        if(requireContext().isConnected){
            productsGridAdapter.refresh()
        }else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }
    private fun openHome(){
        mViewDataBinding.BtnGoHome.setOnClickListener(){
            val intent= Intent(requireContext(), BottomNavigateFragment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}