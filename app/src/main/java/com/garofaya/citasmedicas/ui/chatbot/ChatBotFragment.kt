package com.garofaya.citasmedicas.ui.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.garofaya.citasmedicas.api.RetrofitClient
import com.garofaya.citasmedicas.api.models.*
import com.garofaya.citasmedicas.databinding.FragmentChatbotBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.garofaya.citasmedicas.api.models.ChatbotInfoResponse
import com.garofaya.citasmedicas.api.models.RegistrarCitaRequest
import com.garofaya.citasmedicas.api.models.RegistrarCitaResponse
import com.garofaya.citasmedicas.api.models.ValidarDniRequest
import com.garofaya.citasmedicas.api.models.ValidarDniResponse


class ChatBotFragment : Fragment() {

    private lateinit var binding: FragmentChatbotBinding
    private val api = RetrofitClient.chatBotService
    private val mensajes = mutableListOf<Mensaje>()
    private lateinit var adapter: ChatAdapter

    private var info: ChatbotInfoResponse? = null
    private var estado = 0

    private var especialidadSeleccionada: Especialidad? = null
    private var doctorSeleccionado: Doctor? = null
    private var disponibilidadSeleccionada: Disponibilidad? = null

    // Variables para los datos del paciente
    private var dni: String = ""
    private var nombres: String = ""
    private var apellidos: String = ""
    private var email: String = ""
    private var celular: String = ""
    private var motivo: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatbotBinding.inflate(inflater, container, false)

        // Configurar RecyclerView
        adapter = ChatAdapter(mensajes)
        binding.rvMensajes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMensajes.adapter = adapter

        agregarMensaje("👋 ¡Hola! Soy tu asistente Medisol. Cargando especialidades...", false)
        obtenerInformacionChatbot()

        binding.btnEnviar.setOnClickListener {
            val texto = binding.etMensaje.text.toString().trim()
            if (texto.isNotEmpty()) {
                agregarMensaje(texto, true)
                binding.etMensaje.setText("")
                procesarMensaje(texto)
            }
        }

