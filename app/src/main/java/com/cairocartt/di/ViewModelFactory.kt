package com.cairocartt.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(private val creators: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]
        if (creator == null) {
            creators.forEach {
                if (modelClass.isAssignableFrom(it.key)) {
                    creator = it.value
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown model class" + modelClass.name)
        }
        try {
            return creator!!.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}