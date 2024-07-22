package pe.idat.androidproyecto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AuthViewModel: ViewModel(){
    private val _usuario = MutableLiveData<String>()
    val usuario : LiveData<String> get()  = _usuario
    private val _password = MutableLiveData<String>()
    val password : LiveData<String> get()  =  _password

    fun onLoginTextChanged(usuario:String, password:String){
        _usuario.value = usuario
        _password.value = password
    }

    fun autenticar(): Boolean{
        if(usuario.value.equals("paulo") && password.value.equals("12345")){
            return true
        }
        return false
    }
}