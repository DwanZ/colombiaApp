package com.dwan.feature.country

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dwan.common.BaseViewState
import com.dwan.common.image.ImagePlaceholderKind
import com.dwan.common.image.RemoteImage
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.DescriptionText
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.LabeledBoxView
import com.dwan.common.ui.ScrollableTextSection
import com.dwan.domain.model.CountryModel

@Composable
fun CountryScreen(viewModel: CountryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CountryInfo(uiState, viewModel::refresh)
}

@Composable
fun CountryInfo(
    viewState: BaseViewState<CountryModel>,
    refresh: () -> Unit
) {
    when (viewState) {
        is BaseViewState.Loading -> ColombiaLoadingView()
        is BaseViewState.Failure -> GenericErrorView(
            errorMessage = viewState.errorMessage,
            action = refresh
        )
        is BaseViewState.Success -> CountryContent(viewState.data)
    }
}

@Composable
fun CountryContent(country: CountryModel) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = country.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            val flagUrl = country.flags.getOrNull(1) ?: country.flags.firstOrNull()
            RemoteImage(
                url = flagUrl,
                contentDescription = "${country.name} flag",
                contentScale = ContentScale.Fit,
                placeholderKind = ImagePlaceholderKind.Generic,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(16.dp))
            LabeledBoxView("Capital", country.stateCapital)
            LabeledBoxView("Languages", country.languages.joinToString(", "))
            LabeledBoxView("Population", "%,d".format(country.population))
            LabeledBoxView(
                "Currency",
                "${country.currency} (${country.currencySymbol}, ${country.currencyCode})"
            )
            LabeledBoxView("Phone code", country.phonePrefix)
            LabeledBoxView("Timezone", country.timeZone)
            LabeledBoxView("Borders", country.borders.joinToString(", "))
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (country.description.length > 600) {
                ScrollableTextSection(text = country.description)
            } else {
                DescriptionText(text = country.description)
            }
        }
    }
}
