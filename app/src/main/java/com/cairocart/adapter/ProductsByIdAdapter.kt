package com.cairocart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cairocart.data.remote.model.ProductsByIdResponse
import com.cairocart.databinding.RowProductBinding

class ProductsByIdAdapter(var productData:  ProductItemListener) : RecyclerView.Adapter<ProductsByIdAdapter.DeveloperViewHolder>() {

    private var mProductsModel = ArrayList<ProductsByIdResponse.Data>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            RowProductBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, position: Int) {
        mDeveloperViewHolder.onBind(position)


    }

    override fun getItemCount(): Int {
        return mProductsModel.size
    }

    fun setDeveloperList(mDeveloperModel: MutableList<ProductsByIdResponse.Data>) {
        this.mProductsModel.addAll(mDeveloperModel)
        this.notifyDataSetChanged()
    }

    inner class DeveloperViewHolder(var mTradersResponse: RowProductBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root) {

        fun onBind(position: Int) {
            val currentStudent = mProductsModel[position]
              mTradersResponse.model = currentStudent


            itemView.setOnClickListener { view ->
                run {
                    productData.itemClicked(currentStudent)
                }
            }
        }


    }

    interface ProductItemListener {
        fun itemClicked(productData: ProductsByIdResponse.Data);
    }
}