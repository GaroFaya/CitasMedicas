package com.garofaya.citasmedicas.ui.citas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.garofaya.citasmedicas.api.models.RegistrarCitaResponse
import com.garofaya.citasmedicas.databinding.ItemCitaBinding

class CitasAdapter(private var citas: List<RegistrarCitaResponse>) :
    RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    class CitaViewHolder(val binding: ItemCitaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = ItemCitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        // Mostrar solo lo que existe
        holder.binding.txtDoctor.text = "👨‍⚕️ ID Cita: ${cita.id ?: "N/A"}"
        holder.binding.txtFecha.text = "📅 Mensaje: ${cita.mensaje ?: "Sin mensaje"}"
        holder.binding.txtMotivo.text = "🩺 Error: ${cita.error ?: "N/A"}"
        holder.binding.txtClinica.text = "🏥 N/A" // No tienes info de clínica
    }

    override fun getItemCount(): Int = citas.size

    fun actualizarCitas(nuevasCitas: List<RegistrarCitaResponse>) {
        citas = nuevasCitas
        notifyDataSetChanged()
    }
}
