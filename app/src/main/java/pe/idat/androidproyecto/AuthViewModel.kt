package pe.idat.androidproyecto

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.idat.androidproyecto.data.network.request.Cliente
import pe.idat.androidproyecto.data.network.request.ClienteResponse
import pe.idat.androidproyecto.data.network.request.ClienteUpdate
import pe.idat.androidproyecto.data.network.request.DetalleVenta
import pe.idat.androidproyecto.data.network.request.LoginRequest
import pe.idat.androidproyecto.data.network.request.Producto
import pe.idat.androidproyecto.data.network.request.VentaRequest
import pe.idat.androidproyecto.data.network.response.ClienteRequest
import pe.idat.androidproyecto.data.network.response.LoginResponse
import pe.idat.androidproyecto.data.network.response.ProductoResponse
import pe.idat.androidproyecto.data.network.response.ResponseMessage
import pe.idat.androidproyecto.data.network.response.VentaResponse
import pe.idat.androidproyecto.domain.GetPostUseCase
import pe.idat.androidproyecto.model.Compra
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {


    private val _compraList = MutableStateFlow<List<Compra>>(emptyList())
    val compraList: StateFlow<List<Compra>> get() = _compraList


    private val _usuario = MutableStateFlow("")
    val usuario: StateFlow<String> get() = _usuario


    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password


    private val _productos = MutableStateFlow<List<ProductoResponse>>(emptyList())
    val productos: StateFlow<List<ProductoResponse>> get() = _productos


    private val _clienteResponse = MutableStateFlow<ClienteResponse?>(null)
    val clienteResponse: StateFlow<ClienteResponse?> get() = _clienteResponse


    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> get() = _registerError


    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> get() = _loginResponse


    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError


    private val _updateMessage = MutableStateFlow<ResponseMessage?>(null)
    val updateMessage: StateFlow<ResponseMessage?> get() = _updateMessage

    private val _ventaResponse = MutableStateFlow<VentaResponse?>(null)
    val ventaResponse: StateFlow<VentaResponse?> get() = _ventaResponse

    private val _ventaError = MutableStateFlow<String?>(null)
    val ventaError: StateFlow<String?> get() = _ventaError




    fun productsByCategory(categoria: String) {
        viewModelScope.launch {
            try {
                val products = getPostUseCase(categoria)
                _productos.value = products
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun registerClient(clienteRequest: ClienteRequest) {
        viewModelScope.launch {
            try {
                val response = getPostUseCase(clienteRequest)
                if (response != null) {
                    _clienteResponse.value = response
                    _registerError.value = null
                } else {
                    _registerError.value = "Error en el registro"
                }
            } catch (e: Exception) {
                _registerError.value = "Excepción: ${e.message}"
                e.printStackTrace()
            }
        }
    }


    fun login(usuario: String, password: String) {
        viewModelScope.launch {
            try {

                val response = getPostUseCase.login(LoginRequest(usuario, password))

                if (response != null) {
                    _loginResponse.value = response
                    _loginError.value = null
                } else {
                    _loginError.value = "Usuario y/o Contraseña incorrectos"
                }
            } catch (e: Exception) {
                _loginError.value = "Excepción: ${e.message}"
                e.printStackTrace()
            }
        }
    }



    fun updateCliente(id: Long, clienteUpdate: ClienteUpdate) {
        viewModelScope.launch {
            try {
                val response = getPostUseCase.updateCliente(id, clienteUpdate)
                if (response != null) {
                    _updateMessage.value = ResponseMessage(message = response.message)
                } else {
                    _updateMessage.value = ResponseMessage(message = "Error al actualizar el perfil")
                }
            } catch (e: Exception) {
                _updateMessage.value = ResponseMessage(message = "Excepción: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun registrarVenta() {
        viewModelScope.launch {
            try {
                val ventaRequest = createVentaRequest()
                if (ventaRequest == null) {
                    _ventaError.value = "No se pudo crear la solicitud de venta: Cliente no logueado."
                    return@launch
                }
                val response = getPostUseCase(ventaRequest)
                if (response != null) {
                    _ventaResponse.value = response
                    _ventaError.value = null
                    Log.d("VentaResponse", "Venta registrada exitosamente: ${response.toString()}")
                    println("Venta registrada exitosamente")
                } else {
                    _ventaError.value = "Error al registrar la venta"
                    println("Error al registrar la venta: Response is null")
                }
            } catch (e: Exception) {
                _ventaError.value = "Excepción: ${e.message}"
                println("Excepción: ${e.message}")
                e.printStackTrace()
            }
        }
    }


    fun createVentaRequest(): VentaRequest? {
        val clienteId = loginResponse.value?.idcliente
        if (clienteId == null) {
            // Manejar el caso donde el idcliente es nulo
            println("Error: El cliente no está logueado.")
            return null // O maneja esto como sea necesario
        }

        val cliente = Cliente(idcliente = clienteId)
        val fecha = getCurrentDate()

        val detalleVenta = compraList.value.map { compra ->
            DetalleVenta(
                producto = Producto(id = compra.id), // Aquí estamos usando el id del producto
                cantidad = compra.cantidad
            )
        }

        return VentaRequest(
            cliente = cliente,
            fecha = fecha,
            detalleVenta = detalleVenta
        )
    }



    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.format(Date())
    }


    fun addToCart(compra: Compra) {
        _compraList.value = _compraList.value + compra
        println("Producto añadido al carrito: ${compra.nombreProducto}")
        println("Carrito actual: ${_compraList.value}")
    }

    fun removeAllFromCart() {
        _compraList.value = emptyList()
    }

    fun removeFromCart(compra: Compra) {
        _compraList.value = _compraList.value - compra
    }

    fun updateQuantity(compra: Compra, newQuantity: Int) {
        _compraList.value = _compraList.value.map {
            if (it == compra) it.copy(cantidad = newQuantity) else it
        }
    }

    fun getTotal(): Double {
        return _compraList.value.sumOf { it.cantidad * it.precio }
    }
    val total: StateFlow<Double> = _compraList.map { compraList ->
        compraList.sumOf { it.cantidad * it.precio }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

}
