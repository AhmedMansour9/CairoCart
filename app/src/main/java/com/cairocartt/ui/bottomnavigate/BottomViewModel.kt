package com.cairocartt.ui.bottomnavigate

import androidx.hilt.lifecycle.ViewModelInject
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager

class BottomViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<BottomNavigator>(dataCenterManager) {


}