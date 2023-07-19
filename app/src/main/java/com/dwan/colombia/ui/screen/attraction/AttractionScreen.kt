package com.dwan.colombia.ui.screen.attraction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.FormatListNumberedRtl
import androidx.compose.material.icons.filled.KeyboardTab
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dwan.colombia.R
import com.dwan.colombia.ui.screen.president.PresidentCard
import com.dwan.colombia.ui.screen.president.PresidentListInfo
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.SearchBar
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.PresidentModel

@Composable
fun AttractionScreen(
    viewModel: AttractionViewModel = hiltViewModel(),
    goToAttractionDetail: (id: Int) -> Unit
) {
    var isLimitMenuVisible by remember { mutableStateOf(false) }
    var isPageMenuVisible by remember { mutableStateOf(false) }
    Surface(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(390.dp)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            //colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SearchBar(
                        onSearch = {
                            if (it.length >= 3) {
                                viewModel.search(it)
                            }
                        }
                    )

                    IconButton(onClick = { isLimitMenuVisible = !isLimitMenuVisible }) {
                        Icon(
                            imageVector = Icons.Filled.FormatListNumberedRtl,
                            contentDescription = ""
                        )
                    }
                    IconButton(onClick = { isPageMenuVisible = !isPageMenuVisible }) {
                        Icon(imageVector = Icons.Filled.AutoStories, contentDescription = "")
                    }

                    Box(Modifier) {
                        DropdownMenu(
                            expanded = isPageMenuVisible,
                            onDismissRequest = { isPageMenuVisible = false },
                        ) {
                            (1..viewModel.pageCount).forEach { dropDownItem ->
                                DropdownMenuItem(
                                    onClick = { isPageMenuVisible = false }
                                ) {
                                    Text(
                                        modifier = Modifier,
                                        text = "Page $dropDownItem"
                                    )
                                }
                            }
                        }
                    }

                    Box(Modifier) {
                        DropdownMenu(
                            expanded = isLimitMenuVisible,
                            onDismissRequest = { isLimitMenuVisible = false },
                        ) {
                            listOf(10, 15, 20).forEach { dropDownItem ->
                                DropdownMenuItem(
                                    onClick = { isLimitMenuVisible = false }
                                ) {
                                    Text(
                                        modifier = Modifier,
                                        text = "$dropDownItem Items"
                                    )
                                }
                            }
                        }
                    }

                }


                AttractionListInfo(
                    viewState = viewModel.viewState.collectAsState().value,
                    onCardClicked = { id ->
                        goToAttractionDetail(id)
                    },
                    { viewModel.refresh() }
                )
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

        is BaseViewState.Failure -> {
            GenericErrorView(errorMessage = viewState.errorMessage, action = refresh)
        }

        is BaseViewState.Success -> {
            LazyColumn {
                items(viewState.data) { attraction ->
                    AttractionCard(attraction, onCardClicked)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionCard(
    attraction: AttractionModel,
    onClick: (id: Int) -> Unit
) {
    Row(
        Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            onClick = { onClick(attraction.id) }
        ) {
            Row(Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(top = 5.dp),
                    model = attraction.images[0],
                    contentDescription = "attraction photo",
                    placeholder = painterResource(R.drawable.ic_nature),
                    error = painterResource(R.drawable.ic_nature)
                )
                Column(Modifier.padding(horizontal = 10.dp)) {
                    Text(
                        text = "${attraction.name} ",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row() {
                        Text(text = "Latitude: ", fontWeight = FontWeight.Bold)
                        Text(text = attraction.latitude)
                    }
                    Row() {
                        Text(text = "Longitude: ", fontWeight = FontWeight.Bold)
                        Text(text = attraction.longitude)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AttractionPreview() {
    val dummyModel = AttractionModel(
        id = 1,
        images = listOf("https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/Jos%C3%A9_Mar%C3%ADa_Campo_Serrano_1.jpg/500px-Jos%C3%A9_Mar%C3%ADa_Campo_Serrano_1.jpg"),
        name = "José María",
        latitude = "-18.4095",
        longitude = "18.56789",
        description = "Estado Soberano del Magdalena y jefe civil y militar de Antioquia durante la revolución de1885.También ejerció como ministro durante los gobiernos de Francisco",
        cityId = 709
    )
    AttractionCard(dummyModel) {}

}


