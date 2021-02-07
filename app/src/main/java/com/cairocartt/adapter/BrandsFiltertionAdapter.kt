package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.Brands_Response
import com.cairocartt.databinding.RowCategoryfilterBinding
import com.cairocartt.databinding.RowFilterbrandBinding
import kotlinx.android.synthetic.main.row_filterbrand.view.*
import kotlinx.android.synthetic.main.row_filterbrand.view.Frame

class BrandsFiltertionAdapter (var categoryData: BrandtemListener) : RecyclerView.Adapter<BrandsFiltertionAdapter.DeveloperViewHolder>() {

    private var mListModel: MutableList<Brands_Response.Data>? = arrayListOf()
     var lastSelectedPosition = -1


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<RowFilterbrandBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.row_filterbrand, viewGroup, false
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

    fun setList(mDeveloperModel: MutableList<Brands_Response.Data>) {
        this.mListModel = mDeveloperModel
        notifyDataSetChanged()
    }

    inner class DeveloperViewHolder(var mListMo: RowFilterbrandBinding) :
        RecyclerView.ViewHolder(mListMo.root){

        fun onBind(position: Int) {
                if (lastSelectedPosition == position) {
                    itemView.Frame.visibility = View.VISIBLE
                    itemView.RadioButton.setChecked(true);
                } else {
                    itemView.Frame.visibility = View.GONE
                    itemView.RadioButton.setChecked(false);
                }

                itemView.setOnClickListener() {
                    lastSelectedPosition = position
                    run {
                        categoryData.itemClicked(mListModel?.get(position)!!)
                    }
                    notifyDataSetChanged()
                }

        }

    }

    interface BrandtemListener {
        fun itemClicked(productData: Brands_Response.Data);

    }

}