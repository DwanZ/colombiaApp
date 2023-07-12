package com.dwan.colombia.ui.screen.president.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dwan.common.BaseViewState
import com.dwan.common.ui.ColombiaLoadingView
import com.dwan.common.ui.GenericErrorView
import com.dwan.common.ui.InformationScrollableBoxView
import com.dwan.common.ui.LabeledBoxView
import com.dwan.domain.model.PresidentModel

@Composable
fun PresidentDetailScreen(
    navController: NavController,
    viewModel: PresidentDetailViewModel = hiltViewModel(),
) {
    PresidentDetailInfo(
        viewState = viewModel.viewState.collectAsState().value,
        onBack = { navController.popBackStack() },
        onRefresh = { viewModel.refresh() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresidentDetailInfo(
    viewState: BaseViewState<PresidentModel>,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    when (viewState) {
        is BaseViewState.Loading -> ColombiaLoadingView()

        is BaseViewState.Failure -> {
            GenericErrorView(errorMessage = viewState.errorMessage, action = onRefresh)
        }

        is BaseViewState.Success -> {
            val president = viewState.data
            Scaffold() { padding ->
                Column(Modifier.padding(padding)) {
                    ProfileCard(president = president, onBack)
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    president: PresidentModel,
    onBack: () -> Unit
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(image = president.image, onBack)
                Divider()
                ProfileInfo(president)
                Divider()
                InformationScrollableBoxView(
                    president.description,
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 20.dp)
                )
                LabeledBoxView(
                    label = "City:",
                    description = "${president.city?.name ?: "-"} ",
                    background = Color.Transparent
                )


            }
        }
    }
}

@Composable
private fun ProfileInfo(president: PresidentModel) {
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
            text = "${president.name} ${president.lastName}"
        )

        Text(
            text = president.politicalParty,
            modifier = Modifier.padding(3.dp)
        )

        Text(
            text = "${
                president.startPeriodDate.subSequence(
                    0,
                    4
                )
            } - ${president.endPeriodDate.subSequence(0, 4)}",
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
private fun ProfileImage(image: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(0.dp), horizontalArrangement = Arrangement.End) {
        IconButton(onClick = onBack,Modifier.padding(0.dp)) {
            Icon(imageVector =Icons.Default.Close , contentDescription = "Close president card")
        }
    }
    Surface(
        modifier = Modifier
            .size(154.dp)
            .padding(0.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        shadowElevation = 4.dp,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .padding(top = 5.dp),
            model = image,
            contentDescription = "President photo",
            placeholder = rememberVectorPainter(image = Icons.Default.Person),
            error = rememberVectorPainter(image = Icons.Default.Person)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PresidentProfilePreview() {
    val dummyP = PresidentModel(
        id = 1,
        image = "",
        name = "José María",
        lastName = "Campo Serrano",
        startPeriodDate = "1886-08-05",
        endPeriodDate = "1887-01-06",
        politicalParty = "Partido Nacional",
        description = "José María Campo Serrano (Santa Marta, 8 de septiembre de 1832-Santa Marta, 24 de febrero de 1915) fue un político, abogado y militar colombiano, miembro del extinto Partido Nacional Colombiano. Ocupó varios cargos de importancia, siendo congresista, gobernador en varias ocasiones del Estado Soberano del Magdalena y jefe civil y militar de Antioquia durante la revolución de 1885. También ejerció como ministro durante los gobiernos de Francisco Javier Zaldúa, Ezequiel Hurtado y Rafael Núñez. Concurrió por Antioquia al Consejo Nacional de Delegatarios. Campo fue elegido designado presidencial para el período 1886-1888, siendo nombrado presidente ante la ausencia del titular Rafael Núñez, de 1886 a enero de 1887. En su corto mandato, Campo fue el encargado de sancionar la nueva constitución de 1886, que estuvo vigente hasta 1991.",
        cityId = 709,
        city = null
    )

    ProfileCard(president = dummyP){}
}