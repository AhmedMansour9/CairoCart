package com.cairocartt.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cairocartt.ChangeLanguage
import com.cairocartt.R
import com.cairocartt.adapter.BrandsFiltertionAdapter
import com.cairocartt.adapter.CatgoriesFliterAdapter
import com.cairocartt.adapter.FilterAdapter
import com.cairocartt.base.BaseDialogFragment
import com.cairocartt.data.remote.model.Brands_Response
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.FilterModel
import com.cairocartt.data.remote.model.FilterResponse
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
    private var category_id: String? = String()
    private var brand_Id: String? = String()
    lateinit var filter: FilterResponse
    var list: ArrayList<FilterResponse.Data.Value> = arrayListOf()
    val mViewModel: ProductsByIdViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }

    private lateinit var catAdapter: FilterAdapter

    val mViewModelFiltertion : FiltertionViewModel by navGraphViewModels(R.id.graph_home) {
        defaultViewModelProviderFactory
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewDataBinding.viewmodel=mViewModelFiltertion
        mViewModelFiltertion.navigator=this
        getData()
        onchangeSeekBar()
    }

    private fun getData() {
        var bundle = Bundle()
        bundle = requireArguments()
        filter = bundle.getParcelable("data")!!
        category_id=bundle.getString("category_id")
        catAdapter = FilterAdapter(requireContext(), filter.data, object : FilterAdapter.FilterItemListner {
                override fun onclick(Field: String, filter: FilterResponse.Data.Value) {
                    filter.field=Field
                    addList(Field,filter)
                }

            })
        mViewDataBinding.recyclerCategroies.setAdapter(catAdapter)

//        mViewDataBinding.recyclerCategroies.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//
//            Toast.makeText(
//                requireContext(),
//                "" + filter.data.get(groupPosition).values.get(childPosition).label,
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return@setOnChildClickListener false
//        }


    }

    private fun addList(Field: String, filter: FilterResponse.Data.Value) {
        if (!list.isNullOrEmpty()) {
            list.forEachIndexed { index, value ->
                if (list.get(index).label.equals(filter.label)) {
                    return
                } else {
                    list.forEachIndexed { index2, value2 ->
                        run {
                            if (list.get(index2).field.equals(Field)) {
                                list.removeAt(index2)
                            }
                        }
                    }

                    list.add(filter)
                    return
                }
            }
        }else {
            list.add(filter)
        }
    }

    private fun onchangeSeekBar() {
        mViewDataBinding.seekbar.setOnRangeSeekbarChangeListener(object :
            OnRangeSeekbarChangeListener {
            override fun valueChanged(minValue: Number, maxValue: Number) {
                mViewDataBinding.TMinPrice.text =
                    minValue.toString() + " " + resources.getString(R.string.currency)
                mViewDataBinding.TMax.text =
                    maxValue.toString() + " " + resources.getString(R.string.currency)
            }
        });
    }


    override fun onClickToggleCategory() {
        checkedToggleCategory()
    }

    private fun checkedToggleCategory() {
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

    private fun checkedToggleBrand() {
        var checked = toggleArrow(mViewDataBinding.btToggleBrand)
        if (checked) {
            mViewDataBinding.lytExpandBrand.visibility = View.VISIBLE
        } else {
            mViewDataBinding.lytExpandBrand.visibility = View.GONE
        }
    }

    override fun onCLickFinish() {

        mViewModel.filter.value=list
        dismiss()
    }

    override fun clearAll() {
//        catAdapter.lastSelectedPosition = -1
//        catAdapter.notifyDataSetChanged()

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