package com.dwan.feature.presidents.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.dwan.domain.model.PresidentModel

@Composable
fun PresidentDetailScreen(
    onBack: () -> Unit,
    viewModel: PresidentDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState) {
        is BaseViewState.Loading -> ColombiaLoadingView()
        is BaseViewState.Failure -> GenericErrorView(
            errorMessage = state.errorMessage,
            action = viewModel::refresh
        )
        is BaseViewState.Success -> PresidentDetailContent(state.data, onBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresidentDetailContent(president: PresidentModel, onBack: () -> Unit) {
    val fullName = "${president.name} ${president.lastName}"
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fullName, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RemoteImage(
                url = president.image,
                contentDescription = fullName,
                contentScale = ContentScale.Crop,
                placeholderKind = ImagePlaceholderKind.President,
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                president.politicalParty,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            LabeledBoxView("Start", president.startPeriodDate.take(10))
            LabeledBoxView(
                "End",
                if (president.endPeriodDate.equals("Vigente", ignoreCase = true)) {
                    "Present"
                } else {
                    president.endPeriodDate.take(10)
                }
            )
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text(
                "Biography",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            if (president.description.length > 600) {
                ScrollableTextSection(text = president.description)
            } else {
                DescriptionText(text = president.description)
            }
        }
    }
}
