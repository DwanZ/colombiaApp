package com.dwan.colombia.ui.screen.president

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.SearchBar
import com.dwan.domain.model.PresidentModel

@Composable
fun PresidentScreen(
    viewModel: PresidentViewModel = hiltViewModel(),
    goToPresidentDetail: (id: Int) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        SearchBar(onSearch = {
            if (it.length >= 3) {
                viewModel.search(it)
            }
        }
        )

        PresidentListInfo(
            viewState = viewModel.viewState.collectAsState().value,
            refresh = { viewModel.refresh() }
        ) { id ->
            goToPresidentDetail(id)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PresidentListInfo(
    viewState: BaseViewState<List<PresidentModel>>,
    refresh: () -> Unit,
    onCardClicked: (id: Int) -> Unit
) {
    val refreshing by remember { mutableStateOf(viewState is BaseViewState.Loading) }
    val state = rememberPullRefreshState(refreshing, refresh)
    val rotation = animateFloatAsState(state.progress * 120)
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        when (viewState) {
            is BaseViewState.Loading -> ColombiaLoadingView()

            is BaseViewState.Failure -> {
                GenericErrorView(errorMessage = viewState.errorMessage, action = refresh)
            }

            is BaseViewState.Success -> {
                LazyColumn {
                    items(viewState.data) { president ->
                        PresidentCard(president, onCardClicked)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresidentCard(president: PresidentModel, onClick: (id: Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            onClick = { onClick(president.id) }
        ) {
            Row(Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 5.dp),
                    model = president.image,
                    contentDescription = "President photo",
                    placeholder = rememberVectorPainter(image = Icons.Default.Person),
                    error = rememberVectorPainter(image = Icons.Default.Person)
                )
                Column(Modifier.padding(horizontal = 10.dp)) {
                    Text(
                        text = "${president.name} ${president.lastName}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = president.politicalParty)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = president.startPeriodDate.substring(0, 4))
                        Text(text = president.endPeriodDate.substring(0, 4))
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PresidentCardPreview() {
    val dummyModel = PresidentModel(
        id = 1,
        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/Jos%C3%A9_Mar%C3%ADa_Campo_Serrano_1.jpg/500px-Jos%C3%A9_Mar%C3%ADa_Campo_Serrano_1.jpg",
        name = "José María",
        lastName = "Campo Serrano",
        startPeriodDate = "1886-08-05",
        endPeriodDate = "1887-01-06",
        politicalParty = "Partido Nacional",
        description = "Estado Soberano del Magdalena y jefe civil y militar de Antioquia durante la revolución de1885.También ejerció como ministro durante los gobiernos de Francisco",
        cityId = 709,
        city = null
    )
    PresidentCard(dummyModel) {}
}