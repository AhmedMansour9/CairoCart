package com.cairocartt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cairocartt.R
import com.cairocartt.data.remote.model.FilterResponse


class FilterAdapter(val data: List<FilterResponse.Data>,val context: Context): BaseExpandableListAdapter() {

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
//        val img_arrow_up: ImageView = convertView.findViewById(R.id.img_arrow_up)
//        if (isExpanded) {
//            img_arrow_up.setImageResource(R.drawable.ic_arrow_left)
//        } else {
//            img_arrow_up.setImageResource(R.drawable.ic_arrow_down)
//        }


//----------------------------------------------------------- custom font-----------------------------------------------------

//        val txt_reception: TextView = convertView.findViewById(R.id.txt_reception)
//        txt_bedroom.setText(header_titles.get(groupPosition).Get_Fld6().toString() + " غرفه")
//        txt_wc.setText(header_titles.get(groupPosition).Get_Fld7().toString() + " حمام")
//        txt_kitchen.setText(header_titles.get(groupPosition).Get_Fld9().toString() + " مطبخ")
//        txt_reception.setText(header_titles.get(groupPosition).Get_Fld8().toString() + " ريسيبشن")
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
            convertView = layoutInflater.inflate(R.layout.item_headerfilter, null)
        }
        val recycle: RecyclerView
//        val adapter: recycle_all_exp_model_build
//        recycle = convertView.findViewById(R.id.recycle)
//        adapter = recycle_all_exp_model_build(ctx, header_titles.get(groupPosition).Get_Fld3())
//        recycle.adapter = adapter
//        adapter.notifyDataSetChanged()
//        recycle.scheduleLayoutAnimation()
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}