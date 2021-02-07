package com.cairocartt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.data.remote.model.MyOrdersResponse
import com.cairocartt.databinding.ItemCartmyorderBinding
import com.cairocartt.databinding.ItemcartorderBinding
import kotlinx.android.synthetic.main.item_cart.view.*

class CartOrderDetailsAdapter (context: Context) :
    RecyclerView.Adapter<CartOrderDetailsAdapter.DeveloperViewHolder>() {
    var context: Context

    init {
        this.context=context
    }
    private var mListCart: MutableList<MyOrdersResponse.Data.MobileItem>? = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<ItemCartmyorderBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_cartmyorder, viewGroup, false
        )

        return DeveloperViewHolder(mTradersResponse)
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
        val currentStudent = mListCart!![i]
        mDeveloperViewHolder.mListResponse.model = currentStudent
        mDeveloperViewHolder.onBind(mListCart!![i])
    }

    override fun getItemCount(): Int {
        return if (mListCart != null) {
            mListCart!!.size
        } else {
            0
        }
    }

    fun setDeveloperList(mDeveloperModel: MutableList<MyOrdersResponse.Data.MobileItem>) {
        this.mListCart = mDeveloperModel
        notifyDataSetChanged()
    }
    inner class DeveloperViewHolder(var mListResponse: ItemCartmyorderBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        fun onBind(position: MyOrdersResponse.Data.MobileItem) {
            setTotal(position)
        }



        fun setTotal(position: MyOrdersResponse.Data.MobileItem){
            var total = position.price!!.toDouble() * position.qtyOrdered!!.toDouble()
            itemView.T_TotalPrice.text = context.resources.getString(R.string.total_price) +" "+
                    total.toString() + context.resources.getString(R.string.currency)
        }


    }


}