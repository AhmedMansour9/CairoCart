package com.cairocartt.ui.subcategories

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
import com.cairocartt.adapter.*
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.CategoryFragmentBinding
import com.cairocartt.databinding.FragmentSubCategoriesBinding
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.detailsproduct.DetailsProductFragment
import com.cairocartt.ui.detailsproduct.DetailsProductViewModel
import com.cairocartt.ui.login.LoginActivity
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.ui.productsbyId.ProductByIdNavigator
import com.cairocartt.ui.productsbyId.ProductsByIdViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
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
 * Use the [SubCategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SubCategoriesFragment : BaseFragment<FragmentSubCategoriesBinding>(), SwipeRefreshLayout.OnRefreshListener,
    ProductByIdNavigator {
    var linearView = true
    private var searchJob: Job? = null
    private val productReviwesViewModel: DetailsProductViewModel by viewModels()
    private var token: String? = String()
    private lateinit var productsGridAdapter : ProductsGridByIdAdapter
    private lateinit var productsFeatureAdapter : ProductFeatureAdapter
    var postion=0
    val mViewModel: FeaturedViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    val mViewModel2: ProductsByIdViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    override var idLayoutRes: Int = R.layout.fragment_sub_categories
    private var data: SharedData? = null
    var details: CategoriesResponse.DataCategory?=null
    private lateinit var detailsProduct: ProductsResponse.Data


    private  var catAdapter= LinearCategoriesAdapter(object : LinearCategoriesAdapter.CategoryItemListener{
        override fun itemClicked(productData: CategoriesResponse.DataCategory.ChildrenDataa) {
            val bundle = Bundle()
            bundle.putParcelable("cat", productData)
            Navigation.findNavController(mViewDataBinding.root)
                .navigate(R.id.action_subCategoriesFragment_to_productsById, bundle);
        }

    })

    var bundle = Bundle()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.navigator=this
        data = SharedData(requireContext())

        initAdapter()
        initAdapterFeature()
        getData()
        initRecycle()
        init()
        initGridUI()
        initRecycleFeature()
        setupView()
        setupViewFeature()
        subscribeAddFavourit()
        subscriberemoveFavourit()
        subscribeChangesCatId()
    }
    private fun setupView() {
        searchJob = lifecycleScope.launch {
            mViewModel2.listData.collect {
                productsGridAdapter.submitData(it)
            }
        }
    }
    private fun setupViewFeature() {
        searchJob = lifecycleScope.launch {
            mViewModel.listDataFeatured.collect {
                productsFeatureAdapter.submitData(it)
            }
        }
    }
    private fun getData() {
        bundle = this.requireArguments()
        details=bundle.getParcelable("cat")
        details?.childrenData?.let { catAdapter.setList(it) }
        mViewModel2.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewModel2.userId.value= data?.getValue(SharedData.ReturnValue.STRING, "id")
        productsGridAdapter.token=data?.getValue(SharedData.ReturnValue.STRING, "token")
        mViewModel2.category_Id.value=details?.id.toString()
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewModel.userId.value= data?.getValue(SharedData.ReturnValue.STRING, "id")
        productsFeatureAdapter.token=data?.getValue(SharedData.ReturnValue.STRING, "token")
        mViewModel.category_Id.value=details?.id.toString()
        mViewDataBinding.TTitle.text=details?.name

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

    private fun initRecycleFeature(){
        mViewDataBinding.recyclerFeatured.isMotionEventSplittingEnabled=false
        mViewDataBinding.recyclerFeatured.setLayoutManager(LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false))
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

    private fun initRecycle() {
        mViewDataBinding.recyclerCategroies.setHasFixedSize(true)
        mViewDataBinding.recyclerCategroies.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        mViewDataBinding.recyclerCategroies.adapter = catAdapter

    }
    private fun init() {
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)
        mViewDataBinding.productsViewModel = mViewModel
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)

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

    fun initAdapterFeature(){
        productsFeatureAdapter = ProductFeatureAdapter(requireContext(),productData = object :
            ProductFeatureAdapter.ProductItemListener {
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

    fun showFeatureProduct(){
        mViewDataBinding.TFeature.visibility=View.VISIBLE
        mViewDataBinding.SwipCategories.visibility=View.VISIBLE

    }

    fun checkIsFavourit(postion: Boolean) {
        if (postion) {
            token?.let { it1 -> productReviwesViewModel.removeFavourit(detailsProduct.id.toString(), it1) }
        } else {
            token?.let { it1 -> productReviwesViewModel.addFavourit(detailsProduct.id.toString(), it1) }
        }

    }


    override fun onRefresh() {
        mViewDataBinding.SwipCategories.isRefreshing=false
        if(requireContext().isConnected){
          productsGridAdapter.refresh()
        }else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        productsGridAdapter.type=postion
        checkInternet()
    }

    fun checkInternet(){
        if(!requireContext().isConnected){
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }
    private fun checkEmpryProduct(it : CombinedLoadStates){
        if (it.source.refresh is LoadState.Error && productsGridAdapter.itemCount==0){
            showEmptyPage()
        }
    }

    private fun checkEmptyFeatureProduct(it : CombinedLoadStates){
        if (it.source.refresh is LoadState.Error && productsFeatureAdapter.itemCount==0){

        }else if( productsFeatureAdapter.itemCount>0) {
            showFeatureProduct()
        }
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


    override fun openHome() {
        val intent=Intent(requireContext(), BottomNavigateFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        hideShimmer()
//        postion=0
        productsGridAdapter.type = postion
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

    private fun changeViewList() {
        if (linearView) {
            mViewDataBinding.recyclerProductsGrid.setLayoutManager(
                LinearLayoutManager(
                    requireContext()
                )
            )
            postion=0
            productsGridAdapter.type = postion
        } else {
            mViewDataBinding.recyclerProductsGrid.setLayoutManager(
                GridLayoutManager(
                    requireContext(), 2
                )
            )
            postion=1
            productsGridAdapter.type = postion
        }
    }

    override fun onResume() {
        super.onResume()
        productsGridAdapter.type = postion
    }


}