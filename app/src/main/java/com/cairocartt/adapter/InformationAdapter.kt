package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ProductsResponse
import com.cairocartt.databinding.RowInformationBinding

class InformationAdapter (list :List<ProductsResponse.Data.ProductCustomAttribute> ) :  RecyclerView.Adapter<InformationAdapter.DeveloperViewHolder>(), View.OnClickListener {

    var listInformation: List<ProductsResponse.Data.ProductCustomAttribute>
      init {
          listInformation=list
      }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<RowInformationBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.row_information, viewGroup, false
        )

        return DeveloperViewHolder(mTradersResponse)
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
        val currentStudent = listInformation[i]
        mDeveloperViewHolder.mTradersResponse.model = currentStudent




    }

    override fun getItemCount(): Int {
        return listInformation.size
    }



    inner class DeveloperViewHolder(var mTradersResponse: RowInformationBinding) :
        RecyclerView.ViewHolder(mTradersResponse.root)

    override fun onClick(view: View?) {


    }

}