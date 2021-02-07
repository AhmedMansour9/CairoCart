package com.cairocartt.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class ViewModelModule {

    @Provides
    @Singleton
    fun provideCategoryViewModelProvider(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory =
        viewModelFactory


}