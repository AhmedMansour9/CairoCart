package com.cairocartt.base

import androidx.lifecycle.ViewModel
import com.cairocartt.data.DataCenterManager


abstract class BaseViewModel<BaseNavigator> constructor(protected val dataCenterManager: DataCenterManager) :
    ViewModel() {

    var navigator: BaseNavigator? = null

}


interface BaseNavigator