package com.garofaya.citasmedicas.ui.citas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.garofaya.citasmedicas.api.models.ListarCitaResponse
import com.garofaya.citasmedicas.databinding.ItemCitaBinding

class CitasAdapter(private var citas: List<ListarCitaResponse>) :
    RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    class CitaViewHolder(val binding: ItemCitaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = ItemCitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        holder.binding.txtDoctor.text = "👨‍⚕️ Doctor: ${cita.doctor_nombre}"
        holder.binding.txtClinica.text = "🏥 Clínica: ${cita.clinica_nombre}"
        holder.binding.txtFecha.text = "📅 Fecha: ${cita.fecha} ${cita.hora_inicio} - ${cita.hora_fin}"
        holder.binding.txtMotivo.text = "🩺 Motivo: ${cita.motivo}"
        holder.binding.txtEstado.text = "⚡ Estado: ${cita.estado}"
    }

    override fun getItemCount(): Int = citas.size

    fun actualizarCitas(nuevasCitas: List<ListarCitaResponse>) {
        citas = nuevasCitas
        notifyDataSetChanged()
    }
}
