package pe.idat.androidproyecto.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.idat.androidproyecto.components.ItemCard
import pe.idat.androidproyecto.components.LazyGrid
import pe.idat.androidproyecto.model.promo

@Composable
fun LicoresScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 8.dp), // Padding tenue para separar de los lados de la pantalla
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "LICORES",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // Padding vertical para el texto
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y la lista
        LazyGrid(
            items = promo,
            columns = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp) // Padding tenue para separar de los lados de la pantalla
        ) { promo ->
            ItemCard(
                item = promo,
                imageRes = promo.imageRes,
                title = promo.title,
                price = promo.price,
                iconContentDescription = "Promoción",
                onIconClick = { /* Acción al hacer clic en el icono */ }
            )
        }
    }
}
