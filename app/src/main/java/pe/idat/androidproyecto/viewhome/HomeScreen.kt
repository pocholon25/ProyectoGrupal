package pe.idat.androidproyecto.viewhome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.idat.androidproyecto.R
import pe.idat.androidproyecto.model.Producto
import pe.idat.androidproyecto.model.items
import pe.idat.androidproyecto.model.productos
import pe.idat.androidproyecto.model.promosList
import pe.idat.androidproyecto.route.Rutas

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        ) {
            Column {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)  // Espaciado entre tarjetas
                ) {
                    items(promosList) { product ->
                        ProductCardRow(imageRes = product.imagenRes)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "Categorias",
                            fontSize = 16.sp,
                            color = Color.Black,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    TextButton(onClick = { navController.navigate(Rutas.Populares.ruta) }) {
                        Text(
                            text = "+Vendidos",
                            fontSize = 16.sp,
                            color = Color.Blue,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
        IconGrid(navController = navController)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(border = BorderStroke(2.dp, color = Color(0xFF68ECE8)))
        ) {
            ProductoList(productos = productos, modifier = Modifier.padding())
        }
    }
}

@Composable
fun IconGrid(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent)
            .padding(horizontal = 16.dp)
            .border(border = BorderStroke(2.dp, color = Color(0xFF68ECE8))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Ajuste aquí
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in items.indices step 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (j in 0 until 4) {
                        if (i + j < items.size) {
                            IconItem(navController = navController, item = items[i + j])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconItem(navController: NavController, item: Pair<String, Int>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFF00EEE6))
                .clickable {
                    when (item.first) {
                        "Vegetales" -> navController.navigate(Rutas.Vegetales.ruta)
                        "Embutidos" -> navController.navigate(Rutas.Embutidos.ruta)
                        "Bebidas" -> navController.navigate(Rutas.Bebidas.ruta)
                        "Lacteos" -> navController.navigate(Rutas.Lacteos.ruta)
                        "Limpieza" -> navController.navigate(Rutas.Limpieza.ruta)
                        "Licores" -> navController.navigate(Rutas.Licores.ruta)
                        "Abarrotes" -> navController.navigate(Rutas.Abarrotes.ruta)
                        "Ver más" -> navController.navigate(Rutas.Seemore.ruta)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = item.second),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(text = item.first, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun ProductCardRow(imageRes: Int) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))  // Agregar borde
            .padding(4.dp)  // Padding para separar borde del contenido
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFEFEFEF))  // Color de fondo para la tarjeta
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ProductoList(productos: List<Producto>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(2.dp),
        modifier = modifier
    ) {
        items(productos) { producto ->
            ProductoItem(producto = producto)
        }
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(
                width = 2.dp,
                color = Color(0xFF92BBBA),
                shape = RectangleShape
            ),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = producto.imageRes),
                contentDescription = producto.title,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = (Modifier.height(8.dp)))
            Text(text = producto.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = (Modifier.height(4.dp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = producto.price,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                )
                Row(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.car),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .border(2.dp, Color.Blue, shape = CircleShape)
                            .padding(3.dp)
                    )
                }
            }
        }
    }
}
