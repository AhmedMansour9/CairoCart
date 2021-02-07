package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ListFavouritResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.RowFavouritproductBinding
import com.cairocartt.databinding.RowProductBinding
import com.cairocartt.databinding.RowProductgridBinding
import kotlinx.android.synthetic.main.row_product.view.*

class FavouritProductsAdapter ( var productData: ProductItemListener) :
    PagingDataAdapter<ListFavouritResponse.Data, RecyclerView.ViewHolder>(
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


    override fun getItemViewType(position: Int): Int {
        if (type == 0) return 0 else return 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): RecyclerView.ViewHolder {

            return ProductsGridViewHolder(
                RowFavouritproductBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
    }

    inner class ProductsGridViewHolder(var mTradersResponse: RowFavouritproductBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root) {

        fun onBind(position: ListFavouritResponse.Data?) {

            mTradersResponse.model = position

            itemView.Img_Favourit.setOnClickListener { view ->
                run {
                    productData.itemFavourit(position!!.product)
                }
            }

            itemView.setOnClickListener { view ->
                run {
                    productData.itemClicked(position!!.product)
                }
            }
        }


    }


    inner class ProductsLinearViewHolder(var mTradersResponse: RowFavouritproductBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root) {

        fun onBind(position: ListFavouritResponse.Data?) {
            mTradersResponse.model = position

            fun checkToken(){
                if(!token.isNullOrEmpty()){
                    if(position!!.product.isInWishlist){
                        position.product.isInWishlist=false
                        itemView.Img_Favourit.setImageResource(R.drawable.ic_emptyfavourit)
                    }else{
                        position.product.isInWishlist=true
                        itemView.Img_Favourit.setImageResource(R.drawable.ic_favourit)
                    }
                }
            }


            itemView.Img_Favourit.setOnClickListener { view ->
                run {
                    productData.itemFavourit(position!!.product)
                    checkToken()
                }
            }

            itemView.setOnClickListener { view ->
                run {
                    productData.itemClicked(position!!.product)

                }
            }
        }


    }


    interface ProductItemListener {
        fun itemClicked(productData: ProductsResponse.Data?);
        fun itemFavourit(productData: ProductsResponse.Data);

    }

    object DataDifferntiator : DiffUtil.ItemCallback<ListFavouritResponse.Data>() {

        override fun areItemsTheSame(
            oldItem: ListFavouritResponse.Data,
            newItem: ListFavouritResponse.Data
        ): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(
            oldItem: ListFavouritResponse.Data,
            newItem: ListFavouritResponse.Data
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ProductsGridViewHolder).onBind(getItem(position))
    }


}