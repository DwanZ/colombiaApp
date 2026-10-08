package com.dwan.data

import com.dwan.data.source.remote.CountryEntity
import com.dwan.data.source.remote.toModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryEntityTest {
    @Test
    fun `toModel should convert CountryEntity to CountryModel correctly`() {
        val countryEntity = CountryEntity(
            aircraftPrefix = "ABC",
            borders = listOf("border1", "border2"),
            currency = "Currency",
            currencyCode = "C",
            currencySymbol = "$",
            description = "Description",
            flags = listOf("flag1", "flag2"),
            id = 1,
            internetDomain = "domain.com",
            isoCode = "ISO",
            languages = listOf("language1", "language2"),
            name = "Country",
            phonePrefix = "123",
            population = 1000000,
            radioPrefix = "456",
            region = "Region",
            stateCapital = "Capital",
            subRegion = "Subregion",
            surface = 100,
            timeZone = "Timezone"
        )

        // Act
        val countryModel = countryEntity.toModel()

        // Assert
        assertEquals("ABC", countryModel.aircraftPrefix)
        assertEquals(listOf("border1", "border2"), countryModel.borders)
        assertEquals("Currency", countryModel.currency)
        assertEquals("C", countryModel.currencyCode)
        assertEquals("$", countryModel.currencySymbol)
        assertEquals("Description", countryModel.description)
        assertEquals(listOf("flag1", "flag2"), countryModel.flags)
        assertEquals(1, countryModel.id)
        assertEquals("domain.com", countryModel.internetDomain)
        assertEquals("ISO", countryModel.isoCode)
        assertEquals(listOf("language1", "language2"), countryModel.languages)
        assertEquals("Country", countryModel.name)
        assertEquals("123", countryModel.phonePrefix)
        assertEquals(1000000, countryModel.population)
        assertEquals("456", countryModel.radioPrefix)
        assertEquals("Region", countryModel.region)
        assertEquals("Capital", countryModel.stateCapital)
        assertEquals("Subregion", countryModel.subRegion)
        assertEquals(100, countryModel.surface)
        assertEquals("Timezone", countryModel.timeZone)
    }
}
