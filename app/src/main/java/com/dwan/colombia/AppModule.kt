package com.dwan.colombia

import com.dwan.colombia.ui.screen.country.CountryViewModel
import com.dwan.colombia.ui.screen.president.PresidentViewModel
import com.dwan.domain.repository.CountryRepository
import com.dwan.domain.repository.PresidentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideCountryViewModel(repository: CountryRepository) =
        CountryViewModel(repository)

    @Provides
    fun providePresidentViewModel(repository: PresidentRepository) =
        PresidentViewModel(repository)
}