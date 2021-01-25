package com.cairocartt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.HomeResponse
import com.cairocartt.databinding.RowMenusectionsBinding
import kotlinx.android.synthetic.main.row_menusections.view.*

class HomeAdapter (var context:Context,var itemclick: CartItemListner) :
    RecyclerView.Adapter<HomeAdapter.DeveloperViewHolder>() {
    private var mListOrders: MutableList<HomeResponse.Data>? = arrayListOf()

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<RowMenusectionsBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.row_menusections, viewGroup, false
        )

        return DeveloperViewHolder(mTradersResponse)
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
        val currentStudent = mListOrders?.get(i)
        mDeveloperViewHolder.onBind(mListOrders?.get(i))
      setChildAdapter(mDeveloperViewHolder,currentStudent,mListOrders?.get(i)?.type)


    }

    fun setChildAdapter(mDeveloperViewHolder:DeveloperViewHolder,currentList:HomeResponse.Data?,type:String?){
       if(type.equals("big_card_and_small_cards")){
           var i=2
           val childLayoutManager = GridLayoutManager(mDeveloperViewHolder.itemView.rvMenuChildSections.context,2,GridLayoutManager.HORIZONTAL,false)
           mDeveloperViewHolder.itemView.rvMenuChildSections.apply {
               layoutManager = childLayoutManager
               childLayoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
                   override fun getSpanSize(position: Int): Int {
                       return when (adapter?.getItemViewType(position)) {
                           ChildHomeAdapter.VIEW_TYPE_FIRST -> 2
                           ChildHomeAdapter.VIEW_TYPE_ITEM  -> 1
                           else -> -1
                       }
                   }
               })
               adapter = currentList?.cards?.let { ChildHomeAdapter(it,type!!,context) }
               setRecycledViewPool(viewPool)
           }
       }else {
           val childLayoutManager = LinearLayoutManager(mDeveloperViewHolder.itemView.rvMenuChildSections.context, LinearLayoutManager.HORIZONTAL, false)
           mDeveloperViewHolder.itemView.rvMenuChildSections.apply {
               layoutManager = childLayoutManager
               adapter = currentList?.cards?.let { ChildHomeAdapter(it,type!!,context) }
               setRecycledViewPool(viewPool)
           }
       }
    }

    fun setList(mDeveloperModel: MutableList<HomeResponse.Data>) {
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


    inner class DeveloperViewHolder(var mTradersResponse: RowMenusectionsBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root) {
        fun onBind(position: HomeResponse.Data?) {
            itemView.setOnClickListener() {
                itemclick.onclick(position!!)
            }

        }


    }

    interface CartItemListner {

        fun onclick(list: HomeResponse.Data)

    }
}