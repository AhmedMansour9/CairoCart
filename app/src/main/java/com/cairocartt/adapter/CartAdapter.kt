
package com.cairocartt.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ListCartResponse
import com.cairocartt.databinding.ItemCartBinding
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter( context: Context,var itemclick:CartItemListner) :
    RecyclerView.Adapter<CartAdapter.DeveloperViewHolder>() {
     var context:Context

    init {
        this.context=context
    }
    private var mListCart: MutableList<ListCartResponse.Data.Item>? = arrayListOf()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<ItemCartBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_cart, viewGroup, false
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

    fun setDeveloperList(mDeveloperModel: MutableList<ListCartResponse.Data.Item>) {
        this.mListCart = mDeveloperModel
        notifyDataSetChanged()
    }

    inner class DeveloperViewHolder(var mListResponse: ItemCartBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        var counter = 1
        var totalPrice: Int = 1

        fun onBind(position: ListCartResponse.Data.Item) {
            setTotal(position)
            onClickPlus(position)
            onCLickMinus(position)
            onClickDelete(position)




        }

         fun onClickDelete(position: ListCartResponse.Data.Item) {
             itemView.Img_Delete.setOnClickListener(){
                 run {
                     itemclick.deleteItem(position.itemId.toString())
                 }
             }
        }

        fun onCLickMinus(position:ListCartResponse.Data.Item){
            itemView.Img_Minus.setOnClickListener() {
                checkMinCart()

                run {
                    itemclick.ediItem(position.itemId.toString(),position.sku!!,itemView.T_Quantity.text.toString())
                }
            }
        }

        fun onClickPlus(position:ListCartResponse.Data.Item){
            itemView.Img_Plus.setOnClickListener() {
                checkMaxCart(position)
                run {
                    itemclick.ediItem(position.itemId.toString(),position.sku!!,itemView.T_Quantity.text.toString())
                }
            }
        }

        fun setTotal(position:ListCartResponse.Data.Item){
            var total = position.price.toDouble() * position.qty
            itemView.T_TotalPrice.text = context.resources.getString(R.string.total_price) +" "+
                total.toString() + context.resources.getString(R.string.currency)
        }
        @SuppressLint("SetTextI18n")
        fun checkMaxCart(details: ListCartResponse.Data.Item) {
            var counter = itemView.T_Quantity.text.toString().toInt()
            val countValue: Int = details.maxQty!!
            val defult = itemView.T_Quantity.text.toString().toInt()
            if (countValue > defult) {
                counter++
                itemView.T_Quantity.text = counter.toString()

            } else {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.max_cart) + " " + details.maxQty,
                    Toast.LENGTH_SHORT
                ).show()
                itemView.Img_Plus.isEnabled=false
            }

        }

        @SuppressLint("SetTextI18n")
        fun checkMinCart() {
            var counter = itemView.T_Quantity.text.toString().toInt()
            if (counter > 1) {
                itemView.Img_Plus.isEnabled=true
                counter--
                itemView.T_Quantity.text = counter.toString()
            }
        }
    }

    interface CartItemListner{

        fun ediItem(item_id:String,sku:String,qty:String)

        fun deleteItem(item:String)

    }

}