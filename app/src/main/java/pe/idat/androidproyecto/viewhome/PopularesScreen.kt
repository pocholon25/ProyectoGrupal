package pe.idat.androidproyecto.viewhome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import pe.idat.androidproyecto.components.BoxUse


@Composable
fun PopularesScreen(navController: NavController, modifier:Modifier=Modifier){
    BoxUse(text = "POPULARES", modifier = Modifier.fillMaxSize())
}