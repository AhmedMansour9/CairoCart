package com.cairocartt.ui.login

import com.cairocartt.base.BaseNavigator

interface LoginNavigator : BaseNavigator {

    fun loginClick()

    fun createAccoutClick()

    fun forgetPasswordClick();

    fun loginGoogleClick();

    fun loginFacebookClick()

}