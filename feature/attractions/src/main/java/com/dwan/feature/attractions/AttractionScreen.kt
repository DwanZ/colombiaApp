package com.dwan.feature.attractions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dwan.common.BaseViewState
import com.dwan.common.image.ImagePlaceholderKind
import com.dwan.common.image.RemoteImage
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.SearchBar
import com.dwan.domain.model.AttractionModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionScreen(
    viewModel: AttractionViewModel = hiltViewModel(),
    goToAttractionDetail: (id: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var pageSizeMenuVisible by remember { mutableStateOf(false) }
    val refreshing = uiState.content is BaseViewState.Loading

    Column(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            onSearch = { if (it.length >= 3) viewModel.search(it) }
        )
        Spacer(Modifier.height(8.dp))
        if (!uiState.isSearching) {
            PaginationBar(
                page = uiState.page,
                pageCount = uiState.pageCount,
                pageSize = uiState.pageSize,
                pageSizeMenuVisible = pageSizeMenuVisible,
                onPageSizeMenuChange = { pageSizeMenuVisible = it },
                onPrevious = viewModel::previousPage,
                onNext = viewModel::nextPage,
                onPageSizeSelected = viewModel::setPageSize
            )
            Spacer(Modifier.height(4.dp))
        }
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize()
        ) {
            AttractionListInfo(
                viewState = uiState.content,
                onCardClicked = goToAttractionDetail,
                refresh = viewModel::refresh
            )
        }
    }
}

@Composable
private fun PaginationBar(
    page: Int,
    pageCount: Int,
    pageSize: Int,
    pageSizeMenuVisible: Boolean,
    onPageSizeMenuChange: (Boolean) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onPageSizeSelected: (Int) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrevious, enabled = page > 1) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous page"
                )
            }
            Text(
                "Page $page of $pageCount",
                style = MaterialTheme.typography.labelLarge
            )
            IconButton(onClick = onNext, enabled = page < pageCount) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next page"
                )
            }
        }
        Box {
            TextButton(onClick = { onPageSizeMenuChange(true) }) {
                Text("$pageSize / page")
            }
            DropdownMenu(
                expanded = pageSizeMenuVisible,
                onDismissRequest = { onPageSizeMenuChange(false) }
            ) {
                listOf(10, 15, 20).forEach { size ->
                    DropdownMenuItem(
                        text = { Text("$size items") },
                        onClick = {
                            onPageSizeMenuChange(false)
                            onPageSizeSelected(size)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AttractionListInfo(
    viewState: BaseViewState<List<AttractionModel>>,
    onCardClicked: (id: Int) -> Unit,
    refresh: () -> Unit
) {
    when (viewState) {
        is BaseViewState.Loading -> ColombiaLoadingView()
        is BaseViewState.Failure -> GenericErrorView(
            errorMessage = viewState.errorMessage,
            action = refresh
        )
        is BaseViewState.Success -> {
            if (viewState.data.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No attractions found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn {
                    items(viewState.data, key = { it.id }) { attraction ->
                        AttractionCard(attraction, onCardClicked)
                    }
                }
            }
        }
    }
}

@Composable
fun AttractionCard(
    attraction: AttractionModel,
    onClick: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onClick(attraction.id) }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            RemoteImage(
                url = attraction.images.firstOrNull(),
                contentDescription = attraction.name,
                contentScale = ContentScale.Crop,
                placeholderKind = ImagePlaceholderKind.Attraction,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    attraction.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (attraction.cityName.isNotBlank()) {
                    Text(
                        attraction.cityName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                if (attraction.description.isNotBlank()) {
                    Text(
                        attraction.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }
    }
}
