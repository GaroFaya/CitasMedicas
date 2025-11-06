package com.garofaya.citasmedicas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.garofaya.citasmedicas.db.LocalDataBase
import com.garofaya.citasmedicas.db.entity.Usuario as Usuariodb
import com.garofaya.citasmedicas.repository.UsuarioRepository
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var txtDNI: EditText
    private lateinit var txtPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtMensajeError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        inicializarComponentes()
        inicializarEventos()

        val btnRegistro: Button = findViewById(R.id.btnRegistrarse)
        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        inicializarComponentes()
        val localbase = LocalDataBase.getInstance(this)
        val usuarioDao = localbase.usuarioDao()
        val usuario = usuarioDao.listar() // usuario guardado en Room
        if (usuario != null) {
            // Forzar que token no sea nulo
            val token = usuario.token ?: ""
            irHome(usuario.name, usuario.dni, token)
        }
    }

    private fun inicializarComponentes() {
        txtDNI = findViewById(R.id.txtDniLogin)
        txtPass = findViewById(R.id.txtPassLogin)
        btnLogin = findViewById(R.id.btnIngresar)
        txtMensajeError = findViewById(R.id.txtMensajeError)
    }

    private fun inicializarEventos() {
        btnLogin.setOnClickListener {
            if (validarCampos()) {
                apilogin()
            }
        }
    }

    private fun apilogin() {
        val usuarioRepository = UsuarioRepository()
        val dni = txtDNI.text.toString()
        val password = txtPass.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val respuesta = usuarioRepository.login(dni, password)

                withContext(Dispatchers.Main) {
                    if (respuesta.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val jsonRespuesta =
                            gson.toJson(JsonParser.parseString(respuesta.body()?.string()))
                        Log.i("json de respuesta", jsonRespuesta)

                        val jsonObjct = JSONObject(jsonRespuesta)
                        val refreshToken = jsonObjct.optString("refresh", "")
                        val accessToken = jsonObjct.optString("access", "")

                        if (accessToken.isNotEmpty()) {
                            // Guardar usuario en Room
                            val usuarioBd = Usuariodb(
                                id = 1,
                                name = "Usuario",
                                dni = dni,
                                password = password,
                                token = accessToken
                            )
                            guardarUsuariobd(usuarioBd)

                            // Guardar token y userId en SharedPreferences
                            val sharedPref =
                                getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("ACCESS_TOKEN", accessToken)
                                putInt("USER_ID", 1) // Ajusta según ID real del usuario
                                apply()
                            }

                            irHome(usuarioBd.name, usuarioBd.dni, accessToken)
                        } else {
                            txtMensajeError.text = "Error: respuesta del servidor inválida"
                            txtMensajeError.visibility = View.VISIBLE
                        }
                    } else {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val jsonRespuesta =
                            gson.toJson(JsonParser.parseString(respuesta.errorBody()?.string()))
                        Log.i("ERROR json de respuesta", jsonRespuesta)
                        val jsonObjct = JSONObject(jsonRespuesta)
                        txtMensajeError.text =
                            jsonObjct.optString("message", "Error de autenticación")
                        txtMensajeError.visibility = View.VISIBLE
                    }
                }
            } catch (error: Exception) {
                Log.i("Error login", error.message.toString())
            }
        }
    }

    private fun guardarUsuariobd(usuario: Usuariodb) {
        val localBase = LocalDataBase.getInstance(this)
        val usuarioDao = localBase.usuarioDao()
        usuarioDao.insert(usuario)
    }

    private fun irHome(nombre: String, dni: String, token: String) {
        val intentHome = Intent(this, MainActivity::class.java)
        intentHome.putExtra("nombreUsuario", nombre)
        intentHome.putExtra("dniUsuario", dni)
        intentHome.putExtra("accessToken", token)
        startActivity(intentHome)
        finish()
    }

    private fun validarCampos(): Boolean {
        var correcto = true
        if (txtDNI.text.toString().isBlank()) {
            correcto = false
            txtDNI.error = "Ingresa DNI"
        }
        if (txtPass.text.toString().isBlank()) {
            correcto = false
            txtPass.error = "Ingresa contraseña"
        }
        return correcto
    }
}
