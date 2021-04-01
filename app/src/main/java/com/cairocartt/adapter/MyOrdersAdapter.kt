package com.cairocartt.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.data.remote.model.MyOrdersResponse
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.ItemMyorderBinding
import com.cairocartt.databinding.RowInformationBinding
import kotlinx.android.synthetic.main.item_myorder.view.*

class MyOrdersAdapter (var context:Context,var itemclick: CartItemListner) :
    RecyclerView.Adapter<MyOrdersAdapter.DeveloperViewHolder>() {
    private var mListOrders: MutableList<MyOrdersResponse.Data>? = arrayListOf()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<ItemMyorderBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_myorder, viewGroup, false
        )

        return DeveloperViewHolder(mTradersResponse)
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
        val currentStudent = mListOrders?.get(i)
        mDeveloperViewHolder.mTradersResponse.model = currentStudent
        mDeveloperViewHolder.onBind(mListOrders?.get(i))


    }

    fun setList(mDeveloperModel: MutableList<MyOrdersResponse.Data>) {
        this.mListOrders = mDeveloperModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mListOrders != null) {
            mListOrders!!.size
        } else {
            0
        }
    }


    inner class DeveloperViewHolder(var mTradersResponse: ItemMyorderBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root) {
        fun onBind(position: MyOrdersResponse.Data?) {
            setStatus(position)
            itemView.setOnClickListener() {
                itemclick.onclick(position!!)
            }

        }


        @SuppressLint("SetTextI18n")
        private fun setStatus(position: MyOrdersResponse.Data?) {
            if(position?.status.equals("processing")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.processing)
            }
            else if(position?.status.equals("pending_payment")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.pending_payment)

            }
            else if(position?.status.equals("pending")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.pending)

            }
            else if(position?.status.equals("holded")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.holded)

            }
            else if(position?.status.equals("complete")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.completed)
            }
            else if(position?.status.equals("canceled")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.canceled)
            }
            else if(position?.status.equals("closed")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.closed)
            }
            else if(position?.status.equals("payfort_fort_new")){
                itemView.T_Status.text=": "+context.resources.getString(R.string.payfort_payment)
            }
        }


    }

    interface CartItemListner {

        fun onclick(list: MyOrdersResponse.Data)

    }
}