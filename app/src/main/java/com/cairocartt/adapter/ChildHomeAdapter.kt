package com.cairocartt.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.HomeResponse
import com.cairocartt.data.remote.model.MessageEvent
import com.cairocartt.databinding.*
import kotlinx.android.synthetic.main.item_myorder.view.*
import org.greenrobot.eventbus.EventBus

class ChildHomeAdapter(
    var mListCart: MutableList<HomeResponse.Data.Card>,
    var status: String,
    var context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal val VIEW_TYPE_ONE = 4
    internal val VIEW_TYPE_TWO = 5

    companion object {
        val VIEW_TYPE_FIRST = 0
        val VIEW_TYPE_ITEM = 1

    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        if (i == VIEW_TYPE_ONE)
            return DeveloperViewHolder(
                ItemSmallcardsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        else if (i == VIEW_TYPE_TWO)
            return DeveloperViewHolder2(
                ItemHomesliderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        else
            if (i == 0) {
                return DeveloperViewHolder4(
                    ItemBigcardBindingImpl.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
                )
            } else
                return DeveloperViewHolder3(
                    ItemBigandsmallcardBinding.inflate(
                        LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false
                    )
                )

    }

    override fun onBindViewHolder(mDeveloperViewHolder: RecyclerView.ViewHolder, i: Int) {

        val currentData = mListCart!![i]

        if (mDeveloperViewHolder.itemViewType == VIEW_TYPE_ONE) {
            (mDeveloperViewHolder as ChildHomeAdapter.DeveloperViewHolder).onBind(currentData, i)
        } else if (mDeveloperViewHolder.itemViewType == VIEW_TYPE_TWO) {
            (mDeveloperViewHolder as ChildHomeAdapter.DeveloperViewHolder2).onBind(currentData, i)
        } else
            if (mDeveloperViewHolder.itemViewType == 0) {
                (mDeveloperViewHolder as ChildHomeAdapter.DeveloperViewHolder4).onBind(
                    currentData,
                    i
                )
            } else {
                (mDeveloperViewHolder as ChildHomeAdapter.DeveloperViewHolder3).onBind(
                    currentData,
                    i
                )
            }


    }

    override fun getItemCount(): Int {
        return if (mListCart != null) {
            mListCart!!.size
        } else {
            0
        }
    }


    inner class DeveloperViewHolder(var mListResponse: ItemSmallcardsBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        fun onBind(position: HomeResponse.Data.Card, i: Int) {
            mListResponse.model = position
            itemView.setOnClickListener(){
                checkRefrence(position)
            }
        }
        fun checkRefrence(position: HomeResponse.Data.Card) {
            if (position.referenceType.contains("ProductFactory")) {
                EventBus.getDefault().postSticky(MessageEvent("product",position.referenceId.toString()))

            } else if (position.referenceType.contains("BrandFactory")) {
                var bundle = Bundle()
                bundle.putString("brand_Id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_resultFiltertionFragment, bundle)

            } else if (position.referenceType.contains("CategoryFactory")) {
                var bundle = Bundle()
                bundle.putString("id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_productsById, bundle)

            }
        }
    }

    inner class DeveloperViewHolder2(var mListResponse: ItemHomesliderBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        fun onBind(position: HomeResponse.Data.Card, i: Int) {
            mListResponse.model = position
            itemView.setOnClickListener() {
                itemView.setOnClickListener(){
                    checkRefrence(position)
                }
            }

        }

        fun checkRefrence(position: HomeResponse.Data.Card) {
            if (position.referenceType.contains("ProductFactory")) {
//               var bundle=Bundle()
//
//               mListResponse.root.findNavController().navigate(R.id.action_homeFragment_to_productsById,bundle)

                EventBus.getDefault().postSticky(MessageEvent("product",position.referenceId.toString()))

            } else if (position.referenceType.contains("BrandFactory")) {
                var bundle = Bundle()
                bundle.putString("brand_Id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_resultFiltertionFragment, bundle)

            } else if (position.referenceType.contains("CategoryFactory")) {
                var bundle = Bundle()
                bundle.putString("id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_productsById, bundle)

            }
        }
    }

    inner class DeveloperViewHolder3(var mListResponse: ItemBigandsmallcardBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        fun onBind(position: HomeResponse.Data.Card, i: Int) {
            mListResponse.model = position
            itemView.setOnClickListener(){
                checkRefrence(position)
            }        }
        fun checkRefrence(position: HomeResponse.Data.Card) {
            if (position.referenceType.contains("ProductFactory")) {
//               var bundle=Bundle()
//
//               mListResponse.root.findNavController().navigate(R.id.action_homeFragment_to_productsById,bundle)
                EventBus.getDefault().postSticky(MessageEvent("product",position.referenceId.toString()))


            } else if (position.referenceType.contains("BrandFactory")) {
                var bundle = Bundle()
                bundle.putString("brand_Id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_resultFiltertionFragment, bundle)

            } else if (position.referenceType.contains("CategoryFactory")) {
                var bundle = Bundle()
                bundle.putString("id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_productsById, bundle)

            }
        }
    }

    inner class DeveloperViewHolder4(var mListResponse: ItemBigcardBinding) :
        RecyclerView.ViewHolder(mListResponse.root) {
        fun onBind(position: HomeResponse.Data.Card, i: Int) {
            mListResponse.model = position
            itemView.setOnClickListener(){
                checkRefrence(position)
            }
        }

        fun checkRefrence(position: HomeResponse.Data.Card) {
            if (position.referenceType.contains("ProductFactory")) {
//               var bundle=Bundle()
//
//               mListResponse.root.findNavController().navigate(R.id.action_homeFragment_to_productsById,bundle)
                EventBus.getDefault().postSticky(MessageEvent("product",position.referenceId.toString()))


            } else if (position.referenceType.contains("BrandFactory")) {
                var bundle = Bundle()
                bundle.putString("brand_Id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_resultFiltertionFragment, bundle)

            } else if (position.referenceType.contains("CategoryFactory")) {
                var bundle = Bundle()
                bundle.putString("id", position.referenceId.toString())
                mListResponse.root.findNavController()
                    .navigate(R.id.action_homeFragment_to_productsById, bundle)

            }
        }
    }


    override fun getItemViewType(position: Int): Int {

        // here you can get decide from your model's ArrayList, which type of view you need to load. Like
        return if (status.equals("small_cards")) { // put your condition, according to your requirements
            VIEW_TYPE_ONE
        } else if (status.equals("slider")) {
            VIEW_TYPE_TWO
        } else
            if (position == 0) {
                return VIEW_TYPE_FIRST;
            } else
                return VIEW_TYPE_ITEM
    }


}