package com.example.vamz

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vamz.ui.theme.VAMZTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VAMZTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "menu"
                ) {
                    composable("menu") {
                        MenuNahlad(navController)
                    }
                    composable("mapa") {
                        PrvyNahlad(navController)
                    }
                    composable("vyhladaj") {
                        VyhladajNahlad(navController)
                    }
                    composable(
                        "vysledokHladania/{arg1}",
                        arguments = listOf(
                            navArgument("arg1") { type = NavType.StringType }
                        )
                    ) { entry ->
                        VysledokHladania(navController = navController, entry.arguments?.getString("arg1"))
                    }
                    composable("zoznam_materialov") {
                        ZoznamMaterialovNahlad(navController)
                    }
                }
            }
        }
    }
}

val farbaPozadia = Color(144, 238, 144)

@Composable
fun HornaListaMain(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(BorderStroke(2.dp, Color.Black))
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Späť")
        }
        Text(
            text = "Chenyu Vale",
            color = Color.Black,
            fontSize = 25.sp,
        )
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = "Profil",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun StrednaCastMain() {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1117f / 925f)
    ) {
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
            )
        }

        Image(
            painter = painterResource(id = R.drawable.mapchenyun),
            contentDescription = "Mapa Chenyu Vale",
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state)
        )
    }
}

@Composable
fun PrvyNahlad(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(farbaPozadia)
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        HornaListaMain(navController)
        StrednaCastMain()
    }
}

@Composable
fun MenuNahlad(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(farbaPozadia)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = { navController.navigate("mapa") },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Filled.Place,
                    contentDescription = "Mapa",
                    modifier = Modifier.size(64.dp)
                )
            }
            Text(text = "Mapa")
            Spacer(modifier = Modifier.size(16.dp))

            IconButton(
                onClick = { navController.navigate("vyhladaj") },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Vyhľadať",
                    modifier = Modifier.size(64.dp)
                )
            }
            Text(text = "Vyhľadaj")
            Spacer(modifier = Modifier.size(16.dp))

            IconButton(
                onClick = { navController.navigate("zoznam_materialov") },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = "Zoznam materiálov",
                    modifier = Modifier.size(64.dp)
                )
            }
            Text(text = "Zoznam materiálov")
        }

        Text(
            "ORECH",
            fontSize = 78.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
        )
    }
}

@Composable
fun VyhladajNahlad(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(application)
    )
    val searchText by viewModel.searchText.collectAsState()
    val materialy by viewModel.materialy.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(farbaPozadia)
            .padding(16.dp)
    ) {
        HornaListaMain(navController)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Vyhľadávanie"
                )
            },
            value = searchText,
            label = { Text("Vyhľadaj") },
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            var predosly = ""

            items(materialy) { material ->
                if (material.nazovMaterialu != predosly) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate("vysledokHladania/${material.nazovMaterialu}")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = material.nazovMaterialu,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Face,
                                contentDescription = "Ikona materiálu"
                            )
                        }
                    }
                    predosly = material.nazovMaterialu
                }
            }
        }
    }
}

@Composable
fun VysledokHladania(navController: NavController, material: String?) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(application)
    )
    val materialy by viewModel.materialy.collectAsState()
    val checkedMaterials by viewModel.checkedMaterials.collectAsState()

    val filteredMaterials = materialy.filter { it.nazovMaterialu == material }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(farbaPozadia)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Späť"
                )
            }
            Text(
                text = material ?: "Neznámy materiál",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(48.dp))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(filteredMaterials) { _material ->
                val isChecked = checkedMaterials[Pair(_material.nazovMaterialu, _material.lokacia)] ?: false

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    viewModel.toggleMaterialChecked(_material, checked)
                                }
                            )

                            Text(
                                text = "Lokácia: ${_material.lokacia}",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        Image(
                            painter = painterResource(id = _material.imageRes),
                            contentDescription = _material.nazovMaterialu,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.5f)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        if (filteredMaterials.isNotEmpty()) {
            Button(
                onClick = {
                    viewModel.saveMaterialSelections()
                    Toast.makeText(
                        context,
                        "Označené materiály boli uložené",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0, 128, 0)
                )
            ) {
                Text(
                    "Uložiť označené materiály",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ZoznamMaterialovNahlad(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(application)
    )
    val trackedMaterials by viewModel.trackedMaterials.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearExpiredMaterials()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(farbaPozadia)
            .padding(16.dp)
    ) {
        HornaListaMain(navController)

        Text(
            text = "Zoznam sledovaných materiálov",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (trackedMaterials.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Žiadne sledované materiály",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(trackedMaterials) { material ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (material.isAvailable()) Color.White else Color(0xFFFFEEEE)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = material.nazovMaterialu,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Lokácia: ${material.lokacia}",
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                text = if (material.isAvailable())
                                    "Stav: Dostupné"
                                else
                                    "Respawn za: ${material.getFormattedRespawnTime()}",
                                color = if (material.isAvailable()) Color(0, 128, 0) else Color.Red,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                text = "Dostupné od: ${material.getFormattedDate()}",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Image(
                                painter = painterResource(id = material.imageRes),
                                contentDescription = material.nazovMaterialu,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.5f)
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}