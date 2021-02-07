package com.cairocartt.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cairocartt.ChangeLanguage
import com.cairocartt.R
import com.cairocartt.adapter.HomeAdapter
import com.cairocartt.base.BaseFragment
import com.cairocartt.data.remote.model.HomeResponse
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.FragmentHomeBinding
import com.cairocartt.ui.bottomnavigate.BottomNavigateFragment
import com.cairocartt.ui.detailsproduct.DetailsProductFragment
import com.cairocartt.ui.nointernet.NoInternertActivity
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.cairocartt.utils.isConnected
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class HomeFragment  : BaseFragment<FragmentHomeBinding>(), SwipeRefreshLayout.OnRefreshListener{

    override var idLayoutRes: Int = R.layout.fragment_home
    private var data: SharedData? = null

    val mViewModel: HomeViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }

    private lateinit var homeAdapter : HomeAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initRecycle()
        init()
        categoryObserver()
        SearchKeyBoard()
        productsObserver()
    }

    private fun SearchKeyBoard() {
        mViewDataBinding.ESearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!mViewDataBinding.ESearch.text.toString().isNullOrEmpty()) {
                    val bundle2 = Bundle()
                    bundle2.putString("search", mViewDataBinding.ESearch.text.toString())
                    Navigation.findNavController(mViewDataBinding.root)
                        .navigate(R.id.action_homeFragment_to_searchResultProduct, bundle2);                }
                return@OnEditorActionListener true
            }
            false
        })
    }
    private fun initAdapter() {
        homeAdapter=  HomeAdapter(requireContext(),object : HomeAdapter.CartItemListner {
                override fun onclick(list: HomeResponse.Data) {
//                val newDialogFragment = MyOrderDetails()
//                var bundle=Bundle()
//                bundle.putParcelable("list",list)
//                newDialogFragment.arguments=bundle
//                val transaction: FragmentTransaction =
//                    requireActivity().supportFragmentManager.beginTransaction()
//                newDialogFragment.show(transaction, "New_Dialog_Fragment")
                }
            })

        }



    private fun initRecycle() {
        mViewDataBinding.recyclerCategroies.setHasFixedSize(true)
        mViewDataBinding.recyclerCategroies.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mViewDataBinding.recyclerCategroies.adapter = homeAdapter

    }
    private fun init() {
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        mViewDataBinding.SwipCategories.setOnRefreshListener(this)
        data = SharedData(requireContext())

    }

    private fun categoryObserver() {
        mViewModel.homeResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    hideShimmer ()
                    addData(it.data?.data as MutableList<HomeResponse.Data>)
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

    private fun productsObserver() {
        mViewModel.productResponse.observe(viewLifecycleOwner, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading ()
                    addDetailsProduct(it.data?.data as MutableList<ProductsResponse.Data>)
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading ()
                }
            }
        })
    }

    private fun addDetailsProduct(mutableList: MutableList<ProductsResponse.Data>) {
        val newDialogFragment = DetailsProductFragment()
        val bundle2 = Bundle()
        bundle2.putParcelable("item", mutableList.get(0))
        newDialogFragment.arguments=bundle2
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        newDialogFragment.show(transaction, "New_Dialog_Fragment")


    }


    fun showShimmer(){
        mViewDataBinding.recyclerCategroies.visibility= View.GONE
        mViewDataBinding.shimmerLayout.startShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible=true
    }
    fun hideShimmer(){
        mViewDataBinding.recyclerCategroies.visibility= View.VISIBLE
        mViewDataBinding.shimmerLayout.stopShimmerAnimation()
        mViewDataBinding.shimmerLayout.isVisible=false
    }

    private fun addData(data: MutableList<HomeResponse.Data>) {
        homeAdapter.setList(data)


    }




    override fun onRefresh() {
        mViewDataBinding.SwipCategories.isRefreshing=false
        if(requireContext().isConnected){
            mViewModel.getHome()
        }else {
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        checkInternet()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        checkNotiocationType()
    }

    fun checkInternet(){
        if(!requireContext().isConnected){
            startActivity(Intent(requireContext(), NoInternertActivity::class.java))
        }
    }


    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messsg: MessageEvent) {/* Do something */
        Log.d("IGNORE", "Logging view to curb warnings: $messsg")
        if(messsg.Message.equals("product"))
        {
            messsg.product_Id?.let { mViewModel.getDetailsProduct(it) }
        }

    };
    private fun checkNotiocationType(){
        if(!BottomNavigateFragment.notificationType.isNullOrEmpty()){
          if(BottomNavigateFragment.notificationType.equals("brand")){
              var bundle = Bundle()
              bundle.putString("brand_Id", BottomNavigateFragment.id)
              mViewDataBinding.root.findNavController()
                  .navigate(R.id.action_homeFragment_to_resultFiltertionFragment, bundle)

          }else  if(BottomNavigateFragment.notificationType.equals("category")){
              var bundle = Bundle()
              bundle.putString("id",BottomNavigateFragment.id)
              mViewDataBinding.root.findNavController()
                  .navigate(R.id.action_homeFragment_to_productsById, bundle)

          } else  if(BottomNavigateFragment.notificationType.equals("product")){
              BottomNavigateFragment.id?.let { mViewModel.getDetailsProduct(it) }
          }

        }
    }

}