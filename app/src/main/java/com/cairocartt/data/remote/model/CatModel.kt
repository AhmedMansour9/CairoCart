package com.cairocartt.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
 data class CatModel(
   val id: Int,
   val image: String?,
   val isActive: Boolean,
   val level: Int,
   val name: String,
   val productCount: Int,
   val parentId: Int,
   var isExpanded: Boolean = false,
   val notChild:Boolean=false
) :Parcelable

