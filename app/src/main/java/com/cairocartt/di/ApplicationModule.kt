package com.cairocartt.di

import com.cairocartt.data.DataCenterImpl
import com.cairocartt.data.DataCenterManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideDataCenterManger(dataCenterImpl: DataCenterImpl): DataCenterManager = dataCenterImpl




}