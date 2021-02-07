package com.cairocartt.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cairocartt.ChangeLanguage
import com.cairocartt.R
import com.cairocartt.adapter.BrandsFiltertionAdapter
import com.cairocartt.adapter.CatgoriesFliterAdapter
import com.cairocartt.base.BaseDialogFragment
import com.cairocartt.data.remote.model.Brands_Response
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.FilterModel
import com.cairocartt.databinding.FragmentDetailsProductBinding
import com.cairocartt.databinding.FragmentFiltertionBinding
import com.cairocartt.ui.productsbyId.ProductsByIdViewModel
import com.cairocartt.utils.SharedData
import com.cairocartt.utils.Status
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FiltertionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class FiltertionFragment : BaseDialogFragment<FragmentFiltertionBinding>(), FiltertionNavigator {

    override var idLayoutRes: Int = R.layout.fragment_filtertion

    private var data: SharedData? = null
    private var cat_Id:String?= String()
    private var brand_Id:String?= String()

    val mViewModelShared: ProductsByIdViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }


    val mViewModel: FiltertionViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    private var catAdapter =
        CatgoriesFliterAdapter(object : CatgoriesFliterAdapter.CategoryItemListener {
            override fun itemClicked(productData: CategoriesResponse.DataCategory.ChildrenDataa) {
                cat_Id=productData.id.toString()
                mViewDataBinding.TCategory.text=productData.name
                checkedToggleCategory()
                checkedToggleBrand()
            }

        })

    private var brandAdapter =
        BrandsFiltertionAdapter(object : BrandsFiltertionAdapter.BrandtemListener {
            override fun itemClicked(productData: Brands_Response.Data) {
                brand_Id=productData.optionId.toString()
                mViewDataBinding.TBrand.text=productData.title
                checkedToggleBrand()
            }
        })

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecycleCategory()
        initRecycleBrand()
        initRecycleBrand()
        init()
        categoryObserver()
        brandObserver()
        onchangeSeekBar()
    }



    private fun onchangeSeekBar() {
        mViewDataBinding.seekbar.setOnRangeSeekbarChangeListener(object :OnRangeSeekbarChangeListener {
            override fun valueChanged(minValue:Number, maxValue:Number ) {
                mViewDataBinding.TMinPrice.text=minValue.toString() +" " + resources.getString(R.string.currency)
                mViewDataBinding.TMax.text=maxValue.toString() +" " + resources.getString(R.string.currency)
            }
        });
    }


    private fun initRecycleCategory() {
        mViewDataBinding.recyclerCategroies.setHasFixedSize(true)
        mViewDataBinding.recyclerCategroies.setLayoutManager(
            LinearLayoutManager(
                requireContext()
            )
        )
        mViewDataBinding.recyclerCategroies.adapter = catAdapter

    }

    private fun initRecycleBrand() {
        mViewDataBinding.recyclerBrand.setHasFixedSize(true)
        mViewDataBinding.recyclerBrand.setLayoutManager(
            LinearLayoutManager(
                requireContext()
            )
        )
        mViewDataBinding.recyclerBrand.adapter = brandAdapter

    }


    private fun init() {
        mViewModel.Lang.value = ChangeLanguage.getLanguage(requireContext())
        data = SharedData(requireContext())
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel
    }

    private fun categoryObserver() {
        mViewModel.categoryResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    addData(it.data?.data?.childrenData as MutableList<CategoriesResponse.DataCategory.ChildrenDataa>)
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

    private fun brandObserver() {
        mViewModel.brandResponse.observe(this, Observer {
            when (it.staus) {
                Status.SUCCESS -> {
                    dismissLoading()
                    addDataBrand(it.data?.data as MutableList<Brands_Response.Data>)
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

    private fun addData(data: MutableList<CategoriesResponse.DataCategory.ChildrenDataa>) {
        catAdapter.setList(data)

    }

    private fun addDataBrand(data: MutableList<Brands_Response.Data>) {
        brandAdapter.setList(data)

    }
    override fun onClickToggleCategory() {
      checkedToggleCategory()
    }
    private fun checkedToggleCategory(){
        var checked = toggleArrow(mViewDataBinding.btToggleCategory)
        if (checked) {
            mViewDataBinding.lytExpandCategory.visibility = View.VISIBLE
        } else {
            mViewDataBinding.lytExpandCategory.visibility = View.GONE
        }
    }

    override fun onClickToggleBrand() {
       checkedToggleBrand()
    }
    private fun checkedToggleBrand(){
        var checked = toggleArrow(mViewDataBinding.btToggleBrand)
        if (checked) {
            mViewDataBinding.lytExpandBrand.visibility = View.VISIBLE
        } else {
            mViewDataBinding.lytExpandBrand.visibility = View.GONE
        }
    }

    override fun onCLickFinish() {
        val filter=(FilterModel(cat_Id,brand_Id,mViewDataBinding.seekbar.selectedMinValue.toString(),mViewDataBinding.seekbar.selectedMaxValue.toString()))
        mViewModelShared.filter.value=filter
        dismiss()
    }

    override fun clearAll() {
        catAdapter.lastSelectedPosition=-1
        catAdapter.notifyDataSetChanged()
        brandAdapter.lastSelectedPosition=-1
        brandAdapter.notifyDataSetChanged()

    }


    fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }
}