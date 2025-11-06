package com.garofaya.citasmedicas.ui.citas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.garofaya.citasmedicas.api.RetrofitClient
import com.garofaya.citasmedicas.api.models.RegistrarCitaResponse
import com.garofaya.citasmedicas.databinding.FragmentCitasBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitasFragment : Fragment() {

    private lateinit var binding: FragmentCitasBinding
    private val api = RetrofitClient.citaService
    private lateinit var adapter: CitasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitasBinding.inflate(inflater, container, false)
        binding.recyclerCitas.layoutManager = LinearLayoutManager(requireContext())

        adapter = CitasAdapter(emptyList())
        binding.recyclerCitas.adapter = adapter

        cargarCitas()

        return binding.root
    }

    private fun cargarCitas() {
        val pacienteId = 1 // Aquí usarías el ID real del paciente logueado
        api.listarCitas(pacienteId).enqueue(object : Callback<List<RegistrarCitaResponse>> {
            override fun onResponse(
                call: Call<List<RegistrarCitaResponse>>,
                response: Response<List<RegistrarCitaResponse>>
            ) {
                if (response.isSuccessful) {
                    val citas = response.body() ?: emptyList()
                    adapter.actualizarCitas(citas)
                    println("✅ Citas obtenidas: $citas")
                } else {
                    println("❌ Error al obtener citas: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<RegistrarCitaResponse>>, t: Throwable) {
                println("⚠️ Error de red: ${t.message}")
            }
        })
    }
}
