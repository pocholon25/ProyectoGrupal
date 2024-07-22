package pe.idat.androidproyecto.viewlogin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.idat.androidproyecto.AuthViewModel
import pe.idat.androidproyecto.R
import pe.idat.androidproyecto.route.Rutas

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current

    var usuario by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isValidUsuario by rememberSaveable { mutableStateOf(false) }
    var isValidPassword by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Observa LiveData del ViewModel
    val usuarioLiveData by authViewModel.usuario.observeAsState("")
    val passwordLiveData by authViewModel.password.observeAsState("")

    // Actualiza las variables cuando LiveData cambie
    LaunchedEffect(usuarioLiveData) { usuario = usuarioLiveData }
    LaunchedEffect(passwordLiveData) { password = passwordLiveData }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFb5e48c))
    ) {
        Column(
            Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Card(
                Modifier.padding(12.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    RowImage()
                    RowUsuario(usuario = usuario, usuarioChange = { newUsuario ->
                        usuario = newUsuario
                        authViewModel.onLoginTextChanged(usuario, password)
                        isValidUsuario = newUsuario.length >= 5
                    }, isValid = isValidUsuario, label = "Usuario", keyboardType = KeyboardType.Text)
                    RowPassword(contrasena = password, passwordChange = { newPassword ->
                        password = newPassword
                        authViewModel.onLoginTextChanged(usuario, password)
                        isValidPassword = newPassword.length >= 5
                    }, passwordVisible = passwordVisible, passwordVisibleChange = { passwordVisible = !passwordVisible }, isValidPassword = isValidPassword)
                    RowButtonLogin(
                        context = context,
                        navController,
                        authViewModel,
                        isValidEmail = isValidUsuario,
                        isValidPassword = isValidPassword
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        TextButton(
                            onClick = { navController.navigate(Rutas.Recuperar.ruta) },
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth(),
                            content = {
                                Text(text = "Olvidaste tu contraseña?")
                            }
                        )
                        TextButton(
                            onClick = { navController.navigate(Rutas.Registrar.ruta) },
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                Text(text = "Registrate aquí")
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun RowButtonLogin(
    context: Context,
    navController: NavController,
    authViewModel: AuthViewModel,
    isValidEmail: Boolean,
    isValidPassword: Boolean
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                                                             if (authViewModel.autenticar()){
                                                                 navController.navigate(Rutas.Home.ruta){
                                                                     popUpTo(Rutas.Login.ruta){inclusive=true}
                                                                 }
                                                             }else{
                                                                 Toast.makeText(context, "Usuario y / o Contraseña incorrectos",Toast.LENGTH_LONG).show()
                                                             }
        },
            enabled = isValidPassword && isValidEmail) {
            Text(text = "Iniciar Sesion")
        }
    }
}

@Composable
fun RowPassword(
    contrasena: String,
    passwordChange: (String) -> Unit,
    passwordVisible: Boolean,
    passwordVisibleChange: () -> Unit,
    isValidPassword: Boolean
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = if (isValidPassword) Color.Green else Color.Red,
            unfocusedLabelColor = Color(0xFF5E72AF),
            focusedBorderColor = if (isValidPassword) Color.Green else Color.Red,
            unfocusedBorderColor = Color(0xFF5E72AF),
            errorBorderColor = Color.Red
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = passwordChange,
            maxLines = 1,
            singleLine = true,
            label = { Text(text = "Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = passwordVisibleChange) {
                    Icon(imageVector = image, contentDescription = "Ver contraseña")
                }
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            colors = colors
        )
    }
}

@Composable
fun RowUsuario(usuario: String, usuarioChange: (String) -> Unit, isValid: Boolean,label: String,keyboardType: KeyboardType ) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.Center
    ) {
        val colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = if (isValid) Color.Green else Color.Red,
            unfocusedLabelColor = Color(0xFF5E72AF),
            focusedBorderColor = if (isValid) Color.Green else Color.Red,
            unfocusedBorderColor = Color(0xFF5E72AF),
            errorBorderColor = Color.Red
        )

        OutlinedTextField(
            value = usuario,
            onValueChange = usuarioChange,
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = 1,
            singleLine = true, colors = colors, isError = !isValid && usuario.isNotEmpty()
        )

    }

}

@Composable
fun RowImage() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.width(100.dp),
            painter = painterResource(id = R.drawable.minimark),
            contentDescription = "Image Login"
        )
    }

}