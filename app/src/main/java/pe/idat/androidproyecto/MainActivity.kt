package pe.idat.androidproyecto

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pe.idat.androidproyecto.categorias.AbarrotesScreen
import pe.idat.androidproyecto.categorias.BebidasScreen
import pe.idat.androidproyecto.categorias.EmbutidosScreen
import pe.idat.androidproyecto.categorias.LacteosScreen
import pe.idat.androidproyecto.categorias.LicoresScreen
import pe.idat.androidproyecto.categorias.LimpiezaScreen
import pe.idat.androidproyecto.categorias.SeemoreScreen
import pe.idat.androidproyecto.categorias.VegetalesScreen
import pe.idat.androidproyecto.components.AppTopBar
import pe.idat.androidproyecto.components.DrawerContent
import pe.idat.androidproyecto.route.Rutas
import pe.idat.androidproyecto.ui.theme.AndroidProyectoTheme
import pe.idat.androidproyecto.viewhome.CarritoScreen
import pe.idat.androidproyecto.viewhome.FavoriteScreen
import pe.idat.androidproyecto.viewhome.HomeScreen
import pe.idat.androidproyecto.viewhome.PopularesScreen
import pe.idat.androidproyecto.viewhome.ProfileScreen
import pe.idat.androidproyecto.viewhome.PromoScreen
import pe.idat.androidproyecto.viewlogin.LoginScreen
import pe.idat.androidproyecto.viewlogin.RecuperarScreen
import pe.idat.androidproyecto.viewlogin.RegistrarScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidProyectoTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                // Track current destination
                var currentDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(navController) {
                    navController.addOnDestinationChangedListener { _, destination, _ ->
                        currentDestination = destination.route
                        Log.d("NavigationDebug", "Updated Destination: $currentDestination")
                    }
                }
                val context = LocalContext.current
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(navController = navController, drawerState = drawerState)
                    }
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            Log.d("NavigationDebug", "Top Bar Check - Current Destination: $currentDestination")
                            if (currentDestination !in listOf(
                                    Rutas.Login.ruta,
                                    Rutas.Registrar.ruta,
                                    Rutas.Recuperar.ruta
                                ) && currentDestination != null
                            ) {
                                AppTopBar(
                                    onMenuClick = {
                                        coroutineScope.launch { drawerState.open() }
                                    },
                                    onAction1Click = {
                                        if (context is Activity) {
                                            context.finishAffinity()
                                        }
                                    },
                                    onAction2Click = {
                                        navController.navigate(Rutas.Carrito.ruta)
                                    }
                                )
                            }
                        },
                        content = { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = Rutas.Login.ruta,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                composable(Rutas.Login.ruta) { LoginScreen(navController, AuthViewModel()) }
                                composable(Rutas.Registrar.ruta) { RegistrarScreen(navController) }
                                composable(Rutas.Recuperar.ruta) { RecuperarScreen(navController) }
                                composable(Rutas.Home.ruta) { HomeScreen(navController) }
                                composable(Rutas.Profile.ruta) { ProfileScreen(navController) }
                                composable(Rutas.Promo.ruta) { PromoScreen(navController) }
                                composable(Rutas.Favorite.ruta) { FavoriteScreen(navController) }
                                composable(Rutas.Populares.ruta) { PopularesScreen(navController) }
                                composable(Rutas.Abarrotes.ruta) { AbarrotesScreen(navController) }
                                composable(Rutas.Bebidas.ruta) { BebidasScreen(navController) }
                                composable(Rutas.Embutidos.ruta) { EmbutidosScreen(navController) }
                                composable(Rutas.Lacteos.ruta) { LacteosScreen(navController) }
                                composable(Rutas.Licores.ruta) { LicoresScreen(navController) }
                                composable(Rutas.Limpieza.ruta) { LimpiezaScreen(navController) }
                                composable(Rutas.Seemore.ruta) { SeemoreScreen(navController) }
                                composable(Rutas.Vegetales.ruta) { VegetalesScreen(navController) }
                                composable(Rutas.Carrito.ruta) { CarritoScreen(navController) }
                            }
                        },
                        floatingActionButton = {if (currentDestination !in listOf(
                                Rutas.Login.ruta,
                                Rutas.Registrar.ruta,
                                Rutas.Recuperar.ruta
                            ) && currentDestination != null
                        ){
                        FloatingActionButtonHome(navController = navController)}
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun FloatingActionButtonHome(navController: NavController) {
    FloatingActionButton(
        onClick = {
            navController.navigate(Rutas.Home.ruta) {
                popUpTo(Rutas.Home.ruta) { inclusive = true }
            }
        },
        containerColor = Color.Transparent,
        contentColor = Color.Unspecified,
        content = {
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.doble),
                    contentDescription = "Go to Home",
                    tint = Color.Unspecified
                )
            }
        }
    )
}
