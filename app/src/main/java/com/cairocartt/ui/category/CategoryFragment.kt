package com.cairocartt.ui.category

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cairocartt.ChangeLanguage
import com.cairocartt.R
import com.cairocartt.TreeItemController
import com.cairocartt.adapter.CategoriesAdapter
import com.cairocartt.adapter.GridCategories_Adapter
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.CatModel
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.Node
import com.cairocartt.databinding.CategoryFragmentBinding
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryFragment : BaseFragment<CategoryFragmentBinding>(),SwipeRefreshLayout.OnRefreshListener,
    CategoriesAdapter.ItemListener {

    override var idLayoutRes: Int = R.layout.category_fragment
    private var data: SharedData? = null

    val mViewModel: CategoryViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    private  var catAdapter=GridCategories_Adapter(object : GridCategories_Adapter.CategoryItemListener{
        override fun itemClicked(productData: CategoriesResponse.DataCategory.ChildrenDataa) {
            if(productData.childrenData.size>0){
                val bundle = Bundle()
                bundle.putParcelable("cat", productData)
                Navigation.findNavController(mViewDataBinding.root)
                    .navigate(R.id.action_T_Categories_to_subCategoriesFragment, bundle);

            }else {
                val bundle = Bundle()
                bundle.putParcelable("cat", productData)
                Navigation.findNavController(mViewDataBinding.root)
                    .navigate(R.id.action_T_Categories_to_productsById, bundle);

            }
        }

    })

    private lateinit var controller :TreeItemController
    private   val TAG = "CategoryFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       initRecycle()
        init()
        categoryObserver()
        Filtertion()
    }

    private fun Filtertion() {
        mViewDataBinding.ESearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                catAdapter.getFilter().filter(s)

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })


    }
    private fun initRecycle() {
        mViewDataBinding.recyclerCategroies.setHasFixedSize(true)
        mViewDataBinding.recyclerCategroies.layoutManager =
            GridLayoutManager(context, 3)
        mViewDataBinding.recyclerCategroies.adapter = catAdapter

    }
    private fun init() {
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)
        data = SharedData(requireContext())

    }

    private fun categoryObserver() {
        mViewModel.categoryResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    hideShimmer ()
                    addData(it.data?.data?.childrenData as MutableList<CategoriesResponse.DataCategory.ChildrenDataa>)
                }
                Status.LOADING -> {
                   showShimmer()
                }
                Status.ERROR -> {
                   hideShimmer ()
                }
            }
        })
    }
//    fun initController(){
//        controller  = TreeItemController(this,
//            ::onCatModelClicked
//        )
//        mViewDataBinding.treeItemsRv.setController(controller)
//    }
    fun showShimmer(){
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible=true
    }
    fun hideShimmer(){
        mViewDataBinding.shimmerLayout.stopShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible=false
    }

    var tree: Node<CatModel>? = null
    private fun addData(data: MutableList<CategoriesResponse.DataCategory.ChildrenDataa>) {
        catAdapter.setList(data)

//        mViewDataBinding.treeItemsRv.isVisible=true
//
//        tree = data?.toTree()
//        tree?.let {
//            it.value.isExpanded=false
//            controller.setData(it)
//        }
    }


    private fun onCatModelClicked(node: Node<CatModel>) {
        Log.d(TAG, "onActivityCreated: ")
        if (!node.value.notChild) {
            Log.d(TAG, "onCatModelClicked: "+node.value.name +":"+node.value.isExpanded+":"+node.value.notChild)
            node.value = node.value.copy(isExpanded = !node.value.isExpanded)
            tree?.let {
                controller.setData(it)
            }


        }else{
            itemClicked(node.value)
        }
    }

    override fun itemClicked(node: CatModel) {
             val bundle = Bundle()
            bundle.putParcelable("cat", node)
            Navigation.findNavController(mViewDataBinding.root)
                .navigate(R.id.action_T_Categories_to_productsById, bundle);
    }



    override fun onRefresh() {
        mViewDataBinding.SwipCategories.isRefreshing=false
        if(requireContext().isConnected){
            mViewModel.getCategory()
        }else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
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

}