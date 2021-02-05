package com.cairocartt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.RowProductBinding
import com.cairocartt.databinding.RowProductgridBinding
import com.cairocartt.databinding.RowRelatedBinding
import kotlinx.android.synthetic.main.row_product.view.*

class RelatedAdapter (context: Context, var productData: ProductItemListener) :
    PagingDataAdapter<ProductsResponse.Data, RecyclerView.ViewHolder>(
        DataDifferntiator
    ) {
    var token: String? = String()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var type: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var context: Context
    init {
        this.context=context
    }



    override fun getItemViewType(position: Int): Int {
        if (type == 0) return 0 else return 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerView.ViewHolder {

            return ProductsGridViewHolder(
                RowRelatedBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )


    }

    inner class ProductsGridViewHolder(var mTradersResponse: RowRelatedBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root) {

        fun onBind(position: ProductsResponse.Data?) {

            mTradersResponse.model = position



            itemView.setOnClickListener { view ->
                run {
                    productData.itemClicked(position)
                }
            }
        }


    }




    interface ProductItemListener {
        fun itemClicked(productData: ProductsResponse.Data?);

    }

    object DataDifferntiator : DiffUtil.ItemCallback<ProductsResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: ProductsResponse.Data,
            newItem: ProductsResponse.Data
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductsResponse.Data,
            newItem: ProductsResponse.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            (holder as ProductsGridViewHolder).onBind(getItem(position))

    }

}