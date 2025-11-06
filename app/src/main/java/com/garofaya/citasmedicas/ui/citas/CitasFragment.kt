package com.garofaya.citasmedicas.ui.citas

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.garofaya.citasmedicas.api.RetrofitClient
import com.garofaya.citasmedicas.api.models.ListarCitaResponse
import com.garofaya.citasmedicas.databinding.FragmentCitasBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitasFragment : Fragment() {

    private lateinit var binding: FragmentCitasBinding
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
        val sharedPref = requireActivity().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val token = sharedPref.getString("ACCESS_TOKEN", null)

        if (token.isNullOrEmpty()) {
            binding.textSinCitas.visibility = View.VISIBLE
            binding.textSinCitas.text = "No estás logueado"
            return
        }

        RetrofitClient.citaService.listarCitas("Bearer $token").enqueue(object :
            Callback<List<ListarCitaResponse>> {
            override fun onResponse(
                call: Call<List<ListarCitaResponse>>,
                response: Response<List<ListarCitaResponse>>
            ) {
                if (response.isSuccessful) {
                    val citas = response.body() ?: emptyList()
                    if (citas.isEmpty()) {
                        binding.textSinCitas.visibility = View.VISIBLE
                        binding.textSinCitas.text = "No tienes citas registradas aún."
                    } else {
                        binding.textSinCitas.visibility = View.GONE
                        adapter.actualizarCitas(citas)
                    }
                } else {
                    binding.textSinCitas.visibility = View.VISIBLE
                    binding.textSinCitas.text = "Error al obtener citas: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<ListarCitaResponse>>, t: Throwable) {
                binding.textSinCitas.visibility = View.VISIBLE
                binding.textSinCitas.text = "Error de red: ${t.message}"
            }
        })
    }
}
