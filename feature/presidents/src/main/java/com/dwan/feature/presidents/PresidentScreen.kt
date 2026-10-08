package com.dwan.feature.presidents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.SearchBar
import com.dwan.domain.model.PresidentModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresidentScreen(
    viewModel: PresidentViewModel = hiltViewModel(),
    goToPresidentDetail: (id: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val refreshing = uiState is BaseViewState.Loading

    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        SearchBar(
            Modifier.fillMaxWidth(),
            onSearch = { if (it.length >= 3) viewModel.search(it) }
        )
        Spacer(Modifier.height(8.dp))
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize()
        ) {
            when (val state = uiState) {
                is BaseViewState.Loading -> ColombiaLoadingView()
                is BaseViewState.Failure -> GenericErrorView(
                    errorMessage = state.errorMessage,
                    action = viewModel::refresh
                )
                is BaseViewState.Success -> LazyColumn {
                    items(state.data, key = { it.id }) { president ->
                        PresidentCard(president, goToPresidentDetail)
                    }
                }
            }
        }
    }
}

@Composable
fun PresidentCard(president: PresidentModel, onClick: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onClick(president.id) }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = president.image,
                contentDescription = "${president.name} ${president.lastName}",
                contentScale = ContentScale.Crop,
                placeholder = rememberVectorPainter(Icons.Default.Person),
                error = rememberVectorPainter(Icons.Default.Person)
            )
            Column(Modifier.padding(horizontal = 12.dp).weight(1f)) {
                Text(
                    "${president.name} ${president.lastName}",
                    fontWeight = FontWeight.Bold
                )
                Text(president.politicalParty, style = MaterialTheme.typography.bodyMedium)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(president.startPeriodDate.take(4))
                    Text(president.endPeriodDate.take(4))
                }
            }
        }
    }
}
