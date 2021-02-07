package com.cairocartt.ui.onboarding

import androidx.hilt.lifecycle.ViewModelInject
import com.cairocartt.base.BaseViewModel
import com.cairocartt.data.DataCenterManager

class OnBoardingViewModel @ViewModelInject constructor(dataCenterManager: DataCenterManager) :
    BaseViewModel<Any>(dataCenterManager) {


}