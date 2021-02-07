package com.cairocartt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.ListReviwesResponse
import com.cairocartt.databinding.ItemReviewsBinding

class ReviwesAdapter():RecyclerView.Adapter<ReviwesAdapter.DeveloperViewHolder>() {

    private var mTradersModel: MutableList<ListReviwesResponse.Data>? = ArrayList<ListReviwesResponse.Data>()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        val mTradersResponse = DataBindingUtil.inflate<ItemReviewsBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_reviews, viewGroup, false
        )

        return DeveloperViewHolder(mTradersResponse)
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
        mDeveloperViewHolder.onBind(i)




    }


    override fun getItemCount(): Int {
        return if (mTradersModel != null) {
            mTradersModel!!.size
        } else {
            0
        }
    }

    fun setDeveloperList(mDeveloperModel: MutableList<ListReviwesResponse.Data>) {
        this.mTradersModel = mDeveloperModel
        notifyDataSetChanged()
    }

    inner class DeveloperViewHolder(var mReviwesResponse: ItemReviewsBinding) :
        RecyclerView.ViewHolder(mReviwesResponse.root){
        var i:Int=0

        fun onBind(position: Int) {
            val currentReiew = mTradersModel!![position]

            mReviwesResponse.NickName.text=currentReiew.nickname

            fun setRate(title:String,rate:Int){
                if(title.equals("Quality")){
                    mReviwesResponse.RatingBarQuality.rating=rate.toFloat()
                }
                if(title.equals("Price")){
                    mReviwesResponse.RatingBarPrice.rating=rate.toFloat()
                }
                if(title.equals("Value")){
                    mReviwesResponse.RatingBarValue.rating=rate.toFloat()
                }
            }

            currentReiew.ratings?.forEachIndexed { index, element ->
                    setRate(currentReiew.ratings?.get(index)!!.ratingName,currentReiew.ratings?.get(index)!!.value)

            }

        }
    }


}