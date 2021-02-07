package com.cairocartt

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class ChangeLanguage {

    companion object {


        private fun updateResources(
            context: Context,
            language: String
        ): Context? {
            var context = context
            val locale = Locale(language)
            Locale.setDefault(locale)
            val res: Resources = context.resources
            val config = Configuration(res.getConfiguration())
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale)
                context = context.createConfigurationContext(config)
            } else {
                config.locale = locale
                res.updateConfiguration(config, res.getDisplayMetrics())
            }
            return context
        }

        fun changeLanguage(
            context: Context): Context? {
//            var shared:  SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
//            val Lan = shared.getString("Lann", null)
            val toLocale = Locale("ar")
            Locale.setDefault(toLocale)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateResourcesForO(context, toLocale)
            } else {
                updateResources(context, toLocale)
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun updateResourcesForO(
            context: Context,
            locale: Locale
        ): Context? {
            val configuration =
                context.resources.configuration
            configuration.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        private fun updateResources(
            context: Context,
            locale: Locale
        ): Context? {
            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }
        fun changeLang(context: Context) {
           var shared:  SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
            val Lan = shared.getString("Lann", null)
            if (Lan != null) {



//                val locale = Locale(Lan!!)
//                Locale.setDefault(locale)
//                val config = Configuration()
//                config.locale = locale
//                context.resources.updateConfiguration(
//                        config,
//                        context.resources.displayMetrics
//                )
            }

        }
        fun getLanguage(context: Context):String{

            var DeviceLang=""
            var shared:  SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
            val Lan = shared.getString("Lann", null)
            if(Lan!=null){
                DeviceLang = Lan
            }else {
                DeviceLang = Locale.getDefault().language

            }
            return DeviceLang
        }

//        fun getLanguage():String{
//            var DeviceLang=""
//            var shared:  SharedPreferences = context.getSharedPreferences("Language", AppCompatActivity.MODE_PRIVATE)
//            val Lan = shared.getString("Lann", null)
//            if(Lan!=null){
//                DeviceLang = Lan
//            }else {
//                DeviceLang = Locale.getDefault().language
//
//            }
//            return DeviceLang
//        }
    }
}