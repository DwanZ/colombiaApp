package com.dwan.feature.attractions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.FormatListNumberedRtl
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.dwan.domain.model.AttractionModel

@Composable
fun AttractionScreen(
    viewModel: AttractionViewModel = hiltViewModel(),
    goToAttractionDetail: (id: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLimitMenuVisible by remember { mutableStateOf(false) }
    var isPageMenuVisible by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SearchBar(
                modifier = Modifier.weight(1f),
                onSearch = { if (it.length >= 3) viewModel.search(it) }
            )
            Box {
                IconButton(onClick = { isLimitMenuVisible = true }) {
                    Icon(Icons.Filled.FormatListNumberedRtl, contentDescription = "Page size")
                }
                DropdownMenu(
                    expanded = isLimitMenuVisible,
                    onDismissRequest = { isLimitMenuVisible = false }
                ) {
                    listOf(10, 15, 20).forEach { size ->
                        DropdownMenuItem(
                            text = { Text("$size items") },
                            onClick = {
                                isLimitMenuVisible = false
                                viewModel.setPageSize(size)
                            }
                        )
                    }
                }
            }
            Box {
                IconButton(onClick = { isPageMenuVisible = true }) {
                    Icon(Icons.Filled.AutoStories, contentDescription = "Page")
                }
                DropdownMenu(
                    expanded = isPageMenuVisible,
                    onDismissRequest = { isPageMenuVisible = false }
                ) {
                    (1..uiState.pageCount).forEach { page ->
                        DropdownMenuItem(
                            text = { Text("Page $page") },
                            onClick = {
                                isPageMenuVisible = false
                                viewModel.setPage(page)
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        AttractionListInfo(
            viewState = uiState.content,
            onCardClicked = goToAttractionDetail,
            refresh = viewModel::refresh
        )
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
        is BaseViewState.Success -> LazyColumn {
            items(viewState.data, key = { it.id }) { attraction ->
                AttractionCard(attraction, onCardClicked)
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
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onClick(attraction.id) }
    ) {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            val image = attraction.images.firstOrNull()
            if (image != null) {
                AsyncImage(
                    modifier = Modifier.size(88.dp),
                    model = image,
                    contentDescription = attraction.name,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp).padding(12.dp)
                )
            }
            Column(Modifier.padding(horizontal = 12.dp)) {
                Text(attraction.name, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("Lat: ${attraction.latitude}", style = MaterialTheme.typography.bodySmall)
                Text("Lng: ${attraction.longitude}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
