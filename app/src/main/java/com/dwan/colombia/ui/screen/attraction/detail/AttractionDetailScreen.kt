package com.dwan.colombia.ui.screen.attraction.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dwan.colombia.R
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.InformationScrollableBoxView
import com.dwan.domain.model.AttractionModel

@Composable
fun AttractionDetailScreen(
    navController: NavController,
    viewModel: AttractionDetailViewModel = hiltViewModel(),
    startTrip: (String) -> Unit
) {
    AttractionDetailInfo(
        viewModel.viewState.collectAsState().value,
        {
            navController.popBackStack()
        },
        { viewModel.refresh() },
        startTrip
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionDetailInfo(
    viewState: BaseViewState<AttractionModel>,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    startTrip: (String) -> Unit
) {
    when (viewState) {
        is BaseViewState.Loading -> ColombiaLoadingView()
        is BaseViewState.Failure -> {
            GenericErrorView(errorMessage = viewState.errorMessage, action = onRefresh)
        }

        is BaseViewState.Success -> {
            val attraction = viewState.data
            Scaffold() { padding ->
                Column(Modifier.padding(padding)) {
                    AttractionCard(attraction, onBack, startTrip)
                }
            }
        }
    }
}

@Composable
fun AttractionCard(
    attraction: AttractionModel,
    onBack: () -> Unit,
    startTrip: (String) -> Unit
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
    {

        Card(
            modifier = Modifier
                .width(200.dp)
                .height(390.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        )
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp), horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onBack, Modifier.padding(0.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close, modifier = Modifier.padding(0.dp),
                        contentDescription = "Close attraction card"
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier.padding(0.dp),
                    model = attraction.images[0],
                    contentDescription = "Attraction photo",
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(R.drawable.ic_nature),
                    error = painterResource(R.drawable.ic_nature)
                )
                Divider()
                ProfileInfo(attraction)
                Divider()
                InformationScrollableBoxView(
                    attraction.description,
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 20.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { startTrip(attraction.name) }) {
                    Text(text = "RECORD VISIT", color = Color.White)
                    Icon(
                        imageVector = Icons.Default.Send,
                        modifier = Modifier.padding(start = 10.dp),
                        tint = Color.White,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileInfo(attraction: AttractionModel) {
    Column(
        modifier = Modifier
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            color = Color.Blue,
            fontSize = 24.sp,
            style = MaterialTheme.typography.h4,
            text = attraction.name
        )

        Text(
            text = attraction.cityName,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(3.dp)
        )
        Row() {
            Text(
                text = "Latitude: ",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
            )
            Text(text = attraction.latitude, style = MaterialTheme.typography.subtitle1)
        }
        Row() {
            Text(
                text = "Longitude: ",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1,
            )
            Text(text = attraction.longitude, style = MaterialTheme.typography.subtitle1)
        }
    }
}