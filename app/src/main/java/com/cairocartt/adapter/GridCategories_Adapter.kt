package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.databinding.ItemCategoryBinding
import com.cairocartt.databinding.ItemReviewsBinding

class GridCategories_Adapter(var categoryData: CategoryItemListener) : RecyclerView.Adapter<GridCategories_Adapter.DeveloperViewHolder>() ,
    Filterable {

    private var mListModel: MutableList<CategoriesResponse.DataCategory>? = arrayListOf()

    var filtered: MutableList<CategoriesResponse.DataCategory>? = ArrayList<CategoriesResponse.DataCategory>()

    var mArrayList: MutableList<CategoriesResponse.DataCategory>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<ItemCategoryBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_category, viewGroup, false
        )

        return DeveloperViewHolder(mTradersResponse)
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
        val currentStudent = mListModel?.get(i)
        mDeveloperViewHolder.mListMo.model = currentStudent
        mDeveloperViewHolder.onBind(i)



    }


    override fun getItemCount(): Int {
        return if (mListModel != null) {
            mListModel!!.size
        } else {
            0
        }
    }

    fun setList(mDeveloperModel: MutableList<CategoriesResponse.DataCategory>) {
        this.mListModel = mDeveloperModel
        this.mArrayList = mDeveloperModel

        notifyDataSetChanged()
    }

    inner class DeveloperViewHolder(var mListMo: ItemCategoryBinding) :
        RecyclerView.ViewHolder(mListMo.root){

        fun onBind(position: Int) {

            itemView.setOnClickListener { view ->
                run {
                    categoryData.itemClicked(mListModel?.get(position)!!)

                }
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                filtered?.clear()
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mListModel = mArrayList
                } else {
                    for (androidVersion in mArrayList!!) {
                        if (androidVersion.name?.toLowerCase()!!.contains(charString)) {
                            filtered?.add(androidVersion)
                        }
                    }
                    mListModel = filtered
                }
                val filterResults = FilterResults()
                filterResults.values = mListModel
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResul: FilterResults) {
                mListModel = filterResul.values as MutableList<CategoriesResponse.DataCategory>?
                notifyDataSetChanged()
            }
        }
    }
    interface CategoryItemListener {
        fun itemClicked(productData: CategoriesResponse.DataCategory);

    }

}