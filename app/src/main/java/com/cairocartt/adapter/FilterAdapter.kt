package com.cairocartt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.FilterResponse
import com.cairocartt.data.remote.model.MyOrdersResponse


class FilterAdapter(val context: Context, val data: List<FilterResponse.Data>,var itemclick: FilterItemListner) :
    BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return data.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any? {
        return data.get(groupPosition)
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return data.get(groupPosition).values.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView: View? = convertView
        val title: String? = data.get(groupPosition).label
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.item_headerfilter, null)
        }
        val img_arrow_up: ImageView? = convertView?.findViewById(R.id.bt_toggle_brand)
        val T_Title: TextView? = convertView?.findViewById(R.id.T_Title)
        T_Title?.text = title
        if(isExpanded){
            img_arrow_up?.animate()?.setDuration(200)?.rotation(180f)
        }else {
            img_arrow_up?.animate()?.setDuration(200)?.rotation(0f)
        }


        return convertView
    }


    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView: View? = convertView
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.exp_all_child, null)
        }
        val recycle: RecyclerView
        val adapter: ChildFilterAdapter
        recycle = convertView!!.findViewById(R.id.recycle)
        adapter = ChildFilterAdapter(context, data.get(groupPosition).values,data.get(groupPosition).field,itemclick)
        recycle.adapter = adapter
        adapter.notifyDataSetChanged()
        recycle.scheduleLayoutAnimation()
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    interface FilterItemListner {
        fun onclick(Field:String,filter :FilterResponse.Data.Value)
    }
}