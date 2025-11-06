package com.garofaya.citasmedicas

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.garofaya.citasmedicas.api.UsuarioService
import com.garofaya.citasmedicas.beans.UsuarioRegistro
import com.garofaya.citasmedicas.databinding.ActivityRegistroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var usuarioService: UsuarioService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipos_documento, // array definido en strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipoDocumento.adapter = adapter

        setupRetrofit()

        binding.btnRegistrarse.setOnClickListener {
            val tipoDocumento = binding.spinnerTipoDocumento.selectedItem.toString() // <-- nuevo
            val dni = binding.txtNumeroDocumento.text.toString()
            val nombres = binding.txtNombres.text.toString()
            val apellidoPaterno = binding.txtApellidoPaterno.text.toString()
            val apellidoMaterno = binding.txtApellidoMaterno.text.toString()
            val email = binding.txtEmail.text.toString()
            val telefono = binding.txtCelular.text.toString()
            val direccion = binding.txtDireccion.text.toString()
            val password = binding.txtPassword.text.toString()
            val password2 = binding.txtConfirmPassword.text.toString()
            val username = email // puedes usar email como username

            // Validaciones
            if (dni.isEmpty() || nombres.isEmpty() || apellidoPaterno.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios (*)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != password2) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = UsuarioRegistro(
                tipoDocumento = tipoDocumento, // <-- asignar tipo documento
                dni = dni,
                email = email,
                username = username,
                password = password,
                password2 = password2,
                first_name = nombres,
                last_name = "$apellidoPaterno $apellidoMaterno",
                telefono = telefono,
                direccion = direccion
            )

            registrarUsuario(usuario)
        }

        binding.btnCancelar.setOnClickListener {
            finish() // cierra la actividad
        }
    }

    private fun setupRetrofit() {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apis.ntechs.net.pe/medisol/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        usuarioService = retrofit.create(UsuarioService::class.java)
    }

    private fun registrarUsuario(usuario: UsuarioRegistro) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = usuarioService.registrar(usuario)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegistroActivity, "Usuario registrado correctamente", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegistroActivity, "Error al registrar: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@RegistroActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