        return binding.root
    }

    private fun agregarMensaje(texto: String, esUsuario: Boolean) {
        mensajes.add(Mensaje(texto, esUsuario))
        adapter.notifyItemInserted(mensajes.size - 1)
        binding.rvMensajes.scrollToPosition(mensajes.size - 1)
    }

    private fun obtenerInformacionChatbot() {
        api.getChatbotInfo().enqueue(object : Callback<ChatbotInfoResponse> {
            override fun onResponse(
                call: Call<ChatbotInfoResponse>,
                response: Response<ChatbotInfoResponse>
            ) {
                if (response.isSuccessful) {
                    info = response.body()
                    agregarMensaje("¿Qué especialidad deseas consultar? (Elige el número o escribe el nombre)", false)
                    mostrarOpcionesEspecialidadesNumeradas()
                } else {
                    agregarMensaje("⚠️ Error al obtener información del servidor.", false)
                }
            }

            override fun onFailure(call: Call<ChatbotInfoResponse>, t: Throwable) {
                agregarMensaje("🚫 Error de conexión: ${t.message}", false)
            }
        })
    }

    private fun mostrarOpcionesEspecialidadesNumeradas() {
        info?.especialidades?.forEachIndexed { index, especialidad ->
            agregarMensaje("${index + 1}. ${especialidad.nombre}", false)
        }
        estado = 1
    }

    private fun mostrarDoctoresNumerados() {
        val doctores = info?.doctores?.filter {
            it.titulo.contains(especialidadSeleccionada!!.nombre, ignoreCase = true)
        } ?: emptyList()

        if (doctores.isEmpty()) {
            agregarMensaje("😢 No hay doctores disponibles en esta especialidad.", false)
            return
        }

        doctores.forEachIndexed { index, it ->
            agregarMensaje("${index + 1}. 👨‍⚕️ ${it.nombres} ${it.apellidos}", false)
        }
        estado = 2
    }

    private fun mostrarDisponibilidadesNumeradas() {
        val disp = info?.disponibilidades?.filter { it.doctor_id == doctorSeleccionado!!.id } ?: emptyList()

        if (disp.isEmpty()) {
            agregarMensaje("😢 No hay fechas disponibles para este doctor.", false)
            return
        }

        disp.take(5).forEachIndexed { index, it ->
            agregarMensaje("${index + 1}. 📅 ${it.fecha} (${it.hora_inicio})", false)
        }
        estado = 3
    }

    private fun procesarMensaje(texto: String) {
        when (estado) {
            1 -> { // Selección de especialidad
                especialidadSeleccionada = when {
                    texto.toIntOrNull() != null -> info?.especialidades?.getOrNull(texto.toInt() - 1)
                    else -> info?.especialidades?.find { it.nombre.equals(texto, ignoreCase = true) }
                }

                if (especialidadSeleccionada != null) {
                    agregarMensaje("Excelente. Doctores disponibles en ${especialidadSeleccionada!!.nombre}:", false)
                    mostrarDoctoresNumerados()
                } else {
                    agregarMensaje("❌ No reconocí esa especialidad. Intenta nuevamente.", false)
                    mostrarOpcionesEspecialidadesNumeradas()
                }
            }

            2 -> { // Selección de doctor
                val doctoresFiltrados = info?.doctores?.filter {
                    it.titulo.contains(especialidadSeleccionada!!.nombre, ignoreCase = true)
                } ?: emptyList()

                doctorSeleccionado = when {
                    texto.toIntOrNull() != null -> doctoresFiltrados.getOrNull(texto.toInt() - 1)
                    else -> doctoresFiltrados.find { (it.nombres + " " + it.apellidos).equals(texto, ignoreCase = true) }
                }

                if (doctorSeleccionado != null) {
                    agregarMensaje("Perfecto. Fechas disponibles para el Dr. ${doctorSeleccionado!!.nombres}:", false)
                    mostrarDisponibilidadesNumeradas()
                } else {
                    agregarMensaje("❌ No reconocí ese doctor. Intenta nuevamente.", false)
                    mostrarDoctoresNumerados()
                }
            }

            3 -> { // Selección de fecha
                val dispList = info?.disponibilidades?.filter { it.doctor_id == doctorSeleccionado!!.id } ?: emptyList()
                val disponibilidad = when {
                    texto.toIntOrNull() != null -> dispList.getOrNull(texto.toInt() - 1)
                    else -> dispList.find { it.fecha.equals(texto, ignoreCase = true) }
                }

                if (disponibilidad != null) {
                    disponibilidadSeleccionada = disponibilidad
                    estado = 4
                    agregarMensaje("Por favor, ingresa tu DNI:", false)
                } else {
                    agregarMensaje("⚠️ No tengo esa fecha disponible. Intenta nuevamente.", false)
                    mostrarDisponibilidadesNumeradas()
                }
            }

            4 -> {
                dni = texto
                validarDniAntesDeRegistrar()
            }

            5 -> { nombres = texto; estado = 6; agregarMensaje("Tus apellidos:", false) }
            6 -> { apellidos = texto; estado = 7; agregarMensaje("Tu email:", false) }
            7 -> { email = texto; estado = 8; agregarMensaje("Tu celular:", false) }
            8 -> { celular = texto; estado = 9; agregarMensaje("Motivo de la cita:", false) }
            9 -> { motivo = texto; registrarCita() }
        }
    }

    private fun validarDniAntesDeRegistrar() {
        val request = ValidarDniRequest(dni = dni)  // ✅ Usar el objeto correcto
        api.validarDni(request).enqueue(object : Callback<ValidarDniResponse> {
            override fun onResponse(call: Call<ValidarDniResponse>, response: Response<ValidarDniResponse>) {
                if (response.isSuccessful) {
                    val validar = response.body()
                    if (validar != null && validar.valido) {
                        // DNI válido
                        nombres = validar.usuario?.nombres ?: ""
                        apellidos = validar.usuario?.apellidos ?: ""
                        email = validar.usuario?.email ?: ""
                        celular = validar.usuario?.celular ?: ""
                        estado = 5
                        agregarMensaje("DNI válido, por favor ingresa tu nombre:", false)
                    } else {
                        agregarMensaje("DNI inválido o no registrado. Ingresa manualmente los datos.", false)
                        estado = 5
                    }
                } else {
                    agregarMensaje("Error al validar DNI: ${response.code()}", false)
                    estado = 5
                }
            }

            override fun onFailure(call: Call<ValidarDniResponse>, t: Throwable) {
                agregarMensaje("Error de conexión al validar DNI: ${t.message}", false)
                estado = 5
            }
        })
    }

    private fun registrarCita() {
        val body = RegistrarCitaRequest(
            dni = dni,
            nombres = nombres,
            apellidos = apellidos,
            email = email,
            celular = celular,
            doctor_id = doctorSeleccionado!!.id,
            fecha = disponibilidadSeleccionada!!.fecha,
            hora_inicio = disponibilidadSeleccionada!!.hora_inicio,
            motivo = motivo
        )

        api.registrarCita(body).enqueue(object : Callback<RegistrarCitaResponse> {
            override fun onResponse(call: Call<RegistrarCitaResponse>, response: Response<RegistrarCitaResponse>) {
                if (response.isSuccessful) {
                    agregarMensaje("✅ Cita registrada correctamente para el ${disponibilidadSeleccionada!!.fecha} a las ${disponibilidadSeleccionada!!.hora_inicio}.", false)
                    estado = 0
                } else {
                    agregarMensaje("❌ Error al registrar la cita: ${response.code()}", false)
                }
            }

            override fun onFailure(call: Call<RegistrarCitaResponse>, t: Throwable) {
                agregarMensaje("🚫 No se pudo registrar la cita: ${t.message}", false)
            }
        })
    }
}
