package com.cairocartt.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.FilterResponse
import com.cairocartt.data.remote.model.HomeResponse
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.*
import kotlinx.android.synthetic.main.row_categoryfilter.view.*
import org.greenrobot.eventbus.EventBus

class ChildFilterAdapter(
    var context: Context,
    var mListCart: MutableList<FilterResponse.Data.Value>,
    var category_id: String,var itemclick:FilterAdapter.FilterItemListner
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var lastSelectedPosition = -1


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {


        return DeveloperViewHolder(
            RowCategoryfilterBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )

    }

    override fun onBindViewHolder(mDeveloperViewHolder: RecyclerView.ViewHolder, i: Int) {

        val currentData = mListCart!![i]
        (mDeveloperViewHolder as ChildFilterAdapter.DeveloperViewHolder).onBind( i)


    }

    override fun getItemCount(): Int {
        return if (mListCart != null) {
            mListCart!!.size
        } else {
            0
        }
    }


    inner class DeveloperViewHolder(var mListResponse: RowCategoryfilterBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        fun onBind(position: Int) {

            mListResponse.model=mListCart.get(position)

            if(lastSelectedPosition==position){
                itemView.Frame.visibility= View.VISIBLE
                itemView.RadioButton.setChecked(true);
            }else {
                itemView.Frame.visibility= View.GONE
                itemView.RadioButton.setChecked(false);
            }

            itemView.setOnClickListener(){
                lastSelectedPosition = position
                run {
                    itemclick.onclick(category_id,mListCart.get(position))
                }
                notifyDataSetChanged();
            }


        }
    }

}