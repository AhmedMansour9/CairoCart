package com.cairocartt.ui.productsbyId

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
import com.cairocartt.adapter.ProductFeatureAdapter
import com.cairocartt.adapter.ProductsGridByIdAdapter
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.FilterResponse
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.FragmentProductsByIdBinding
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.detailsproduct.DetailsProductFragment
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.ui.detailsproduct.DetailsProductViewModel
import com.cairocartt.ui.filter.FiltertionFragment
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.ui.resultfilter.ResultFitertionViewModel
import com.cairocartt.ui.subcategories.FeaturedViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class ProductsById : BaseFragment<FragmentProductsByIdBinding>(), ProductByIdNavigator,
    SwipeRefreshLayout.OnRefreshListener {
    var linearView = true
    var details: CategoriesResponse.DataCategory.ChildrenDataa? = null
    var bundle = Bundle()
    private var searchJob: Job? = null
    private val productReviwesViewModel: DetailsProductViewModel by viewModels()
    override var idLayoutRes: Int = R.layout.fragment_products_by_id
    private var data: SharedData? = null
    private var token: String? = String()
    private lateinit var detailsProduct: ProductsResponse.Data
    private lateinit var productsGridAdapter: ProductsGridByIdAdapter
    var postion = 0
    var category_Id: String? = String()
    private lateinit var productsFeatureAdapter: ProductFeatureAdapter
    val mViewModel2: FeaturedViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    val mViewModel: ProductsByIdViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }

    val mViewModelFilter: ResultFitertionViewModel by viewModels ()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.navigator = this
        mViewModel2.navigator = this
        initAdapterFeature()
        init()
        initAdapter()
        getData()
        initGridUI()
        initRecycleFeature()
        setupView()
        setupViewFeature()
        subscribeAddFavourit()
        subscriberemoveFavourit()
        subscribeChangesCatId()
        subscribeFiltertion()
        subscribegetFilters()
        SearchKeyBoard()


    }

    private fun setupViewFeature() {
        searchJob = lifecycleScope.launch {
            mViewModel2.listDataFeatured.collect {
                productsFeatureAdapter.submitData(it)
            }
        }
    }

    private fun initRecycleFeature() {
        mViewDataBinding.recyclerFeatured.isMotionEventSplittingEnabled = false
        mViewDataBinding.recyclerFeatured.setLayoutManager(
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        mViewDataBinding.recyclerFeatured.adapter = productsFeatureAdapter
        mViewDataBinding.recyclerFeatured.adapter =
            productsFeatureAdapter.withLoadStateFooter(footer = LoadStateViewHolder.LoadingStateAdapter {
                productsFeatureAdapter.retry()
            })

        productsFeatureAdapter.addLoadStateListener {
            val status = it.source.refresh is LoadState.Loading
            if (!status) {
                checkEmptyFeatureProduct(it)
            }


        }
    }

    private fun checkEmptyFeatureProduct(it: CombinedLoadStates) {
        if (it.source.refresh is LoadState.Error && productsFeatureAdapter.itemCount == 0) {

        } else if (productsFeatureAdapter.itemCount > 0) {
            showFeatureProduct()
        }
    }

    fun showFeatureProduct() {
        mViewDataBinding.TFeature.visibility = View.VISIBLE
        mViewDataBinding.SwipCategories.visibility = View.VISIBLE

    }

    fun initAdapterFeature() {
        productsFeatureAdapter = ProductFeatureAdapter(requireContext(), productData = object :
            ProductFeatureAdapter.ProductItemListener {
            override fun itemClicked(productData: ProductsResponse.Data?) {
                val newDialogFragment = DetailsProductFragment()
                val bundle2 = Bundle()
                bundle2.putParcelable("item", productData)
                newDialogFragment.arguments = bundle2
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                newDialogFragment.show(transaction, "New_Dialog_Fragment")

            }

            override fun itemFavourit(productData: ProductsResponse.Data?) {
                detailsProduct = productData!!
                token = data?.getValue(SharedData.ReturnValue.STRING, "token")
                if (!token.isNullOrEmpty()) {
                    checkIsFavourit(detailsProduct.isInWishlist!!)
                } else {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
            }
        })
    }

    fun initAdapter() {
        productsGridAdapter = ProductsGridByIdAdapter(requireContext(), productData = object :
            ProductsGridByIdAdapter.ProductItemListener {
            override fun itemClicked(productData: ProductsResponse.Data?) {
                val newDialogFragment = DetailsProductFragment()
                val bundle2 = Bundle()
                bundle2.putParcelable("item", productData)
                newDialogFragment.arguments = bundle2
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                newDialogFragment.show(transaction, "New_Dialog_Fragment")

            }

            override fun itemFavourit(productData: ProductsResponse.Data?) {
                detailsProduct = productData!!
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


    private fun setupView() {
        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            mViewModel.listData.collect {
                productsGridAdapter.submitData(it)
            }
        }


    }

    private fun getData() {
        bundle = this.requireArguments()
        if (bundle.getString("id").isNullOrEmpty()) {
            details = bundle.getParcelable("cat")
            mViewDataBinding.TTitle.text = details?.name
            category_Id = details?.id.toString()
        } else {
            category_Id = bundle.getString("id")
            mViewDataBinding.TTitle.setText(resources.getString(R.string.product))
        }

        mViewModel2.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewModel2.userId.value = data?.getValue(SharedData.ReturnValue.STRING, "id")
        productsGridAdapter.token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        mViewModel2.category_Id.value = category_Id.toString()
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewModel.userId.value = data?.getValue(SharedData.ReturnValue.STRING, "id")
        productsFeatureAdapter.token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        mViewModel.category_Id.value = category_Id.toString()

    }

    private fun initGridUI() {
        mViewDataBinding.recyclerProductsGrid.isMotionEventSplittingEnabled = false
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

    private fun checkEmpryProduct(it: CombinedLoadStates) {
        if (it.source.refresh is LoadState.Error && productsGridAdapter.itemCount == 0) {
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

    fun showEmptyPage() {
        mViewDataBinding.RelaEmpty.visibility = View.VISIBLE
    }

    fun hideEmptyPage() {
        mViewDataBinding.RelaEmpty.visibility = View.INVISIBLE
    }


    private fun changeViewList() {
        if (linearView) {
            mViewDataBinding.recyclerProductsGrid.setLayoutManager(
                LinearLayoutManager(
                    requireContext()
                )
            )
            postion = 0
            productsGridAdapter.type = postion
        } else {
            mViewDataBinding.recyclerProductsGrid.setLayoutManager(
                GridLayoutManager(
                    requireContext(), 2
                )
            )
            postion = 1
            productsGridAdapter.type = postion
        }
    }

    override fun onClickGrid() {
        selectGrid()
        unselectLinear()
        changeViewList()

    }

    override fun onClickLinear() {
        selectLinear()
        unselectGrid()
        changeViewList()
    }

    override fun onClickFilter() {
        mViewModel.getFilters()
    }

    override fun openHome() {
        val intent = Intent(requireContext(), BottomNavigateFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun selectGrid() {
        linearView = false
        mViewDataBinding.BtnGrid.setImageResource(R.drawable.ic_grid)
        mViewDataBinding.RelaGrid.setBackgroundResource(R.drawable.bc_righttoggle)
    }

    fun unselectGrid() {
        mViewDataBinding.BtnGrid.setImageResource(R.drawable.ic_nongrid)
        mViewDataBinding.RelaGrid.setBackgroundColor(
            Color.TRANSPARENT
        )
    }

    fun selectLinear() {
        linearView = true
        mViewDataBinding.ImgLinear.setImageResource(R.drawable.ic_linearselect)
        mViewDataBinding.RelaLinear.setBackgroundResource(R.drawable.bc_lefttoggle)
    }

    fun unselectLinear() {
        mViewDataBinding.ImgLinear.setImageResource(R.drawable.ic_linearsnonelected)
        mViewDataBinding.RelaLinear.setBackgroundColor(
            Color.TRANSPARENT
        )

    }

    override fun onStop() {
        super.onStop()
        hideShimmer()

//        productsGridAdapter.type = 0
    }

    override fun onResume() {
        super.onResume()
        productsGridAdapter.type = postion

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

    private fun subscribegetFilters() {
        mViewModel.filtersResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    setFilterData(it.data)
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

    private fun setFilterData(data: FilterResponse?) {
        var bundle = Bundle()
        bundle.putParcelable("data", data)
        bundle.putString("category_id", category_Id)

        val newDialogFragment = FiltertionFragment()
        newDialogFragment.arguments = bundle
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        newDialogFragment.show(transaction, "New_Dialog_Fragment")
    }

    private fun subscribeChangesCatId() {
        mViewModel.checkChanges.observe(viewLifecycleOwner, Observer {
            if (it) {
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
            token?.let { it1 ->
                productReviwesViewModel.removeFavourit(
                    detailsProduct.id.toString(),
                    it1
                )
            }
        } else {
            token?.let { it1 ->
                productReviwesViewModel.addFavourit(
                    detailsProduct.id.toString(),
                    it1
                )
            }
        }

    }


    @Subscribe(sticky = false, threadMode = ThreadMode.BACKGROUND)
    fun onMessageEvent(messsg: MessageEvent) {/* Do something */
        if (messsg.Message.equals("login")) {
            productsGridAdapter.token = data?.getValue(SharedData.ReturnValue.STRING, "token")
        }
        if (messsg.Message.equals("network")) {
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

    fun checkInternet() {
        if (!requireContext().isConnected) {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }

    fun refreshData() {
        if (requireContext().isConnected) {
            productsGridAdapter.refresh()
        } else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }


    private fun subscribeFiltertion() {
        mViewModel.filter.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                var bundle=Bundle()
                bundle.putParcelableArrayList("list",it)
                bundle.putString("cat_Id", category_Id)
                Log.e("TAG", "subscribeFiltertion: "+mViewModel.filter.value )

                Navigation.findNavController(mViewDataBinding.root)
                    .navigate(R.id.action_productsById_to_resultFiltertionFragment,bundle);
                mViewModel.filter.value?.clear()
                mViewModel.filter.value = null
            }
        })
    }

    private fun SearchKeyBoard() {
        mViewDataBinding.ESearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!mViewDataBinding.ESearch.text.toString().isNullOrEmpty()) {
                    val bundle2 = Bundle()
                    bundle2.putString("cat_Id", category_Id)
                    bundle2.putString("search", mViewDataBinding.ESearch.text.toString())
                    Navigation.findNavController(mViewDataBinding.root)
                        .navigate(R.id.action_productsById_to_searchResultProduct, bundle2); }
                return@OnEditorActionListener true
            }
            false
        })
    }
}