package pe.idat.androidproyecto.viewlogin

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.idat.androidproyecto.R
import pe.idat.androidproyecto.route.Rutas

@SuppressLint("UnrememberedMutableState")
@Composable
fun RegistrarScreen(navController: NavController) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var isValidEmail by rememberSaveable { mutableStateOf(false) }
    var isValidPassword by rememberSaveable { mutableStateOf(false) }
    var isValidConfirmPassword by rememberSaveable { mutableStateOf(false) }
    var celular by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isFormValid by derivedStateOf {
        nombre.isNotEmpty() && isValidEmail && celular.isNotEmpty() && isValidPassword && password == confirmPassword
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFb5e48c))
            .padding(16.dp),
        contentAlignment = Alignment.Center
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
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                   RowImage()
                    Text(
                        text = "Registrate",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                    )
                    RowUsuario(usuario = nombre, usuarioChange = { nombre = it }, isValid = nombre.length >= 2, label = "Nombre", keyboardType = KeyboardType.Text)

                    RowEmail(email = email, emailChange = {
                        email = it
                        isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    }, isValidEmail)

                    RowUsuario(usuario = celular, usuarioChange = { celular = it }, isValid = celular.length >= 9, label = "Celular", keyboardType = KeyboardType.Phone)


                    RowPassword(
                        contrasena = password,
                        passwordChange = {
                            password = it
                            isValidPassword = password.length >= 5
                        },
                        passwordVisible = passwordVisible,
                        passwordVisibleChange = { passwordVisible = !passwordVisible },
                        isValidPassword = isValidPassword
                    )

                    RowPassword(
                        contrasena = confirmPassword,
                        passwordChange = {
                            confirmPassword = it
                            isValidConfirmPassword = confirmPassword.length >= 5 && confirmPassword == password
                        },
                        passwordVisible = confirmPasswordVisible,
                        passwordVisibleChange = { confirmPasswordVisible = !confirmPasswordVisible },
                        isValidPassword = isValidConfirmPassword
                    )

                    RowButtonRegistrar(isValidForm = isFormValid, text = "Registrar")

                    TextButton(
                        onClick = { navController.navigate(Rutas.Login.ruta) }
                    ) {
                        Text(text = "Ya tienes una cuenta? Iniciar Sesion")
                    }
                }
            }
        }
    }
}

@Composable
fun RowEmail(email: String, emailChange: (String) -> Unit, isValid: Boolean) {
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
            value = email,
            onValueChange = emailChange,
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            singleLine = true,
            colors = colors,
            isError = !isValid && email.isNotEmpty()
        )
    }
}

@Composable
fun RowButtonRegistrar(isValidForm: Boolean,text:String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Handle sign up */ },
            enabled = isValidForm
        ) {
            Text(text = text)
        }
    }
}
