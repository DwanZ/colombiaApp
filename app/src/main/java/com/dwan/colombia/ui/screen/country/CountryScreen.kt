package com.dwan.colombia.ui.screen.country

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.InformationScrollableBoxView
import com.dwan.common.ui.LabeledBoxView
import com.dwan.domain.model.CountryModel

@Composable
fun CountryScreen(viewModel: CountryViewModel = hiltViewModel()) {
    CountryInfo(viewModel.countryState.collectAsState().value) {
        viewModel.refresh()
    }
}

@Composable
fun CountryInfo(
    viewState: BaseViewState<CountryModel>,
    refresh: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when (viewState) {
            is BaseViewState.Loading -> ColombiaLoadingView()
            is BaseViewState.Failure -> {
                GenericErrorView(errorMessage = viewState.errorMessage, action = refresh)
            }

            is BaseViewState.Success -> {
                CountryContent(viewState.data)
            }
        }
    }
}

@Composable
fun CountryContent(country: CountryModel) {
    Surface(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
    )
    {
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(390.dp)
                .padding(8.dp),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            //colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        )
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(90.dp), horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = country.flags[1],
                    contentDescription = "flag",
                )
            }
            //Text(modifier = Modifier.padding(bottom = 15.dp), text = country.name, fontSize = 25.sp)
            LabeledBoxView("Capital:", country.stateCapital)
            LabeledBoxView("Languages:", country.languages.joinToString(", "))
            LabeledBoxView("Population:", country.population.toString())
            LabeledBoxView(
                "Currency:",
                "${country.currency} (${country.currencySymbol}, ${country.currencyCode})"
            )
            LabeledBoxView("Country Code:", country.phonePrefix)
            LabeledBoxView("Timezone:", country.timeZone)
            LabeledBoxView("Borders:", country.borders.joinToString(", "))
            InformationScrollableBoxView(text = country.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryInfoPreview() {
    val dummyModel = CountryModel(
        aircraftPrefix = "ABC",
        borders = listOf("Border1", "Border2", "Border3"),
        currency = "USD",
        currencyCode = "USD",
        currencySymbol = "$",
        description = "Country description",
        flags = listOf("https://flagcdn.com/co.svg", "Flag2"),
        id = 1,
        internetDomain = "example.com",
        isoCode = "US",
        languages = listOf("English", "Spanish"),
        name = "United States",
        phonePrefix = "+1",
        population = 328_200_000,
        radioPrefix = "Prefix",
        region = "Americas",
        stateCapital = "Washington, D.C.",
        subRegion = "North America",
        surface = 9_826_675,
        timeZone = "GMT-4"
    )
    CountryInfo(
        viewState = BaseViewState.Success(dummyModel),
        {}
    )
}